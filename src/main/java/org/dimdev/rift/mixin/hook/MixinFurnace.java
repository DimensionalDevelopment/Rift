package org.dimdev.rift.mixin.hook;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityFurnace;
import org.dimdev.rift.listener.BurnTimeProvider;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(TileEntityFurnace.class)
public class MixinFurnace {
    @Inject(method = "getBurnTimes", at = @At(value = "RETURN"))
    private static void getBurnTimes(CallbackInfoReturnable<Map<Item, Integer>> cir) {
        for (BurnTimeProvider burnTimeProvider : RiftLoader.instance.getListeners(BurnTimeProvider.class)) {
            burnTimeProvider.registerBurnTimes(cir.getReturnValue());
        }
    }
}
