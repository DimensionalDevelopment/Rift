package org.dimdev.rift.resources;

import com.google.common.collect.Lists;
import net.minecraft.resources.AbstractResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;

public class ModPack extends AbstractResourcePack {
    private final String root;
    private final String name;
    private Logger LOGGER = LogManager.getLogger();

    public ModPack(String name, URL root) {
        super(null);
        this.name = name;
        this.root = root.toString();
    }

    @Override
    protected InputStream getInputStream(String path) throws IOException {
        return new URL(root + path).openStream();
    }

    @Override
    protected boolean resourceExists(String path) {
        try (InputStream ignored = getInputStream(path)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String path, int maxDepth, Predicate<String> filter) {
        Set<ResourceLocation> resourceLocations = new HashSet<>();
        for (String namespace : getResourceNamespaces(type)) {
            resourceLocations.addAll(getAllResourceLocations(type, new ResourceLocation(namespace, path), maxDepth, filter));
        }
        return resourceLocations;
    }

    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, ResourceLocation location, int maxDepth, Predicate<String> filter) {
        Set<ResourceLocation> resourceLocations = new HashSet<>();

        try {
            String path = String.format("%s/%s/%s", type.getDirectoryName(), location.getNamespace(), location.getPath());
            URI url = new URL(root + path).toURI();
            if ("file".equals(url.getScheme())) {
                resourceLocations.addAll(getAllResourceLocations(maxDepth, location, Paths.get(url), filter));
            } else if ("jar".equals(url.getScheme())) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(url, Collections.emptyMap())) {
                    resourceLocations.addAll(getAllResourceLocations(maxDepth, location, fileSystem.getPath(path), filter));
                }
            } else {
                LOGGER.error("Unsupported scheme " + url + " trying to list mod resources");
            }
        } catch (NoSuchFileException | FileNotFoundException ignored) {
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Couldn't get a list of all resources of '" + getName() + "'", e);
        }

        return resourceLocations;
    }

    private static Collection<ResourceLocation> getAllResourceLocations(int maxDepth, ResourceLocation rootLocation, Path rootPath, Predicate<String> filter) throws IOException {
        List<ResourceLocation> resourceLocations = Lists.newArrayList();
        Iterator<Path> pathIterator = Files.walk(rootPath, maxDepth).iterator();

        while (pathIterator.hasNext()) {
            Path path = pathIterator.next();
            if (!path.endsWith(".mcmeta") && Files.isRegularFile(path) && filter.test(path.getFileName().toString())) {
                resourceLocations.add(new ResourceLocation(rootLocation.getNamespace(), rootLocation.getPath() + "/" + rootPath.toAbsolutePath().relativize(path).toString().replaceAll("\\\\", "/")));
            }
        }

        return resourceLocations;
    }

    @Override
    public Set<String> getResourceNamespaces(ResourcePackType type) {
        try {
            URI uri = new URL(root + type.getDirectoryName() + "/").toURI();
            if ("file".equals(uri.getScheme())) {
                Set<String> namespaces = new HashSet<>();
                File rootFile = new File(uri);
                if (rootFile.isDirectory()) {
                    for (File file : rootFile.listFiles()) {
                        namespaces.add(file.getName());
                    }
                }
                return namespaces;
            } else if ("jar".equals(uri.getScheme())) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                     DirectoryStream<Path> directoryStream = fileSystem.provider().newDirectoryStream(fileSystem.getPath(type.getDirectoryName()), x -> true)) {
                    Set<String> namespaces = new HashSet<>();
                    for (Path p : directoryStream) {
                        String fileName = p.getFileName().toString();
                        namespaces.add(fileName.substring(0, fileName.length() - 1));
                    }
                    return namespaces;
                }
            } else {
                LOGGER.error("Unsupported scheme " + uri + " trying to list mod resource namespaces");
            }
        } catch (NoSuchFileException | FileNotFoundException | NotDirectoryException ignored) {
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Couldn't get a list of resource namespaces of '" + getName() + "'", e);
        }

        return Collections.emptySet();
    }

    @Override
    public void close() {}

    @Override
    public String getName() {
        return name;
    }
}
