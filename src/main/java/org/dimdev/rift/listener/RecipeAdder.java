package org.dimdev.rift.listener;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public interface RecipeAdder {
    void addRecipes(Map<ResourceLocation, IRecipe> target, IResourceManager resourceManager);
}
