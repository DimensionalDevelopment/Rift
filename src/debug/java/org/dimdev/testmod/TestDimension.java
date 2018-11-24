package org.dimdev.testmod;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.EndGenSettings;
import net.minecraft.world.gen.IChunkGenerator;

/** Loose clone of {@link EndDimension} */
public class TestDimension extends Dimension {
	private static DimensionType type;

	@Override
	protected void init() {
		hasSkyLight = false;
	}

	@Override
	public IChunkGenerator<?> createChunkGenerator() {
		EndGenSettings endGenerator = ChunkGeneratorType.FLOATING_ISLANDS.createSettings();

		endGenerator.setDefautBlock(Blocks.WHITE_CONCRETE.getDefaultState());
		endGenerator.setDefaultFluid(Blocks.AIR.getDefaultState());
		endGenerator.setSpawnPos(getSpawnCoordinate());

		return ChunkGeneratorType.FLOATING_ISLANDS.create(world, BiomeProviderType.THE_END.create(BiomeProviderType.THE_END.createSettings().setSeed(world.getSeed())), endGenerator);
	}

	@Override
	public BlockPos getSpawnCoordinate() {
		return EndDimension.SPAWN;
	}

	@Override
	public BlockPos findSpawn(ChunkPos chunk, boolean checkValid) {
		Random random = new Random(world.getSeed());
		BlockPos pos = new BlockPos(chunk.getXStart() + random.nextInt(15), 0, chunk.getZEnd() + random.nextInt(15));
		return world.getGroundAboveSeaLevel(pos).getMaterial().blocksMovement() ? pos : null;
	}

	@Override
	public BlockPos findSpawn(int x, int z, boolean checkValid) {
		return findSpawn(new ChunkPos(x >> 4, z >> 4), checkValid);
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return 0F;
	}

	@Override
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
		return null;
	}

	@Override
	public boolean doesXZShowFog(int x, int z) {
		return false;
	}

	@Override
	public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        float sun = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2F + 0.5F;
        sun = MathHelper.clamp(sun, 0F, 1F);

        float r = 0.627451F;
        float g = 0.5019608F;
        float b = 0.627451F;
        r = r * (sun * 0.0F + 0.15F);
        g = g * (sun * 0.0F + 0.15F);
        b = b * (sun * 0.0F + 0.15F);

        return new Vec3d(r, g, b);
	}

	@Override
	public boolean canRespawnHere() {
		return false;
	}

	@Override
	public float getCloudHeight() {
		return 8F;
	}

	static void giveType(DimensionType type) {
		TestDimension.type = type;
	}

	@Override
	public DimensionType getType() {
		if (type == null) throw new IllegalStateException("Null type?");
		return type;
	}
}