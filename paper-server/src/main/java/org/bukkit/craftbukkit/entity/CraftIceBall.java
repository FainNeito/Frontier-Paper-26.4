package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.IceBall;

public class CraftIceBall extends CraftThrowableProjectile implements IceBall {

    public CraftIceBall(CraftServer server, net.minecraft.world.entity.projectile.throwableitemprojectile.IceBall entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.throwableitemprojectile.IceBall getHandle() {
        return (net.minecraft.world.entity.projectile.throwableitemprojectile.IceBall) this.entity;
    }
}
