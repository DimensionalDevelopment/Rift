package org.dimdev.rift.mixin.optifine.client;

import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;

import org.dimdev.rift.injectedmethods.RiftFluid;

@Mixin(BlockFluidRenderer.class)
public class MixinBlockFluidRenderer {
	@Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/BlockFluidRenderer;atlasSpritesWater:[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", opcode = Opcodes.GETFIELD), method = "render(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/fluid/IFluidState;)Z", constraints = "OPTIFINE(1+)")
	public TextureAtlasSprite[] getWaterFluidTextures(BlockFluidRenderer me, IWorldReader world, BlockPos pos, BufferBuilder buffer, IFluidState state) {
		if (state.getFluid() instanceof RiftFluid) {
			RiftFluid fluid = (RiftFluid) state.getFluid();
			return new TextureAtlasSprite[] {fluid.getStillTexture(), fluid.getFlowingTexture()};
		} else {
			return new TextureAtlasSprite[] {MissingTextureSprite.getSprite(), MissingTextureSprite.getSprite()};
		}
	}

	@Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/BlockFluidRenderer;atlasSpritesLava:[Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", opcode = Opcodes.GETFIELD), method = "render(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/fluid/IFluidState;)Z", constraints = "OPTIFINE(1+)")
	public TextureAtlasSprite[] getLavaFluidTextures(BlockFluidRenderer me, IWorldReader world, BlockPos pos, BufferBuilder buffer, IFluidState state) {
		if (state.getFluid() instanceof RiftFluid) {
			RiftFluid fluid = (RiftFluid) state.getFluid();
			return new TextureAtlasSprite[] {fluid.getStillTexture(), fluid.getFlowingTexture()};
		} else {
			return new TextureAtlasSprite[] {MissingTextureSprite.getSprite(), MissingTextureSprite.getSprite()};
		}
	}

	@Redirect(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/optifine/CustomColors;getFluidColor(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)I", shift = Shift.BEFORE, remap = false), method = "render(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/fluid/IFluidState;)Z", constraints = "OPTIFINE(1+)")
	public int getFluidColours(BlockFluidRenderer me, int optifine, IWorldReader world, BlockPos pos, BufferBuilder buffer, IFluidState state) {
		if (state.getFluid() instanceof RiftFluid) {
			RiftFluid fluid = (RiftFluid) state.getFluid();
			if (fluid.ignoreOptifine()) return fluid.getColorMultiplier(world, pos);
		}

		return optifine;
	}
}