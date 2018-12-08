package org.dimdev.rift.mixin.hook;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.RecipeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
    @Shadow @Final private Map<ResourceLocation, IRecipe> recipes;

    @Inject(method = "onResourceManagerReload", at = @At(value = "INVOKE", target = "Ljava/util/Map;size()I", remap = false))
    private void onLoadRecipes(IResourceManager resourceManager, CallbackInfo ci) {
        for (RecipeAdder recipeAdder : RiftLoader.instance.getListeners(RecipeAdder.class)) {
            recipeAdder.addRecipes(recipes, resourceManager);
        }
    }
}
