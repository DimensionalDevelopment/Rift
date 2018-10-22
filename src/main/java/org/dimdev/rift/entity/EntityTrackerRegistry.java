package org.dimdev.rift.entity;

import net.minecraft.entity.EntityType;
import org.dimdev.rift.listener.EntityTrackerAdder;
import org.dimdev.riftloader.RiftLoader;

import java.util.HashMap;
import java.util.Map;

public class EntityTrackerRegistry {
    public static final Map<EntityType, EntityTrackerAdder.EntityTrackerInfo> TRACKER_INFO = new HashMap<>();

    public static void buildTrackerInfo() {
        RiftLoader.instance.getListeners(EntityTrackerAdder.class).forEach(trackerAdder -> trackerAdder.addEntityTrackerInfo(TRACKER_INFO));
    }
}
