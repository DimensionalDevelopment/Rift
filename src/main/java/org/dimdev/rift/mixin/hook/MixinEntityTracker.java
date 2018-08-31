package org.dimdev.rift.mixin.hook;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityType;
import org.dimdev.rift.entity.EntityTrackerRegistry;
import org.dimdev.rift.listener.EntityTrackerAdder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTracker.class)
public class MixinEntityTracker {

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "track", at = @At("RETURN"))
    public void trackEntity(Entity entityIn, CallbackInfo ci) {
        if(!EntityType.getId(entityIn.getType()).getNamespace().equals("minecraft")) {
            if(EntityTrackerRegistry.TRACKER_INFO.containsKey(entityIn.getType())) {
                EntityTrackerAdder.EntityTrackerInfo info = EntityTrackerRegistry.TRACKER_INFO.get(entityIn.getType());
                ((EntityTracker) (Object) this).track(entityIn, info.getTrackingRange(), info.getUpdateFrequency(), info.shouldSendVelocityUpdates());
            }
        }
    }

}
