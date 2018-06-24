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

    public EnumActionResult func_195939_a(ItemUseContext context) {
        if (context.func_195991_k().isRemote) {
            return EnumActionResult.SUCCESS;
        }

        int data = (int) (Math.random() * 1000);
        log.info("Sending test packet to server: " + data);
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketTest(data));

        return EnumActionResult.SUCCESS;
    }
}
