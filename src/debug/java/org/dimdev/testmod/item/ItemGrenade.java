package org.dimdev.testmod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.dimdev.testmod.entity.EntityGrenade;

public class ItemGrenade extends Item {

    public ItemGrenade(Builder builder) {
        super(builder);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            EntityGrenade grenade = new EntityGrenade(world, player);
            grenade.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity(grenade);
        }

        player.addStat(StatList.ITEM_USED.get(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
