package org.dimdev.rift.resources;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import org.dimdev.riftloader.ModInfo;
import org.dimdev.riftloader.RiftLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModResourcePackFinder implements IPackFinder {
    @Override
    public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
        List<String> modResourceDomains = new ArrayList<>();
        for (ModInfo mod : RiftLoader.instance.getMods()) {
            modResourceDomains.add(mod.id);
        }
        nameToPackMap.put("rift", ResourcePackInfo.func_195793_a("rift", true, () -> new ModPack(modResourceDomains.toArray(new String[0])), packInfoFactory, ResourcePackInfo.Priority.BOTTOM));
    }
}
