package org.dimdev.rift.listener;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;

import java.util.Map;

public interface TileEntityRendererAdder {
    void addRenderers(Map<Class<? extends TileEntity>, TileEntityRenderer<? extends TileEntity>> renderers);
}
