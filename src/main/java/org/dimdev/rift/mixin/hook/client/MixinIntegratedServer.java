package org.dimdev.rift.mixin.hook.client;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import org.dimdev.rift.mixin.hook.MixinMinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(IntegratedServer.class)
public abstract class MixinIntegratedServer extends MixinMinecraftServer {
    @Shadow @Final private Minecraft mc;
    @Shadow @Final private WorldSettings worldSettings;

    @Override
    protected WorldSettings getWorldSettings(@Nullable WorldInfo worldInfo, long seed, WorldType worldType, JsonElement generatorOptions) {
        return worldSettings;
    }

    @Override
    protected EnumDifficulty getInitialDifficulty() {
        return mc.gameSettings.difficulty;
    }

    @Overwrite
    @Override
    public void loadAllWorlds(String saveName, String worldName, long seed, WorldType type, JsonElement generatorOptions) {
        super.loadAllWorlds(saveName, worldName, seed, type, generatorOptions);
    }
}
