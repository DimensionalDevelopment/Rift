package org.dimdev.testmod;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.dimdev.utils.ReflectionUtils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.ILocationArgument;
import net.minecraft.command.arguments.LocationInput;
import net.minecraft.command.arguments.LocationPart;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.command.impl.TeleportCommand;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;

public class ChangeDimensionCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = literalArgument("changedimension")
                .then(requiredArgument("dimension", DimensionArgument.getDimension())
                              .executes(context -> {
                                  WorldServer world = context.getSource().getServer().getWorld(DimensionArgument.func_212592_a(context, "dimension"));

                                  BlockPos pos = world.getSpawnCoordinate();
                                  if (pos == null) pos = world.getSpawnPoint();

                                  return teleport(context, new LocationInput(new LocationPart(false, pos.getX()), new LocationPart(false, pos.getY()), new LocationPart(false, pos.getZ())));
                              }))
                .then(requiredArgument("dimension", DimensionArgument.getDimension())
                .then(requiredArgument("location", Vec3Argument.vec3())
                			  .executes(context -> teleport(context, Vec3Argument.getLocation(context, "location"))
                			  )));
        dispatcher.register(builder);
    }

    private static LiteralArgumentBuilder<CommandSource> literalArgument(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private static <T> RequiredArgumentBuilder<CommandSource, T> requiredArgument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    static int teleport(CommandContext<CommandSource> context, ILocationArgument location) throws CommandSyntaxException {
    	Entity entity = context.getSource().assertIsEntity();
		DimensionType type = DimensionArgument.func_212592_a(context, "dimension");

		try {	                				  
			Class<?> facing = Arrays.stream(TeleportCommand.class.getDeclaredClasses()).filter(Class::isMemberClass).findFirst().get(); //Grab TeleportCommand.Facing, only need it to pass null in
			Method m = ReflectionUtils.findMethod(TeleportCommand.class, int.class, CommandSource.class, Collection.class, WorldServer.class, ILocationArgument.class, ILocationArgument.class, facing);
			MethodHandles.lookup().unreflect(m).invokeWithArguments(context.getSource(), Collections.singleton(entity), context.getSource().getServer().getWorld(type), location, LocationInput.current(), null);
		} catch (Throwable t) {
			throw new RuntimeException("Error teleporting " + entity + " to " + type, t);
		}

    	return 1;
    }
}
