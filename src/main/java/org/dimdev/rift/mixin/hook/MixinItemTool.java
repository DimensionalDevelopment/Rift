package org.dimdev.rift.mixin.hook;

import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import org.dimdev.rift.listener.ToolEfficiencyProvider;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(ItemTool.class)
public class MixinItemTool {
    @Shadow @Mutable @Final private Set<Block> effectiveBlocks;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(float attackDamage, float attackSpeed, IItemTier tier, Set<Block> effectiveBlocks, Item.Builder builder, CallbackInfo ci) {
        this.effectiveBlocks = new HashSet<>(effectiveBlocks);

        for (ToolEfficiencyProvider toolEfficiencyProvider : RiftLoader.instance.getListeners(ToolEfficiencyProvider.class)) {
            toolEfficiencyProvider.addEffectiveBlocks((ItemTool) (Object) this, this.effectiveBlocks);
        }
    }
}
