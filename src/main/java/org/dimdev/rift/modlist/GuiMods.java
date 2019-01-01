package org.dimdev.rift.modlist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Util;
import org.dimdev.riftloader.ModInfo;
import org.dimdev.riftloader.RiftLoader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiMods extends GuiScreen {
    
    private final String searchBoxSuggestion = I18n.format("rift.modlist.search_mods");
    public static List<ModInfo> modList = new ArrayList<>();
    
    @Nullable
    private GuiModsContent guiModListContent;
    private GuiTextField searchBox;
    public GuiScreen previousGui;
    
    public GuiMods() {
        regenerateMods();
    }
    
    private void regenerateMods() {
        modList = new ArrayList<>();
        RiftLoader.instance.getMods().forEach(modInfo -> {
            ModInfo cloned = new ModInfo();
            cloned.id = modInfo.id;
            cloned.name = modInfo.name != null ? modInfo.name : modInfo.id;
            cloned.source = modInfo.source;
            cloned.authors = modInfo.authors;
            cloned.listeners = modInfo.listeners;
            modList.add(modInfo);
        });
        Collections.sort(modList, (riftMod, anotherMod) -> {
            return riftMod.name.compareTo(anotherMod.name);
        });
    }
    
    @Override
    protected void initGui() {
        Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
        addButton(new GuiButton(501, this.width / 2 - 100, this.height - 30, 93, 20, I18n.format("rift.modlist.openFolder")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Util.getOSType().openFile(RiftLoader.instance.modsDir);
            }
        });
        this.guiModListContent = new GuiModsContent(this, "");
        
        this.searchBox = new GuiTextField(103, this.fontRenderer, this.width / 2 - 100, 32, 200, 20) {
            @Override
            public void setFocused(boolean var1) {
                super.setFocused(true);
            }
        };
        this.searchBox.setTextAcceptHandler((p_212350_1_, p_212350_2_) ->
        {
            this.guiModListContent.searchFilter(p_212350_2_);
        });
        addButton(new GuiButton(104, this.width / 2 - 3, this.height - 30, 93, 20, I18n.format("rift.modlist.done")) {
            @Override
            public void onClick(double var1, double var3) {
                close();
                Minecraft.getInstance().displayGuiScreen(previousGui);
            }
        });
        this.children.add(searchBox);
        this.children.add(guiModListContent);
        this.searchBox.setFocused(true);
        this.searchBox.setCanLoseFocus(false);
    }
    
    public void reloadSearch() {
        try {
            this.searchBox.setText("");
            this.guiModListContent.searchFilter("");
        } catch (Exception e) {
        }
    }
    
    @Override
    public void tick() {
        this.searchBox.tick();
        this.searchBox.setSuggestion(searchBox.getText().equals("") ? searchBoxSuggestion : null);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.guiModListContent.drawScreen(mouseX, mouseY, partialTicks);
        this.searchBox.drawTextField(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, I18n.format("rift.modlist.mods"), this.width / 2, 16, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
    
    @Nullable
    public GuiModsContent getGuiModListContent() {
        return guiModListContent;
    }
    
}
