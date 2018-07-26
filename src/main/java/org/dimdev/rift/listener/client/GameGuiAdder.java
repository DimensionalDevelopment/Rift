package org.dimdev.rift.listener.client;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;

public interface GameGuiAdder {
    boolean displayGui(ResourceLocation id, IInteractionObject interactionObject);
    boolean displayContainerGui(ResourceLocation id, IInventory inventory);
}
