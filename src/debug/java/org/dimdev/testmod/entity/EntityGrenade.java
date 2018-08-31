package org.dimdev.testmod.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.dimdev.testmod.TestMod;

public class EntityGrenade extends EntityThrowable {

    public EntityGrenade(World world) {
        super(TestMod.ENTITY_TYPE_GRENADE, world);
    }

    public EntityGrenade(World world, EntityLivingBase shooter) {
        super(TestMod.ENTITY_TYPE_GRENADE, shooter, world);
    }

    public EntityGrenade(World world, double x, double y, double z) {
        super(TestMod.ENTITY_TYPE_GRENADE, x, y, z, world);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(this.world.isRemote()) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 12.0F, false);
        }
        this.setDead();
    }

}
