package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.resources.ResourcePackInfoClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackList;
import org.dimdev.rift.listener.client.KeybindHandler;
import org.dimdev.rift.listener.client.AmbientMusicTypeProvider;
import org.dimdev.rift.listener.client.ClientTickable;
import org.dimdev.rift.listener.ResourcePackFinderAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow @Final private ResourcePackList<ResourcePackInfoClient> resourcePackRepository;
    @Shadow @Final public Profiler profiler;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourcePackList;addPackFinder(Lnet/minecraft/resources/IPackFinder;)V", ordinal = 1))
    private void onAddResourcePacks(ResourcePackList resourcePackList, IPackFinder minecraftPackFinder) {
        resourcePackList.addPackFinder(minecraftPackFinder);

        for (ResourcePackFinderAdder resourcePackFinderAdder : RiftLoader.instance.getListeners(ResourcePackFinderAdder.class)) {
            for (IPackFinder packFinder : resourcePackFinderAdder.getResourcePackFinders()) {
                resourcePackRepository.addPackFinder(packFinder);
            }
        }
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        profiler.startSection("mods");
        for (ClientTickable tickable : RiftLoader.instance.getListeners(ClientTickable.class)) {
            profiler.startSection(() -> tickable.getClass().getCanonicalName().replace('.', '/'));
            tickable.clientTick();
            profiler.endSection();
        }
        profiler.endSection();
    }

    @Inject(method = "getAmbientMusicType", at = @At(value = "INVOKE"), cancellable = true)
    private void getMusicType(CallbackInfoReturnable<MusicTicker.MusicType> cir) {
        for (AmbientMusicTypeProvider ambientMusicTypeProvider : RiftLoader.instance.getListeners(AmbientMusicTypeProvider.class)) {
            MusicTicker.MusicType type = ambientMusicTypeProvider.getAmbientMusicType((Minecraft) (Object) this);

            if(type != null) {
                cir.setReturnValue(type);
                cir.cancel();
                return;
            }
        }
    }

    @Inject(method = "processKeyBinds", at = @At("HEAD"))
    public void onProcessKeyBinds(CallbackInfo ci) {
        for (KeybindHandler keybindHandler : RiftLoader.instance.getListeners(KeybindHandler.class)) {
            keybindHandler.processKeybinds();
        }
    }
}
