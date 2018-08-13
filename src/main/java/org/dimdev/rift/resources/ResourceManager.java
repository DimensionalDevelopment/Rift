package org.dimdev.rift.resources;

import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.ResourcePackType;
import org.dimdev.rift.listener.DataPackFinderAdder;
import org.dimdev.rift.listener.ResourcePackFinderAdder;

import java.util.Collections;
import java.util.List;

public class ResourceManager implements ResourcePackFinderAdder, DataPackFinderAdder {
    @Override
    public List<IPackFinder> getResourcePackFinders() {
        return Collections.singletonList(new ModPackFinder(ResourcePackType.CLIENT_RESOURCES));
    }

    @Override public List<IPackFinder> getDataPackFinders() {
        return Collections.singletonList(new ModPackFinder(ResourcePackType.SERVER_DATA));
    }
}
