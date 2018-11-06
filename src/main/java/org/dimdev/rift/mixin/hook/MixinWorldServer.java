package org.dimdev.rift.mixin.hook;

import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import org.dimdev.rift.listener.ChunkGeneratorReplacer;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldServer.class)
public class MixinWorldServer {

    @SuppressWarnings("ConstantConditions")
    @Redirect(method = "createChunkProvider", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;createChunkGenerator()Lnet/minecraft/world/gen/IChunkGenerator;"))
    protected <T extends IChunkGenSettings> IChunkGenerator<?> onCreateChunkGenerator(Dimension dimension) {
        WorldServer world = (WorldServer) (Object) this;
        WorldType type = world.getWorldInfo().getGenerator();
        IChunkGenerator<T> generator = null;
        for(ChunkGeneratorReplacer adder : RiftLoader.instance.getListeners(ChunkGeneratorReplacer.class)) {
            IChunkGenerator<T> value = adder.createChunkGenerator(world, type, dimension.getType().getId());
            if(value != null) generator = value;
        }
        return generator != null ? generator : dimension.createChunkGenerator();
    }
}
