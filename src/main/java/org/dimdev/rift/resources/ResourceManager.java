package org.dimdev.rift.resources;

import net.minecraft.resources.IPackFinder;
import org.dimdev.rift.listener.ResourcePackFinderAdder;

import java.util.Collections;
import java.util.List;

public class ResourceManager implements ResourcePackFinderAdder {
    @Override
    public List<IPackFinder> getResourcePackFinders() {
        return Collections.singletonList(new ModResourcePackFinder());
    }
}
