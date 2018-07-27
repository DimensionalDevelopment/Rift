package org.dimdev.rift.listener.client;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.IInteractionObject;

public interface GameGuiAdder {
    void displayGui(EntityPlayerSP player, String id, IInteractionObject interactionObject);
    void displayContainerGui(EntityPlayerSP player, String id, IInventory inventory);
}
