package org.dimdev.rift.listener;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.util.Map;

@Deprecated //TODO make this a static register() call that is to be called when adding the type!
public interface EntityTrackerAdder {

    void addEntityTrackerInfo(Map<EntityType, EntityTrackerAdder.EntityTrackerInfo> entityTrackers);

    class EntityTrackerInfo<T extends Entity> {

        private final int trackingRange;
        private final int updateFrequency;
        private final boolean sendVelocityUpdates;

        public EntityTrackerInfo(int trackingRange, int updateFrequency, boolean sendVelocityUpdates) {
            this.trackingRange = trackingRange;
            this.updateFrequency = updateFrequency;
            this.sendVelocityUpdates = sendVelocityUpdates;
        }

        public int getTrackingRange() {
            return trackingRange;
        }

        public int getUpdateFrequency() {
            return updateFrequency;
        }

        public boolean shouldSendVelocityUpdates() {
            return sendVelocityUpdates;
        }
    }
}