package org.dimdev.rift.mixin.hook.client;

import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiMainMenu.class)
public class MixinMainMenu {

    @Overwrite
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        
    }
}
