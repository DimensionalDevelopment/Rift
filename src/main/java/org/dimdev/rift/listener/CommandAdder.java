package org.dimdev.rift.listener;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

@ListenerInterface
public interface CommandAdder {
    void registerCommands(CommandDispatcher<CommandSource> dispatcher);
}
