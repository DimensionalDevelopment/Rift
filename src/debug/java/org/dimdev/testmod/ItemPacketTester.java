package org.dimdev.testmod;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemPacketTester extends Item {
    private static final Logger log = LogManager.getLogger();

    public ItemPacketTester(Builder builder) {
        super(builder);
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext context) {
        if (context.getWorld().isRemote) {
            return EnumActionResult.SUCCESS;
        }

        int data = (int) (Math.random() * 1000);
        log.info("Sending test packet and message to server: " + data);
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketTest(data));

        new TestMessage(data).sendToServer();

        return EnumActionResult.SUCCESS;
    }
}
