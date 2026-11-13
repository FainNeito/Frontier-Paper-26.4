package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Frostbite;

public class CraftFrostbite extends CraftZombie implements Frostbite, com.destroystokyo.paper.entity.CraftRangedEntity<net.minecraft.world.entity.monster.zombie.Frostbite> { // Paper

    public CraftFrostbite(CraftServer server, net.minecraft.world.entity.monster.zombie.Frostbite entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.zombie.Frostbite getHandle() {
        return (net.minecraft.world.entity.monster.zombie.Frostbite) this.entity;
    }
}
