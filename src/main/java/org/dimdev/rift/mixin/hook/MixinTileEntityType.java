package org.dimdev.rift.mixin.hook;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.util.registry.IRegistry;
import org.apache.logging.log4j.Logger;
import org.dimdev.rift.Rift;
import org.dimdev.rift.listener.TileEntityTypeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityType.class)
public abstract class MixinTileEntityType {
    @Shadow @Final private static Logger LOGGER;

    @Overwrite
    public static <T extends TileEntity> TileEntityType<T> register(String id, TileEntityType.Builder<T> builder) {
        Type<?> dataFixerType = null;

        try {
        	//Schema#getChoiceType can throw an IAE that vanilla won't catch, so we catch that too
            dataFixerType = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(Rift.DATAFIXER_VERSION)).getChoiceType(TypeReferences.BLOCK_ENTITY, id);
        } catch (IllegalStateException | IllegalArgumentException e) {
            LOGGER.debug("No data fixer registered for block entity {}", id);
        }

        TileEntityType<T> tileEntityType = builder.build(dataFixerType);
        IRegistry.BLOCK_ENTITY_TYPE.put(new ResourceLocation(id), tileEntityType);
        return tileEntityType;
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onRegisterTileEntityTypes(CallbackInfo ci) {
        for (TileEntityTypeAdder entityTypeAdder : RiftLoader.instance.getListeners(TileEntityTypeAdder.class)) {
            entityTypeAdder.registerTileEntityTypes();
        }
    }
}
