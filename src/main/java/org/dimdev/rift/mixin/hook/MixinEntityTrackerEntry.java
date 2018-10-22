package org.dimdev.rift.mixin.hook;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.EntityType;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.network.message.MessageSpawnEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityTrackerEntry.class)
public class MixinEntityTrackerEntry {

    @Shadow
    @Final
    private Entity trackedEntity;

    @Inject(method = "createSpawnPacket", at = @At("INVOKE"), cancellable = true)
    private void createCustomSpawnPacket(CallbackInfoReturnable<Packet<?>> info) {
        if(!this.trackedEntity.isDead) {
            EntityType type = this.trackedEntity.getType();
            ResourceLocation typeName = EntityType.getId(type);
            if(!typeName.getNamespace().equals("minecraft")) {
                info.setReturnValue(new MessageSpawnEntity(this.trackedEntity).toPacket(EnumPacketDirection.CLIENTBOUND));
            }
        }
    }

}
