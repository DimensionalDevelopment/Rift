package org.dimdev.rift.listener;

import net.minecraft.resources.IPackFinder;

import java.util.List;

@ListenerInterface
public interface DataPackFinderAdder {
    List<IPackFinder> getDataPackFinders();
}
