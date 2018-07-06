package org.dimdev.rift.mixin.hook;

import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.dimdev.rift.listener.CommandAdder;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Commands.class)
public class MixinCommands {
    // Workaround for https://github.com/SpongePowered/Mixin/issues/267
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;findAmbiguities(Lcom/mojang/brigadier/AmbiguityConsumer;)V"))
    public void findAmbiguities(CommandDispatcher<CommandSource> dispatcher, AmbiguityConsumer<CommandSource> consumer) {
        for (CommandAdder commandAdder : RiftLoader.instance.getListeners(CommandAdder.class)) {
            commandAdder.registerCommands(dispatcher);
        }
        dispatcher.findAmbiguities(consumer);
    }
}
