package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import org.dimdev.rift.listener.CustomPayloadHandler;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    @Shadow private WorldClient world;

    @Inject(method = "handleCustomPayload", at = @At("RETURN"))
    private void handleModCustomPayload(SPacketCustomPayload packet, CallbackInfo ci) {
        for (CustomPayloadHandler customPayloadHandler : RiftLoader.instance.getListeners(CustomPayloadHandler.class)) {
            if (customPayloadHandler.clientHandlesChannel(packet.getChannelName())) {
                customPayloadHandler.clientHandleCustomPayload(packet.getChannelName(), packet.getBufferData());
            }
        }
    }

    @Inject(method = "handleUpdateTileEntity", at = @At("RETURN"))
    private void handleUpdateModTileEntity(SPacketUpdateTileEntity packet, CallbackInfo ci) {
        TileEntity tileEntity = world.getTileEntity(packet.getPos());
        if (tileEntity == null || packet.getNbtCompound() == null || !packet.getNbtCompound().hasKey("id", 8)) {
            return;
        }

        ResourceLocation tileEntityId = TileEntityType.getId(tileEntity.getTileEntityType());
        ResourceLocation packetId = new ResourceLocation(packet.getNbtCompound().getString("id"));
        if (packetId != null && !packetId.getNamespace().equals("minecraft") && packetId.equals(tileEntityId)) {
            tileEntity.readFromNBT(packet.getNbtCompound());
        }
    }
}
