package org.dimdev.rift.resources;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.data.PackMetadataSection;
import org.dimdev.riftloader.ModInfo;
import org.dimdev.riftloader.RiftLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class ModPackFinder implements IPackFinder {
    private final boolean isResourcePack;

    public ModPackFinder(boolean isResourcePack) {
        this.isResourcePack = isResourcePack;
    }

    @Override
    public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
        for (ModInfo mod : RiftLoader.instance.getMods()) {
            URL root = getRootUrl(mod);

            try (ModPack pack = new ModPack(mod.name != null ? mod.name : mod.id, root)) {
                PackMetadataSection meta = pack.func_195760_a(PackMetadataSection.field_198964_a);
                if (meta != null) {
                    nameToPackMap.put(mod.id, packInfoFactory.create(mod.id, isResourcePack, () -> pack, pack, meta, ResourcePackInfo.Priority.BOTTOM));
                }
            } catch (IOException ignored) {}
        }
    }

    public URL getRootUrl(ModInfo mod) {
        File source = mod.source;
        URL root;
        try {
            if (source.isFile()) {
                root = new URL("jar:" + source.toURI().toURL() + "!/");
            } else {
                root = source.toURI().toURL();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return root;
    }
}
