package org.dimdev.testmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;

public class ChangeDimensionCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = literalArgument("changedimension")
                .then(requiredArgument("dimension", IntegerArgumentType.integer(0))
                              .executes(context -> {
                                  Entity entity = context.getSource().getEntity();
                                  entity.changeDimension(context.getArgument("dimension", Integer.class));
                                  return 0;
                              }));
        dispatcher.register(builder);
    }

    private static LiteralArgumentBuilder<CommandSource> literalArgument(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private static <T> RequiredArgumentBuilder<CommandSource, T> requiredArgument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
