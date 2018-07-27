package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.IInteractionObject;
import org.dimdev.rift.listener.client.GameGuiAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Inject(method = "displayGui", at = @At("HEAD"), cancellable = true)
    public void onDisplayGui(IInteractionObject interactionObject, CallbackInfo ci) {
        String id = interactionObject.getGuiID();
        if (!id.startsWith("minecraft:")) {
            for (GameGuiAdder gameGuiAdder : RiftLoader.instance.getListeners(GameGuiAdder.class)) {
                gameGuiAdder.displayGui((EntityPlayerSP) (Object) this, id, interactionObject);
            }
            ci.cancel();
        }
    }

    @Inject(method = "displayGUIChest", at = @At("HEAD"), cancellable = true)
    public void onDisplayContainerGui(IInventory inventory, CallbackInfo ci) {
        String id = inventory instanceof IInteractionObject ? ((IInteractionObject) inventory).getGuiID() : "minecraft:container";
        if (!id.startsWith("minecraft:")) {
            for (GameGuiAdder gameGuiAdder : RiftLoader.instance.getListeners(GameGuiAdder.class)) {
                gameGuiAdder.displayContainerGui((EntityPlayerSP) (Object) this, id, inventory);
            }
            ci.cancel();
        }
    }
}
