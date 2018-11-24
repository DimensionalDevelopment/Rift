package org.dimdev.rift.mixin.core.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/IRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/util/ResourceLocation;"))
	private ResourceLocation onGetItem(IRegistry<Object> registry, Object item) {
		ResourceLocation key = registry.getKey(item);

		if (key == null) {
			//Guard against unregistered items to avoid throwing unhelpful NPEs in ModelResourceLocation's constructor
			throw new IllegalStateException("Item registered with null key: " + item + " - " + ((Item) item).getName() + " (" + item.getClass() + ')');
		} else {
			return key;
		}
	}
}