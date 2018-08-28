package org.dimdev.rift.mixin.hook;

import net.minecraft.item.crafting.RecipeSerializers;
import org.dimdev.rift.listener.RecipeSerializerAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RecipeSerializers.class)
public class MixinRecipeSerializers {
    static {
        for (RecipeSerializerAdder recipeSerializerAdder : RiftLoader.instance.getListeners(RecipeSerializerAdder.class)) {
            recipeSerializerAdder.addRecipeSerializers();
        }
    }
}
