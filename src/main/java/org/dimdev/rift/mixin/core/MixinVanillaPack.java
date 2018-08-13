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
     * @reason Ignore resources that are not in the minecraft jar.
     */
    @Overwrite
    @Nullable
    protected InputStream func_195782_c(ResourcePackType type, ResourceLocation location) {
        String pathString = type.func_198956_a() + "/" + location.getNamespace() + "/" + location.getPath();

        if (field_199754_a != null) {
            Path path = field_199754_a.resolve(pathString);
            if (Files.exists(path)) {
                try {
                    return Files.newInputStream(path);
                } catch (IOException ignored) {}
            }
        }

        try {
            URL rootMarker = VanillaPack.class.getResource("/" + type.func_198956_a() + "/.mcassetsroot");
            String root = rootMarker.toString().substring(0, rootMarker.toString().length() - ".mcassetsroot".length());
            String path = location.getNamespace() + "/" + location.getPath();
            return new URL(root + path).openStream();
        } catch (IOException ignored) {}

        return null;
    }
}
