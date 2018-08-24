package org.dimdev.rift.mixin.core;

import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.VanillaPack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(VanillaPack.class)
public class MixinVanillaPack {
    @Shadow public static Path field_199754_a;

    /**
     * @reason Give priority to resources in the Minecraft jar to avoid them
     * from being overwritten by mods.
     */
    @Overwrite
    @Nullable
    protected InputStream func_195782_c(ResourcePackType type, ResourceLocation location) {
        String pathString = type.getDirectoryName() + "/" + location.getNamespace() + "/" + location.getPath();

        if (field_199754_a != null) {
            Path path = field_199754_a.resolve(pathString);
            if (Files.exists(path)) {
                try {
                    return Files.newInputStream(path);
                } catch (IOException ignored) {}
            }
        }

        try {
            URL rootMarker = VanillaPack.class.getResource("/" + type.getDirectoryName() + "/.mcassetsroot");
            String root = rootMarker.toString().substring(0, rootMarker.toString().length() - ".mcassetsroot".length());
            String path = location.getNamespace() + "/" + location.getPath();
            return new URL(root + path).openStream();
        } catch (IOException ignored) {}

        // Realms and Optifine just add resources to the classpath. If no resources were
        // found in the Minecraft jar, fall back to looking on the classpath. Duplicates
        // will be handled by the classpath order.
        return VanillaPack.class.getResourceAsStream("/" + type.getDirectoryName() + "/" + location.getNamespace() + "/" + location.getPath());
    }
}
