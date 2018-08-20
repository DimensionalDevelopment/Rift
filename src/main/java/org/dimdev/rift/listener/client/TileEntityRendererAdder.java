package org.dimdev.rift.listener.client;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
import org.dimdev.rift.listener.ListenerInterface;

import java.util.Map;

@ListenerInterface
public interface TileEntityRendererAdder {
    void addTileEntityRenderers(Map<Class<? extends TileEntity>, TileEntityRenderer<? extends TileEntity>> renderers);
}
