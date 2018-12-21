package org.dimdev.utils;

import net.minecraft.client.Minecraft;

public class ModList {
	
	static GuiMods guiModsList;
	
	public static void displayModList() {
		if (guiModsList == null)
			guiModsList = new GuiMods();
		else
			guiModsList.getGuiModListContent().setCurrentIndex(-1);
		guiModsList.previousGui = Minecraft.getInstance().currentScreen;
		guiModsList.reloadSearch();
		Minecraft.getInstance().displayGuiScreen(guiModsList);
	}
	
}
