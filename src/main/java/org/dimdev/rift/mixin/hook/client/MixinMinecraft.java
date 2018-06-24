package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackInfoClient;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackList;
import org.dimdev.rift.listener.ResourcePackFinderAdder;
import org.dimdev.simpleloader.SimpleLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow @Final private ResourcePackList<ResourcePackInfoClient> mcResourcePackRepository;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourcePackList;func_198982_a(Lnet/minecraft/resources/IPackFinder;)V", ordinal = 1))
    private void onAddResourcePacks(ResourcePackList resourcePackList, IPackFinder p_198982_1_) {
        resourcePackList.func_198982_a(p_198982_1_);

        for (ResourcePackFinderAdder resourcePackFinderAdder : SimpleLoader.instance.getListeners(ResourcePackFinderAdder.class)) {
            for (IPackFinder packFinder : resourcePackFinderAdder.getResourcePackFinders()) {
                mcResourcePackRepository.func_198982_a(packFinder);
            }
        }
    }
}
