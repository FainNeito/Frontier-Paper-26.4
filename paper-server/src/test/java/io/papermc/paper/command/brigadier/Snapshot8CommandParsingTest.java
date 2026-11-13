package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import io.papermc.paper.command.brigadier.argument.WrappedArgumentCommandNode;
import net.minecraft.commands.ParseBox;
import net.minecraft.commands.ParseContext;
import org.bukkit.command.CommandSender;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@VanillaFeature
class Snapshot8CommandParsingTest {
    @Test void detachedRedirectChainsAndSharedTargetsArePreserved() throws Exception {
        var hidden = new com.mojang.brigadier.tree.RootCommandNode<net.minecraft.commands.CommandSourceStack>();
        hidden.addChild(LiteralArgumentBuilder.<net.minecraft.commands.CommandSourceStack>literal("value").executes(c -> 7).build());
        var middle = LiteralArgumentBuilder.<net.minecraft.commands.CommandSourceStack>literal("middle").redirect(hidden).build();
        var dispatcher = new CommandDispatcher<net.minecraft.commands.CommandSourceStack>();
        dispatcher.register(LiteralArgumentBuilder.<net.minecraft.commands.CommandSourceStack>literal("alias").redirect(middle));
        dispatcher.register(LiteralArgumentBuilder.<net.minecraft.commands.CommandSourceStack>literal("second").redirect(hidden));
        var parser = ParseBox.fromFullTree(dispatcher);
        assertDoesNotThrow(() -> parser.parse("second value", mock(ParseContext.class)).unpack());
        assertDoesNotThrow(() -> net.minecraft.commands.SuggestionBox.fromFullTree(dispatcher));
        // A child may redirect to its ancestor without a redirect-only cycle.
        var parent = LiteralArgumentBuilder.<net.minecraft.commands.CommandSourceStack>literal("parent").build();
        parent.addChild(LiteralArgumentBuilder.<net.minecraft.commands.CommandSourceStack>literal("again").redirect(parent).build());
        dispatcher.getRoot().addChild(parent);
        assertDoesNotThrow(() -> ParseBox.fromFullTree(dispatcher));
    }

    @Test void pluginPermissionsRemainSourceAwareAndFailClosed() throws Exception {
        var source = mock(net.minecraft.commands.CommandSourceStack.class);
        var sender = mock(CommandSender.class);
        when(source.getBukkitSender()).thenReturn(sender);
        var context = mock(ParseContext.class);
        when(context.commandSourceStack()).thenReturn(source);
        var dispatcher = new CommandDispatcher<net.minecraft.commands.CommandSourceStack>();
        dispatcher.register(LiteralArgumentBuilder.<net.minecraft.commands.CommandSourceStack>literal("protected")
            .requires(stack -> stack.getBukkitSender().hasPermission("frontier.test")).executes(c -> 1));
        var parser = ParseBox.fromFullTree(dispatcher);
        assertThrows(CommandSyntaxException.class, () -> parser.parse("protected", context).unpack());
        when(sender.hasPermission("frontier.test")).thenReturn(true);
        assertDoesNotThrow(() -> parser.parse("protected", context).unpack());
        assertThrows(CommandSyntaxException.class, () -> parser.parse("protected", mock(ParseContext.class)).unpack());
    }

    @Test @SuppressWarnings({"unchecked", "rawtypes"})
    void clonedPluginArgumentPreservesConvertedValueAndSource() throws Exception {
        var source = mock(net.minecraft.commands.CommandSourceStack.class);
        var context = mock(ParseContext.class);
        when(context.commandSourceStack()).thenReturn(source);
        ArgumentType<String> apiArgument = new ArgumentType<>() {
            @Override public String parse(StringReader reader) throws CommandSyntaxException {
                return "converted-" + reader.readInt();
            }
            @Override public <S> String parse(StringReader reader, S actualSource) throws CommandSyntaxException {
                assertSame(source, actualSource);
                return this.parse(reader);
            }
        };
        var wrapped = new WrappedArgumentCommandNode<Integer, String>("value", apiArgument,
            IntegerArgumentType.integer(), c -> 1, s -> true, null, null, false, null);
        var dispatcher = new CommandDispatcher<net.minecraft.commands.CommandSourceStack>();
        var root = dispatcher.register(LiteralArgumentBuilder.<net.minecraft.commands.CommandSourceStack>literal("plugin"));
        root.addChild((CommandNode) wrapped);
        var parsed = ParseBox.fromFullTree(dispatcher).parse("plugin 42", context).unpack();
        assertEquals("converted-42", parsed.getTopContext().getArgument("value", String.class));
    }
}
