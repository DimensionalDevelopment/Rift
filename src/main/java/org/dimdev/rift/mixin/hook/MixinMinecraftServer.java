package org.dimdev.rift.mixin.hook;

import net.minecraft.profiler.Profiler;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;
import org.dimdev.rift.listener.DataPackFinderAdder;
import org.dimdev.rift.listener.ServerTickable;
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

    @Shadow @Final public Profiler profiler;
    @Inject(method = "func_195560_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourcePackList;addPackFinder(Lnet/minecraft/resources/IPackFinder;)V", shift = At.Shift.AFTER))
    private void afterAddVanillaPackFinder(File serverDirectory, WorldInfo worldInfo, CallbackInfo ci) {
        for (DataPackFinderAdder resourcePackFinderAdder : RiftLoader.instance.getListeners(DataPackFinderAdder.class)) {
            for (IPackFinder packFinder : resourcePackFinderAdder.getDataPackFinders()) {
                resourcePacks.addPackFinder(packFinder);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void onTick(CallbackInfo ci) {
        profiler.startSection("mods");
        for (ServerTickable tickable : RiftLoader.instance.getListeners(ServerTickable.class)) {
            profiler.startSection(() -> tickable.getClass().getCanonicalName().replace('.', '/'));
            tickable.serverTick((MinecraftServer) (Object) this);
            profiler.endSection();
        }
        profiler.endSection();
    }
}
