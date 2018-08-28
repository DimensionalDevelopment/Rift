package org.dimdev.rift.listener;

import net.minecraft.item.crafting.IRecipeSerializer;

public interface RecipeSerializerAdder {
    /**
     * Use {@link net.minecraft.item.crafting.RecipeSerializers#register(IRecipeSerializer)}
     * to register new recipe serializers.
     */
    void addRecipeSerializers();
}
