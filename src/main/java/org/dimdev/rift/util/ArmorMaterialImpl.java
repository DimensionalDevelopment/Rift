package org.dimdev.rift.util;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class ArmorMaterialImpl implements IArmorMaterial {
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final LazyLoadBase<Ingredient> ingredient;

    public ArmorMaterialImpl(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, Supplier<Ingredient> ingredientSupplier) {
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.ingredient = new LazyLoadBase<>(ingredientSupplier);
    }

    @Override
    public int getDurability(EntityEquipmentSlot slot) {
        return ArmorMaterial.MAX_DAMAGE_ARRAY[slot.getIndex()] * maxDamageFactor;
    }

    @Override
    public int getDamageReductionAmount(EntityEquipmentSlot slot) {
        return damageReductionAmountArray[slot.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return ingredient.getValue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }
}
