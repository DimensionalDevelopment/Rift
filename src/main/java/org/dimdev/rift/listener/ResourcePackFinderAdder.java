package org.dimdev.rift.listener;

import net.minecraft.resources.IPackFinder;

import java.util.List;

public interface ResourcePackFinderAdder {
    List<IPackFinder> getResourcePackFinders();
}
