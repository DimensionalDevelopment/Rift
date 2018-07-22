package org.dimdev.rift.mixin.hook;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.injectedmethods.RiftCPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CPacketCustomPayload.class)
public class MixinCPacketCustomPayload implements RiftCPacketCustomPayload {
    @Shadow private ResourceLocation channel;
    @Shadow private PacketBuffer data;

    @Override
    public ResourceLocation getChannelName() {
        return channel;
    }

    @Override
    public PacketBuffer getData() {
        return data;
    }
}
