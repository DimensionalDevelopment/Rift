package org.dimdev.rift.network.message;

import io.netty.buffer.Unpooled;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import org.dimdev.rift.network.ClientMessageContext;
import org.dimdev.rift.entity.IEntityExtraSpawnData;
import org.dimdev.rift.network.Message;
import org.dimdev.riftloader.RiftLoader;
import org.dimdev.utils.ReflectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MessageSpawnEntity extends Message {

    private Entity entity;
    private int entityID;
    private UUID entityUUID;
    private EntityType entityType;
    private double x, y, z;
    private float yaw, pitch, yawHead;
    private double velocityX, velocityY, velocityZ;
    private List<EntityDataManager.DataEntry<?>> dataEntries;
    private int throwerID;
    private PacketBuffer packetBuffer;

    @SuppressWarnings("WeakerAccess")
    public MessageSpawnEntity() {
        //NO-OP
    }

    public MessageSpawnEntity(Entity entity) {
        this();
        this.entity = entity;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(entity.getEntityId());
        buffer.writeUniqueId(entity.getUniqueID());
        buffer.writeResourceLocation(EntityType.getId(entity.getType()));
        buffer.writeDouble(entity.posX);
        buffer.writeDouble(entity.posY);
        buffer.writeDouble(entity.posZ);
        buffer.writeByte((byte)(entity.rotationYaw * 256.0F / 360.0F));
        buffer.writeByte((byte) (entity.rotationPitch * 256.0F / 360.0F));
        if (entity instanceof EntityLivingBase) buffer.writeByte((byte) (((EntityLivingBase)entity).rotationYawHead * 256.0F / 360.0F));
        else buffer.writeByte(0);

        if(entity instanceof EntityThrowable) {
            EntityLivingBase thrower = ((EntityThrowable) entity).getThrower();
            buffer.writeVarInt(thrower != null ? thrower.getEntityId() : entity.getEntityId());
            double maxVelocity = 3.9D;
            buffer.writeInt((int) (MathHelper.clamp(entity.motionX, -maxVelocity, maxVelocity) * 8000.0D));
            buffer.writeInt((int) (MathHelper.clamp(entity.motionY, -maxVelocity, maxVelocity) * 8000.0D));
            buffer.writeInt((int) (MathHelper.clamp(entity.motionZ, -maxVelocity, maxVelocity) * 8000.0D));
        }
        else buffer.writeVarInt(0);

        //write DataWatcher
        {
            PacketBuffer tmp = new PacketBuffer(Unpooled.buffer());
            try {
                entity.getDataManager().writeEntries(tmp);
            } catch(Exception e) {
                RiftLoader.getLogger().fatal("unable to write entity spawn data watchers", e);
                throw new RuntimeException(e);
            }
            buffer.writeBytes(tmp);
        }

        //write extra data
        if(entity instanceof IEntityExtraSpawnData) {
            PacketBuffer tmp = new PacketBuffer(Unpooled.buffer());
            try {
                ((IEntityExtraSpawnData) entity).write(tmp);
            }
            catch(Exception e) {
                RiftLoader.getLogger().fatal("unable to write entity additional spawn data", e);
                throw new RuntimeException(e);
            }
            buffer.writeBytes(tmp);
        }
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.entityID = buffer.readVarInt();
        this.entityUUID = buffer.readUniqueId();
        this.entityType = EntityType.REGISTRY.get(buffer.readResourceLocation());
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        yaw = buffer.readByte() * 360F / 256F;
        pitch = buffer.readByte() * 360F / 256F;
        yawHead = buffer.readByte() * 360F / 256F;

        throwerID = buffer.readVarInt();
        if(throwerID != 0) {
            velocityX = buffer.readInt() / 8000.0D;
            velocityY = buffer.readInt() / 8000.0D;
            velocityZ = buffer.readInt() / 8000.0D;
        }

        try {
            dataEntries = EntityDataManager.readEntries(buffer);
        } catch(IOException e) {
            RiftLoader.getLogger().fatal("unable to read entity spawn data watchers", e);
            throw new RuntimeException(e);
        }
        packetBuffer = new PacketBuffer(buffer.retain());
    }

    @Override
    public void process(ClientMessageContext context) {
        WorldClient world = context.getClient().world;
        entity = entityType.create(world);
        if(entity != null) {
            entity.setPosition(x, y, z);
            EntityTracker.updateServerPosition(entity, x, y, z);
            Entity[] parts = entity.getParts();
            if(parts != null) {
                for(Entity part : parts) {
                    part.setEntityId(entityID);
                }
            }
            entity.setEntityId(entityID);
            entity.setUniqueId(entityUUID);
            world.addEntityToWorld(entityID, entity);
            entity.rotationYaw = yaw;
            entity.rotationPitch = pitch;
            entity.setRotationYawHead(yawHead);
            if(entity instanceof EntityThrowable) {
                Entity thrower = world.getEntityByID(throwerID);
                if(thrower instanceof EntityLivingBase) ReflectionUtils.setPrivateValue(EntityThrowable.class, (EntityThrowable) entity, thrower, "c", "field_70192_c", "thrower");
                entity.motionX = velocityX;
                entity.motionY = velocityY;
                entity.motionZ = velocityZ;
            }
            entity.getDataManager().setEntryValues(dataEntries);
            if(entity instanceof IEntityExtraSpawnData) {
                try {
                    ((IEntityExtraSpawnData) entity).read(packetBuffer);
                } catch(Exception e) {
                    RiftLoader.getLogger().fatal("unable to read entity additional spawn data", e);
                    throw new RuntimeException(e);
                }
            }
        }
        else RiftLoader.getLogger().error("discarding spawn packet for null entity!");
    }
}
