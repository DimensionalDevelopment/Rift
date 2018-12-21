package org.dimdev.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.dimdev.riftloader.ModInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GuiModsContent extends GuiSlot {
	
	private List<ModInfo> modList = new ArrayList<>();
	private GuiMods parent;
	private FontRenderer fontRenderer;
	private int currentIndex = -1;
	
	public GuiModsContent(GuiMods parent, String searchTerm) {
		super(
				Minecraft.getInstance(),
				parent.width,
				parent.height,
				60,
				parent.height - 40,
				40
		);
		this.parent = parent;
		this.modList = new ArrayList<>();
		this.fontRenderer = mc.fontRenderer;
		setShowSelectionBox(true);
		searchFilter(searchTerm);
	}
	
	@Override
	public int getListWidth() {
		return this.width - 48 * 2;
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public List<ModInfo> getModList() {
		return modList;
	}
	
	@Override
	protected int getSize() {
		return modList.size();
	}
	
	@Override
	protected boolean isSelected(int slotIndex) {
		return slotIndex == currentIndex;
	}
	
	@Override
	protected void drawBackground() {
		parent.drawDefaultBackground();
	}
	
	protected int getY(int index) {
		return top + 4 - getAmountScrolled() + index * slotHeight + headerPadding;
	}
	
	protected int getX(int index) {
		return left + width / 2 - getListWidth() / 2 + 2;
	}
	
	private final ResourceLocation packIcon = new ResourceLocation("textures/misc/unknown_pack.png");
	
	@Override
	protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		int i = this.getY(slotIndex);
		int j = this.getX(slotIndex);
		ModInfo mod = modList.get(slotIndex);
		//Minecraft.getInstance().getTextureManager().bindTexture(mod.getModIcon());
		Minecraft.getInstance().getTextureManager().bindTexture(packIcon);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.drawModalRectWithCustomSizedTexture(j, i, 0, 0, 32, 32, 32, 32);
		String modName = mod.name;
		int j1 = fontRenderer.getStringWidth(modName);
		if (j1 > 157)
			modName = fontRenderer.trimStringToWidth(modName, 157 - fontRenderer.getStringWidth("...")) + "...";
		this.fontRenderer.drawStringWithShadow(modName, (float) (j + 32 + 2), (float) (i + 1), 16777215);
		String authors = I18n.format("riftmodlist.authors");
		for (String name : mod.authors) {
			authors += " " + name;
		}
		if (mod.authors.size() == 0) authors += " " + I18n.format("riftmodlist.noone");
		List<String> list = fontRenderer.listFormattedStringToWidth(authors, 157);
		for (String l : list) {
			fontRenderer.drawStringWithShadow(l, (float) (j + 32 + 2), (float) (i + 12 + 10 * list.indexOf(l)), 8421504);
		}
		GuiButton viewButton = new GuiButton(700 + slotIndex, left + width / 2 + getListWidth() / 2 - 61, i + 6, 50, 20,
				I18n.format("riftmodlist.view")) {
		};
		viewButton.enabled = false;
		viewButton.render(mouseXIn, mouseYIn, partialTicks);
	}
	
	@Override
	protected int getContentHeight() {
		return this.getSize() * 40;
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		int x = left + width / 2 + getListWidth() / 2 - 61, y = 0, index = 0;
		while (true) {
			if (getY(index) > p_mouseClicked_3_) {
				index--;
				y = getY(index);
				break;
			}
			index++;
		}
		if (p_mouseClicked_1_ > x && p_mouseClicked_1_ < x + 50 && p_mouseClicked_3_ > y + 6 && p_mouseClicked_3_ < y + 26) {
			//Minecraft.getInstance().getSoundHandler().play(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			//Minecraft.getInstance().displayGuiScreen(new GuiModListView(modList.get(index)));
			//currentIndex = -1;
		} else if (isMouseInList(p_mouseClicked_1_, p_mouseClicked_3_)) {
			Minecraft.getInstance().getSoundHandler().play(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			currentIndex = index;
		}
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
	private boolean isMouseInList(double p_mouseClicked_1_, double p_mouseClicked_3_) {
		return p_mouseClicked_3_ > 40 && p_mouseClicked_3_ < parent.height - 40;
	}
	
	@Override
	protected int getScrollBarX() {
		return this.width - 46;
	}
	
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public void searchFilter(String searchTerm) {
		String currentId = currentIndex > 0 ? modList.get(currentIndex).id : "";
		List<ModInfo> modList = new ArrayList<>();
		if (searchTerm.replaceAll(" ", "").equals(""))
			modList = new LinkedList<>(parent.modList);
		else
			for (ModInfo mod : parent.modList) {
				List<String> list = new LinkedList<>(mod.authors);
				list.addAll(Arrays.asList(new String[]{mod.id, mod.name, mod.source.getName()}));
				if (hasMatch(searchTerm, list.toArray(new String[list.size()])))
					modList.add(mod);
			}
		this.modList = new LinkedList<>(modList);
		int i = -1;
		for (int j = 0; j < modList.size(); j++) {
			ModInfo mod = modList.get(j);
			if (mod.id.equals(currentId))
				i = j;
		}
		setCurrentIndex(i);
	}
	
	private boolean hasMatch(String searchTerm, String... items) {
		for (String i : items)
			if (i.toLowerCase().contains(searchTerm.toLowerCase()))
				return true;
		return false;
	}
	
}
