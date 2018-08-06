package org.dimdev.rift.mixin.hook;

import net.minecraft.world.chunk.Chunk;
import org.dimdev.rift.listener.ChunkEventListener;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chunk.class)
public class MixinChunk {
    @Inject(method = "onLoad", at = @At("RETURN"))
    private void onChunkLoad(CallbackInfo ci) {
        for (ChunkEventListener listener : RiftLoader.instance.getListeners(ChunkEventListener.class)) {
            listener.onChunkLoad((Chunk) (Object) this);
        }
    }

    @Inject(method = "onUnload", at = @At("RETURN"))
    private void onChunkUnoad(CallbackInfo ci) {
        for (ChunkEventListener listener : RiftLoader.instance.getListeners(ChunkEventListener.class)) {
            listener.onChunkUnload((Chunk) (Object) this);
        }
    }
}
