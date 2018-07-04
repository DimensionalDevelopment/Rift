package org.dimdev.rift.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.VanillaPack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;

public class ModPack extends VanillaPack {
    private static final Logger LOGGER = LogManager.getLogger();

    public ModPack(String... resourceDomains) {
        super(resourceDomains);
    }

    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String path, int maxDepth, Predicate<String> filter) {
        Set<ResourceLocation> resourceLocations = Sets.newHashSet();
        for (String domain : resourceDomains) {
            resourceLocations.addAll(getAllResourceLocations(type, domain, path, maxDepth, filter));
        }
        return resourceLocations;
    }

    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String domain, String path, int maxDepth, Predicate<String> filter) {
        Set<ResourceLocation> resourceLocations = Sets.newHashSet();
        if (field_199754_a != null) {
            try {
                resourceLocations.addAll(getAllResourceLocations(maxDepth, domain, field_199754_a.resolve(type.func_198956_a()).resolve(domain), path, filter));
            } catch (IOException ignored) {}
        }

        try {
            String assetRootMarker = domain + "/.modassetsroot";
            URL assetRootMarkerURL = VanillaPack.class.getResource("/" + type.func_198956_a() + "/" + assetRootMarker);
            if (assetRootMarkerURL == null) {
                LOGGER.debug("Couldn't find .modassetsroot for domain " + domain + ", cannot load mod resources");
                return resourceLocations;
            }

            URI assetRootMarkerURI = assetRootMarkerURL.toURI();
            if ("file".equals(assetRootMarkerURI.getScheme())) {
                URL domainRootURL = new URL(assetRootMarkerURL.toString().substring(0, assetRootMarkerURL.toString().length() - assetRootMarker.length()) + domain);
                if (domainRootURL == null) {
                    return resourceLocations;
                }

                Path domainRootPath = Paths.get(domainRootURL.toURI());
                resourceLocations.addAll(getAllResourceLocations(maxDepth, domain, domainRootPath, path, filter));
            } else if ("jar".equals(assetRootMarkerURI.getScheme())) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(assetRootMarkerURI, Collections.emptyMap())) {
                    Path domainRootPath = fileSystem.getPath("/" + type.func_198956_a() + "/" + domain);
                    resourceLocations.addAll(getAllResourceLocations(maxDepth, domain, domainRootPath, path, filter));
                }
            } else {
                LOGGER.error("Unsupported scheme " + assetRootMarkerURI + " trying to list mod resources (NYI?)");
            }
        } catch (NoSuchFileException | FileNotFoundException ignored) {
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Couldn't get a list of all mod resources", e);
        }

        return resourceLocations;
    }

    private Collection<ResourceLocation> getAllResourceLocations(int maxDepth, String domain, Path domainRootPath, String p_195781_4_, Predicate<String> filter) throws IOException {
        List<ResourceLocation> resourceLocations = Lists.newArrayList();
        Iterator<Path> pathIterator = Files.walk(domainRootPath.resolve(p_195781_4_), maxDepth, new FileVisitOption[0]).iterator();

        while (pathIterator.hasNext()) {
            Path path = pathIterator.next();
            if (!path.endsWith(".mcmeta") && Files.isRegularFile(path) && filter.test(path.getFileName().toString())) {
                resourceLocations.add(new ResourceLocation(domain, domainRootPath.relativize(path).toString().replaceAll("\\\\", "/")));
            }
        }

        return resourceLocations;
    }

    @Override
    public String getName() {
        return "Mod Resources";
    }
}
