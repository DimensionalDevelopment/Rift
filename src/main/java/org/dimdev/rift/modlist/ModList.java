package org.dimdev.rift.modlist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModList {
    
    static GuiMods guiModsList;
    private static Map<String, ResourceLocation> modIconMap = new HashMap<>();
    private static final ResourceLocation packIcon = new ResourceLocation("textures/misc/unknown_pack.png");
    
    public static void displayModList() {
        if (guiModsList == null)
            guiModsList = new GuiMods();
        else
            guiModsList.getGuiModListContent().setCurrentIndex(-1);
        guiModsList.previousGui = Minecraft.getInstance().currentScreen;
        guiModsList.reloadSearch();
        Minecraft.getInstance().displayGuiScreen(guiModsList);
    }
    
    public static ResourceLocation getModIcon(String modid, File source) {
        if (modIconMap.containsKey(modid))
            return modIconMap.get(modid);
        if (!source.isFile()) return packIcon;
        try (JarFile jar = new JarFile(source)) {
            JarEntry entry = jar.getJarEntry("pack.png");
            if (entry != null) {
                InputStream inputStream = jar.getInputStream(entry);
                NativeImage nativeImage = NativeImage.read(inputStream);
                return Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("modpackicon", new DynamicTexture(nativeImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packIcon;
    }
    
}
