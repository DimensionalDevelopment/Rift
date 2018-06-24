package org.dimdev.rift.resources;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.VanillaPack;
import org.dimdev.simpleloader.ModInfo;
import org.dimdev.simpleloader.SimpleLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModResourcePackFinder implements IPackFinder {
    @Override
    public <T extends ResourcePackInfo> void func_195730_a(Map<String, T> p_195730_1_, ResourcePackInfo.IFactory<T> p_195730_2_) {
        List<String> modResourceDomains = new ArrayList<>();
        for (ModInfo mod : SimpleLoader.instance.getMods()) {
            modResourceDomains.add(mod.id);
        }
        p_195730_1_.put("simpleloader", ResourcePackInfo.func_195793_a("simpleloader", true, () -> new VanillaPack(modResourceDomains.toArray(new String[0])), p_195730_2_, ResourcePackInfo.Priority.BOTTOM));
    }
}
