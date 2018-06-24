package org.dimdev.rift.listener;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public interface CommandAdder {
    void addCommands(CommandDispatcher<CommandSource> dispatcher);
}
