package org.dimdev.rift.mixin.hook;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.rift.listener.EntityTypeAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@Mixin(EntityType.class)
public abstract class MixinEntityType {
    static {
        for (EntityTypeAdder entityTypeAdder : RiftLoader.instance.getListeners(EntityTypeAdder.class)) {
            entityTypeAdder.registerEntityTypes();
        }
    }

    @Mixin(EntityType.Builder.class)
    public abstract static class Builder<T extends Entity> {
        @Shadow private boolean serializable;

        @Shadow @Final private Class<? extends T> entityClass;
        @Shadow @Final private Function<? super World, ? extends T> factory;
        @Shadow private boolean summonable;
        private static final Logger LOGGER = LogManager.getLogger();

        @Overwrite
        public EntityType<T> build(String id) {
            Type<?> dataFixerType = null;

            if (serializable) {
                try {
                    dataFixerType = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(1519)).getChoiceType(TypeReferences.ENTITY_TYPE, id);
                } catch (IllegalStateException | IllegalArgumentException ignored) {
                    LOGGER.debug("No data fixer registered for entity {}", id);
                }
            }

            return new EntityType<>(entityClass, factory, serializable, summonable, dataFixerType);
        }
    }
}
