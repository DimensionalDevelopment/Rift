package org.dimdev.rift.listener;

import net.minecraft.item.Item;
import java.util.Map;

@ListenerInterface
public interface BurnTimeProvider {
    void registerBurnTimes(Map<Item, Integer> burnTimeMap);
}
