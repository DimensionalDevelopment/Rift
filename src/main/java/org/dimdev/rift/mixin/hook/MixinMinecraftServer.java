package org.dimdev.rift.mixin.hook;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;
import org.dimdev.rift.listener.DataPackFinderAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Shadow @Final private ResourcePackList<ResourcePackInfo> resourcePacks;

    @Inject(method = "func_195560_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourcePackList;addPackFinder(Lnet/minecraft/resources/IPackFinder;)V", shift = At.Shift.AFTER))
    private void afterAddVanillaPackFinder(File serverDirectory, WorldInfo worldInfo, CallbackInfo ci) {
        for (DataPackFinderAdder resourcePackFinderAdder : RiftLoader.instance.getListeners(DataPackFinderAdder.class)) {
            for (IPackFinder packFinder : resourcePackFinderAdder.getDataPackFinders()) {
                resourcePacks.addPackFinder(packFinder);
            }
        }
    }
}
