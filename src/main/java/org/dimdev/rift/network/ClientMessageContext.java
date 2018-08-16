package org.dimdev.rift.network;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.util.NBTQueryManager;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.NetworkManager;
import net.minecraft.tags.NetworkTagManager;

public class ClientMessageContext implements MessageContext {
    private final Minecraft client;
    private final GameProfile profile;
    private final NetworkManager networkManager;
    private final RecipeManager recipeManager;
    private final ClientAdvancementManager advancementManager;
    private final CommandDispatcher<ISuggestionProvider> commandDispatcher;
    private final ClientSuggestionProvider clientSuggestionProvider;
    private final NetworkTagManager networkTagManager;
    private final NBTQueryManager nbtQueryManager;

    public ClientMessageContext(
            Minecraft client,
            GameProfile profile,
            NetworkManager networkManager,
            RecipeManager recipeManager,
            ClientAdvancementManager advancementManager,
            CommandDispatcher<ISuggestionProvider> commandDispatcher,
            ClientSuggestionProvider clientSuggestionProvider,
            NetworkTagManager networkTagManager,
            NBTQueryManager nbtQueryManager
    ) {
        this.client = client;
        this.profile = profile;
        this.networkManager = networkManager;
        this.recipeManager = recipeManager;
        this.advancementManager = advancementManager;
        this.commandDispatcher = commandDispatcher;
        this.clientSuggestionProvider = clientSuggestionProvider;
        this.networkTagManager = networkTagManager;
        this.nbtQueryManager = nbtQueryManager;
    }

    public Minecraft getClient() {
        return client;
    }

    public GameProfile getProfile() {
        return profile;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public ClientAdvancementManager getAdvancementManager() {
        return advancementManager;
    }

    public CommandDispatcher<ISuggestionProvider> getCommandDispatcher() {
        return commandDispatcher;
    }

    public ClientSuggestionProvider getSuggestionProvider() {
        return clientSuggestionProvider;
    }

    public NetworkTagManager getNetworkTagManager() {
        return networkTagManager;
    }

    public NBTQueryManager getNbtQueryManager() {
        return nbtQueryManager;
    }

    @Override
    public void reply(Message message) {
        message.sendToServer();
    }
}
