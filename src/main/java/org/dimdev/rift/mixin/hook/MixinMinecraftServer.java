package org.dimdev.rift.mixin.hook;

import net.minecraft.profiler.Profiler;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataStorage;

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
import java.util.Map;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Shadow @Final private ResourcePackList<ResourcePackInfo> resourcePacks;
    @Shadow @Final public Profiler profiler;

    @Shadow @Final public Map<DimensionType, WorldServer> worlds;
    @Shadow public abstract WorldServer getWorld(DimensionType dimension);
    @Shadow public abstract GameType getGameType();
    @Shadow public abstract boolean isSinglePlayer();

    @Inject(method = "loadDataPacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourcePackList;reloadPacksFromFinders()V"))
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

    @Inject(method = "func_212369_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerList;func_212504_a(Lnet/minecraft/world/WorldServer;)V"))
    private void onWorldLoad(ISaveHandler saveHandler, WorldSavedDataStorage savedDataStorage, WorldInfo info, WorldSettings settings, CallbackInfo ci) {
    	WorldServer overworld = getWorld(DimensionType.OVERWORLD);

    	for (DimensionType type : DimensionType.func_212681_b()) {
    		//Skip around existing types (the Overworld, Nether and End)
    		if (!worlds.containsKey(type)) {
    			WorldServerMulti world = new WorldServerMulti((MinecraftServer) (Object) this, saveHandler, type, overworld, profiler);
    			world.func_212251_i__();

    	        worlds.put(type, world);

    	        world.addEventListener(new ServerWorldEventHandler((MinecraftServer) (Object) this, world));
    	        if (!isSinglePlayer()) {
    	            world.getWorldInfo().setGameType(getGameType());
    	        }
    		}
    	}
    }
}
