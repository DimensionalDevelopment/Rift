package org.dimdev.testmod;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSprite;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.EndDimension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.rift.listener.*;
import org.dimdev.rift.listener.client.AmbientMusicTypeProvider;
import org.dimdev.rift.listener.client.ClientTickable;
import org.dimdev.rift.listener.client.EntityRendererAdder;
import org.dimdev.rift.listener.client.TextureAdder;
import org.dimdev.rift.network.Message;
import org.dimdev.testmod.entity.EntityGrenade;
import org.dimdev.testmod.item.ItemGrenade;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static net.minecraft.init.SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;

public class TestMod implements BlockAdder, ItemAdder, FluidAdder, TextureAdder, PacketAdder, CommandAdder, ClientTickable, /*AmbientMusicTypeProvider,*/ DimensionTypeAdder, MessageAdder, EntityTypeAdder, EntityRendererAdder, EntityTrackerAdder {
    public static EntityType<EntityGrenade> ENTITY_TYPE_GRENADE;
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Block WHITE_BLOCK = new Block(Block.Builder.create(Material.ROCK));
    public static final Block TRANSLUCENT_WHITE_BLOCK = new BlockStainedGlass(EnumDyeColor.WHITE, Block.Builder.create(Material.GLASS));
    public static final FlowingFluid WHITE_FLUID = new WhiteFluid.Source();
    public static final FlowingFluid FLOWING_WHITE_FLUID = new WhiteFluid.Flowing();
    public static final BlockFlowingFluid BLOCK_WHITE_FLUID = new BlockFlowingFluid(WHITE_FLUID, Block.Builder.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100F, 100F).variableOpacity());
    public static final Item PACKET_TESTER = new ItemPacketTester(new Item.Builder().group(ItemGroup.TOOLS));
    public static final Item GRENADE = new ItemGrenade(new Item.Builder().group(ItemGroup.COMBAT).maxStackSize(8));
    public static final MusicTicker.MusicType TEST_MUSIC = AmbientMusicTypeProvider.newMusicType("test", ENTITY_EXPERIENCE_ORB_PICKUP, 0, 0);
    private int clientTickCount = 0;

    @Override
    public void registerBlocks() {
        Block.register(new ResourceLocation("testmod", "white_block"), WHITE_BLOCK);
        Block.register(new ResourceLocation("testmod", "translucent_white_block"), TRANSLUCENT_WHITE_BLOCK);
        Block.register(new ResourceLocation("testmod", "white_fluid"), BLOCK_WHITE_FLUID);
    }

    @Override
    public void registerItems() {
        Item.registerItemBlock(WHITE_BLOCK, ItemGroup.BUILDING_BLOCKS);
        Item.registerItemBlock(TRANSLUCENT_WHITE_BLOCK, ItemGroup.BUILDING_BLOCKS);
        Item.registerItem(new ResourceLocation("testmod", "packet_tester"), PACKET_TESTER);
        Item.register(new ResourceLocation("testmod", "grenade"), GRENADE);
    }

    @Override
    public void registerFluids() {
        Fluid.register(new ResourceLocation("testmod", "white_fluid"), WHITE_FLUID);
        Fluid.register(new ResourceLocation("testmod", "flowing_white_fluid"), FLOWING_WHITE_FLUID);
    }

    @Override
    public Collection<? extends ResourceLocation> getBuiltinTextures() {
        return Collections.singletonList(new ResourceLocation("testmod", "block/white_fluid_flow"));
    }

    @Override
    public void registerHandshakingPackets(PacketRegistrationReceiver receiver) {}

    @Override
    public void registerPlayPackets(PacketRegistrationReceiver receiver) {
        receiver.registerPacket(EnumPacketDirection.SERVERBOUND, CPacketTest.class);
    }

    @Override
    public void registerStatusPackets(PacketRegistrationReceiver receiver) {}

    @Override
    public void registerLoginPackets(PacketRegistrationReceiver receiver) {}

    @Override
    public void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
        ExplosionCommand.register(dispatcher);
        ChangeDimensionCommand.register(dispatcher);
    }

    @Override
    public void clientTick() {
        if (clientTickCount++ == 100) {
            LOGGER.info("100 ticks have passed");
        }
    }

//    @Override
    public MusicTicker.MusicType getAmbientMusicType(Minecraft client) {
        return TEST_MUSIC;
    }

    @Override
    public Set<? extends DimensionType> getDimensionTypes() {
        return Collections.singleton(DimensionTypeAdder.newDimensionType(555, "test_dimension", "_test", EndDimension::new));
    }

    @Override
    public void registerMessages(RegistryNamespaced<ResourceLocation, Class<? extends Message>> registry) {
        registry.putObject(new ResourceLocation("testmod", "test_message"), TestMessage.class);

    @Override
    public void registerEntityTypes() {
        ENTITY_TYPE_GRENADE = EntityType.register("testmod:grenade", EntityType.Builder.create(EntityGrenade.class, EntityGrenade::new));
    }

    @Override
    public void addEntityRenderers(Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap, RenderManager renderManager) {
        entityRenderMap.put(EntityGrenade.class, new RenderSprite<>(renderManager, GRENADE, Minecraft.getMinecraft().getRenderItem()));
    }

    @Override
    public void addEntityTrackerInfo(Map<EntityType, EntityTrackerInfo> entityTrackers) {
        entityTrackers.put(ENTITY_TYPE_GRENADE, new EntityTrackerInfo(64, 20, true));
    }
}
