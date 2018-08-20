package org.dimdev.rift.listener.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.dimdev.rift.listener.ListenerInterface;

import java.util.Map;

@ListenerInterface
public interface EntityRendererAdder {
    void addEntityRenderers(Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap, RenderManager renderManager);
}
