package org.bukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import java.util.Locale;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a fluid type.
 */
public interface Fluid extends OldEnum<Fluid>, Keyed {

    // Start generate - Fluid
    Fluid EMPTY = getFluid("empty");

    Fluid LAVA = getFluid("lava");

    Fluid WATER = getFluid("water");
    // End generate - Fluid

    /** @deprecated flowing and source fluids now share a registry type. */
    @Deprecated(since = "26.4")
    Fluid FLOWING_LAVA = LAVA;

    /** @deprecated flowing and source fluids now share a registry type. */
    @Deprecated(since = "26.4")
    Fluid FLOWING_WATER = WATER;

    @NotNull
    private static Fluid getFluid(@NotNull @KeyPattern.Value String key) {
        return Registry.FLUID.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    /**
     * @param name of the fluid.
     * @return the fluid with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Fluid valueOf(@NotNull String name) {
        final NamespacedKey key = NamespacedKey.fromString(name.toLowerCase(Locale.ROOT));
        Fluid fluid = key == null ? null : Bukkit.getUnsafe().get(RegistryKey.FLUID, key);
        Preconditions.checkArgument(fluid != null, "No fluid found with the name %s", name);
        return fluid;
    }

    /**
     * @return an array of all known fluids.
     * @deprecated use {@link Registry#stream()}.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Fluid[] values() {
        return Registry.FLUID.stream().toArray(Fluid[]::new);
    }
}
