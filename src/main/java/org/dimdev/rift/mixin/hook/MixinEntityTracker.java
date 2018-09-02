package org.dimdev.rift.mixin.hook;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import org.dimdev.rift.entity.EntityTrackerRegistry;
import org.dimdev.rift.listener.EntityTrackerAdder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTracker.class)
public class MixinEntityTracker {

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "track", at = @At("HEAD"), cancellable = true)
    public void trackEntity(Entity entityIn, CallbackInfo ci) {
        if(EntityTrackerRegistry.TRACKER_INFO.containsKey(entityIn.getType())) {
            EntityTrackerAdder.EntityTrackerInfo info = EntityTrackerRegistry.TRACKER_INFO.get(entityIn.getType());
            ((EntityTracker) (Object) this).track(entityIn, info.getTrackingRange(), info.getUpdateFrequency(), info.shouldSendVelocityUpdates());
            ci.cancel();
        }
    }
}
