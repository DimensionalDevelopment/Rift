package org.dimdev.rift.mixin.core.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.dimdev.utils.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinMainMenu extends GuiScreen {
	
	@Shadow
	private void switchToRealms() {}
	
	@ModifyArg(
			method = "addSingleplayerMultiplayerButtons",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/GuiMainMenu;addButton(Lnet/minecraft/client/gui/GuiButton;)Lnet/minecraft/client/gui/GuiButton;",
					ordinal = 2
			)
	)
	private GuiButton getRealmsButton(GuiButton original) {
		GuiButton button = new GuiButton(original.id, width / 2 + 2, original.y, 98, 20, I18n.format("menu.online")) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				switchToRealms();
			}
		};
		return button;
	}
	
	@Inject(method = "addSingleplayerMultiplayerButtons", at = @At("RETURN"))
	private void onAddSingleplayerMultiplayerButtons(int y, int dy, CallbackInfo ci) {
		GuiButton button = new GuiButton(100, width / 2 - 100, y + dy * 2, 98, 20, I18n.format("riftmodlist.mods")) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				ModList.displayModList();
			}
		};
		addButton(button);
	}
	
}
