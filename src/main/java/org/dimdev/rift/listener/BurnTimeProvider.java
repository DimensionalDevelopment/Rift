package org.dimdev.rift.listener;

import net.minecraft.item.Item;
import java.util.Map;

public interface BurnTimeProvider {
    void registerBurnTimes(Map<Item, Integer> burnTimeMap);
}
