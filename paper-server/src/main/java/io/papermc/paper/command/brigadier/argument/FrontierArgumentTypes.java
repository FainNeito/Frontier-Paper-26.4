package io.papermc.paper.command.brigadier.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.SerializableArgument;
import net.minecraft.core.registries.BuiltInRegistries;

/** Snapshot-8 command argument validation, including serializable Brigadier primitives. */
public final class FrontierArgumentTypes {
    private FrontierArgumentTypes() {}
    public static boolean isRecognized(ArgumentType<?> argument) {
        try {
            var nativeType = SerializableArgument.unwrapOrThrow(argument).argument().type();
            return BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getKey(nativeType) != null;
        } catch (UnsupportedOperationException exception) {
            return false;
        }
    }
}
