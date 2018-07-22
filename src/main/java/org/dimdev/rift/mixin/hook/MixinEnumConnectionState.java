package org.dimdev.rift.mixin.hook;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import org.dimdev.rift.listener.PacketAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnumConnectionState.class)
public abstract class MixinEnumConnectionState {
    @Shadow protected abstract EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet<?>> packetClass);

    @Mixin(targets = "net/minecraft/network/EnumConnectionState$1")
    public abstract static class Handshaking extends MixinEnumConnectionState {
        @Inject(method = "<init>", at = @At("RETURN"))
        private void registerModPackets(CallbackInfo ci) {
            for (PacketAdder packetAdder : RiftLoader.instance.getListeners(PacketAdder.class)) {
                packetAdder.registerHandshakingPackets(this::registerPacket);
            }
        }
    }

    @Mixin(targets = "net/minecraft/network/EnumConnectionState$2")
    public abstract static class Play extends MixinEnumConnectionState {
        @Inject(method = "<init>", at = @At("RETURN"))
        private void registerModPackets(CallbackInfo ci) {
            for (PacketAdder packetAdder : RiftLoader.instance.getListeners(PacketAdder.class)) {
                packetAdder.registerPlayPackets(this::registerPacket);
            }
        }
    }

    @Mixin(targets = "net/minecraft/network/EnumConnectionState$3")
    public abstract static class Status extends MixinEnumConnectionState {
        @Inject(method = "<init>", at = @At("RETURN"))
        private void registerModPackets(CallbackInfo ci) {
            for (PacketAdder packetAdder : RiftLoader.instance.getListeners(PacketAdder.class)) {
                packetAdder.registerStatusPackets(this::registerPacket);
            }
        }
    }

    @Mixin(targets = "net/minecraft/network/EnumConnectionState$4")
    public abstract static class Login extends MixinEnumConnectionState {
        @Inject(method = "<init>", at = @At("RETURN"))
        private void registerModPackets(CallbackInfo ci) {
            for (PacketAdder packetAdder : RiftLoader.instance.getListeners(PacketAdder.class)) {
                packetAdder.registerLoginPackets(this::registerPacket);
            }
        }
    }
}
