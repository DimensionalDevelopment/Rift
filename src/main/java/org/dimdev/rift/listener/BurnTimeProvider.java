package org.dimdev.rift.listener;

import net.minecraft.util.IItemProvider;

import java.util.Map;

public interface BurnTimeProvider {
    Map<IItemProvider, Integer> getBurnTimes();
}
