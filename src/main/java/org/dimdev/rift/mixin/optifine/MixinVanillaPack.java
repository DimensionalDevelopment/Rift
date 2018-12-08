package org.dimdev.rift.mixin.optifine;

import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.VanillaPack;
import net.minecraft.util.ResourceLocation;

import org.dimdev.riftloader.OptifineLoader;
import org.dimdev.utils.ReflectionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

@Mixin(VanillaPack.class)
public class MixinVanillaPack {
    @Shadow public static Path basePath;

    /**
     * @reason Give priority to resources in the Minecraft jar to avoid them
     * from being overwritten by mods.
     */
    @Nullable
    @Overwrite(constraints = "OPTIFINE(1+)")
    protected InputStream getInputStreamVanilla(ResourcePackType type, ResourceLocation location) {
        String pathString = type.getDirectoryName() + "/" + location.getNamespace() + "/" + location.getPath();

        InputStream in = optifineResourceFisher.apply(pathString);
        if (in != null) return in;

        if (basePath != null) {
            Path path = basePath.resolve(pathString);
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
        } catch (IOException ignored) {
        	return VanillaPack.class.getResourceAsStream("/" + pathString);        	
        }
    }

    private static final Function<String, InputStream> optifineResourceFisher;
    static {
    	//Ironically Optifine does all this via reflection too, so we're actually no slower than how it otherwise works
    	try {
    		//Fish for the type
    		Class<?> transformer = Class.forName(OptifineLoader.OPTIFINE_TRANSFORMER);
    		//Then the instance field Optifine helpfully hangs onto
    		Field instanceField = ReflectionUtils.findField(transformer, transformer);
    		Object instance = instanceField.get(null);
    		if (instance == null) throw new IllegalStateException("Transformer hasn't loaded yet?");
    		//Now fish for the method
    		Method method = transformer.getDeclaredMethod("getOptiFineResource", String.class);
    		MethodHandle handle = MethodHandles.lookup().unreflect(method);
    		optifineResourceFisher = path -> {
				try {
					byte[] resource = (byte[]) handle.invoke(instance, path);
					return resource != null ? new ByteArrayInputStream(resource) : null;
				} catch (Throwable t) {
					throw new RuntimeException("Error getting resource from Optifine", t);
				}
    		};
    	} catch (ReflectiveOperationException e) {
    		throw new RuntimeException("Unable to get Optifine resource method", e);
    	}
    }
}