package org.dimdev.riftloader;

import com.google.gson.JsonParseException;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.accesstransform.AccessTransformationSet;
import org.dimdev.accesstransform.AccessTransformer;
import org.dimdev.riftloader.listener.InitializationListener;
import org.dimdev.riftloader.listener.Instantiator;
import org.dimdev.riftloader.swing.GuiModLoaderProgress;
import org.dimdev.utils.InstanceListMap;
import org.dimdev.utils.InstanceMap;
import org.dimdev.utils.ReflectionUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

public class RiftLoader {
    public static final RiftLoader instance = new RiftLoader();
    private static final Logger log = LogManager.getLogger("RiftLoader");

    public final File modsDir = new File(Launch.minecraftHome, "mods");
    public final File configDir = new File(Launch.minecraftHome, "config");
    private Side side;
    private boolean loaded;

    public AccessTransformer accessTransformer;
    private Map<String, ModInfo> modInfoMap = new LinkedHashMap<>();
    private List<Class<?>> listenerClasses = new ArrayList<>();
    private InstanceMap listenerInstanceMap = new InstanceMap();
    private InstanceListMap listeners = new InstanceListMap();
    private InstanceListMap customListenerInstances = new InstanceListMap();

    public void load(boolean isClient) {
        if (loaded) {
            throw new IllegalStateException("Already loaded");
        }
        loaded = true;

        side = isClient ? Side.CLIENT : Side.SERVER;
        GuiModLoaderProgress gui = null;
        if (isClient) gui = new GuiModLoaderProgress(750, 250);
        findMods(modsDir, gui);
        sortMods(gui);
        initMods(gui);
        initAccessTransformer();
    }

    /**
     * Looks for Rift mods (jars containing a 'riftmod.json' at their root) in
     * the 'modsDir' directory (creating it if it doesn't exist) and loads them
     * into the 'modInfoMap'.
     **/
    private void findMods(File modsDir, @Nullable GuiModLoaderProgress gui) {
        // Load classpath mods
        log.info("Searching mods on classpath");
        try {
            if (gui != null) gui.updateProgressText("Finding classpath mods...");
            Enumeration<URL> urls = ClassLoader.getSystemResources("riftmod.json");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                InputStream in = url.openStream();

                // Convert jar utls to file urls (from JarUrlConnection.parseSpecs)
                switch (url.getProtocol()) {
                    case "jar":
                        String spec = url.getFile();

                        int separator = spec.indexOf("!/");
                        if (separator == -1) {
                            throw new MalformedURLException("no !/ found in url spec:" + spec);
                        }

                        url = new URL(spec.substring(0, separator));

                        loadModFromJson(in, new File(url.toURI()), gui);
                        break;
                    case "file":
                        loadModFromJson(in, new File(url.toURI()).getParentFile(), gui);
                        break;
                    default:
                        throw new RuntimeException("Unsupported protocol: " + url);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Load jar mods
        log.info("Searching for mods in " + modsDir);
        if (gui != null) gui.updateProgressText("Finding mods in " + modsDir);
        modsDir.mkdirs();
        File[] modDirFiles = modsDir.listFiles();
        int c = 0;
        for (File file : modDirFiles) {
            if (gui != null) gui.setProgress(c, modDirFiles.length);
            if (!file.getName().endsWith(".jar")) continue;

            try (JarFile jar = new JarFile(file)) {
                // Check if the file contains a 'riftmod.json'
                if (!file.isFile()) continue; // Inside try since there may be a SecurityException

                JarEntry entry = jar.getJarEntry("riftmod.json");
                if (entry != null) {
                    loadModFromJson(jar.getInputStream(entry), file, gui);
                    continue;
                }

                if (jar.getJarEntry("optifine/OptiFineClassTransformer.class") != null) {
                    if (gui != null) gui.updateProgressText("Loading OptiFine");
                    ModInfo mod = new ModInfo();
                    mod.source = file;
                    mod.id = "optifine";
                    mod.name = "Optifine";
                    mod.authors.add("sp614x");
                    mod.listeners.add(new ModInfo.Listener("org.dimdev.riftloader.OptifineLoader"));
                    modInfoMap.put("optifine", mod);
                    if (gui != null) gui.updateProgressText("Finding mods in " + modsDir);
                }

                log.debug("Skipping " + file + " since it does not contain riftmod.json");
            } catch (ZipException e) {
                log.error("Could not read file " + file + " as a jar file", e);
            } catch (Throwable t) {
                log.error("Exception while checking if file " + file + " is a mod", t);
            }
            c++;
        }
        log.info("Loaded " + modInfoMap.size() + " mods");
    }

    private void loadModFromJson(InputStream in, File source, @Nullable GuiModLoaderProgress gui) {
        try {
            // Parse the 'riftmod.json' and make a ModInfo
            ModInfo modInfo = ModInfo.GSON.fromJson(new InputStreamReader(in), ModInfo.class);
            if (gui != null) gui.updateProgressText("Reading " + modInfo.name + " (" + source.getName() + ")...");
            modInfo.source = source;

            // Make sure the id isn't null and there aren't any duplicates
            if (modInfo.id == null) {
                log.error("Mod file " + modInfo.source + "'s riftmod.json is missing a 'id' field");
                return;
            } else if (modInfoMap.containsKey(modInfo.id)) {
                throw new DuplicateModException(modInfo, modInfoMap.get(modInfo.id));
            }

            // Add the mod to the 'id -> mod info' map
            modInfoMap.put(modInfo.id, modInfo);
            log.info("Loaded mod '" + modInfo.id + "'");
            if (gui != null) gui.updateProgressText("Read " + modInfo.name + " (" + modInfo.id + ")...");
        } catch (JsonParseException e) {
            if (gui != null) gui.updateProgressText(source.getName() + " was an invalid mod, skipping...");
            throw new RuntimeException("Could not read riftmod.json in " + source, e);
        }
    }

    private void sortMods(@Nullable GuiModLoaderProgress gui) {
        if (gui != null) gui.updateProgressText("Sorting mods...");
        log.debug("Sorting mods");
    }

    private void initMods(@Nullable GuiModLoaderProgress gui) {
        if (gui != null) gui.updateProgressText("Initialising mods...");
        log.info("Initializing mods");
        // Load all the mod jars
        for (ModInfo modInfo : modInfoMap.values()) {
            try {
                if (gui != null) gui.updateProgressText("Initialising " + modInfo.name);
                addURLToClasspath(modInfo.source.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        // Load the listener classes
        Collection<ModInfo> modInfos = modInfoMap.values();
        int c = 0;
        for (ModInfo modInfo : modInfos) {
            if (gui != null) gui.updateProgressText("Starting listeners for " + modInfo.name);
            if (gui != null) gui.setProgress(c, modInfos.size());
            if (modInfo.listeners != null) {
                for (ModInfo.Listener listener : modInfo.listeners) {
                    if (listener.side.includes(side)) {
                        Class<?> listenerClass;
                        try {
                            listenerClass = Launch.classLoader.findClass(listener.className);
                            listenerClasses.add(listenerClass);
                        } catch (ReflectiveOperationException e) {
                            throw new RuntimeException("Failed to find listener class " + listener.className, e);
                        }
                    }
                }
            }
            c++;
        }

        for (InitializationListener listener : getListeners(InitializationListener.class)) {
            listener.onInitialization();
        }

        log.info("Done initializing mods");
        if (gui != null) gui.updateProgressText("Done! Continuting to initialise Minecraft...");
        if (gui != null) gui.setProgress(1,1);
        if (gui != null) gui.close(1500L);
    }

    private static void addURLToClasspath(URL url) {
        ReflectionUtils.addURLToClasspath(url);
        Launch.classLoader.addURL(url);
    }

    private void initAccessTransformer() {
        try {
            AccessTransformationSet transformations = new AccessTransformationSet();

            Enumeration<URL> urls = Launch.classLoader.getResources("access_transformations.at");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                try (Scanner scanner = new Scanner(url.openStream())) {
                    while (scanner.hasNextLine()) {
                        transformations.addMinimumAccessLevel(scanner.nextLine());
                    }
                }
            }

            accessTransformer = new AccessTransformer(transformations);
            Launch.classLoader.registerTransformer("org.dimdev.riftloader.RiftAccessTransformer");
        } catch (Throwable t) {
            throw new RuntimeException("Failed to initialize access transformers", t);
        }
    }

    public Collection<ModInfo> getMods() {
        return modInfoMap.values();
    }

    public Side getSide() {
        return side;
    }

    public <T> List<T> getListeners(Class<T> listenerInterface) {
        List<T> listenerInstances = listeners.get(listenerInterface);

        if (listenerInstances == null) {
            loadListeners(listenerInterface);
            listenerInstances = listeners.get(listenerInterface);
        }

        return listenerInstances;
    }

    public <T> void loadListeners(Class<T> listenerInterface) {
        List<T> listenerInstances = new ArrayList<>();
        listeners.put(listenerInterface, listenerInstances);

        for (Class<?> listenerClass : listenerClasses) {
            if (listenerInterface.isAssignableFrom(listenerClass)) {
                // Initialize the class first, in case it wants to add itself to the listenerInstanceMap
                try {
                    Class.forName(listenerClass.getName(), true, listenerClass.getClassLoader());
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(e);
                }

                // Get the instance of that class, or create a new one if it wasn't instantiated yet
                T listenerInstance = listenerInterface.cast(listenerInstanceMap.get(listenerClass));
                if (listenerInstance == null) {
                    try {
                        listenerInstance = listenerInterface.cast(newInstance(listenerClass));
                        listenerInstanceMap.castAndPut(listenerClass, listenerInstance);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to create listener instance", e);
                    }
                }

                listenerInstances.add(listenerInstance);
            }
        }

        List<T> customInstances = customListenerInstances.get(listenerInterface);
        if (customInstances != null) {
            listenerInstances.addAll(customInstances);
        }
    }

    public <T> T newInstance(Class<T> clazz) throws ReflectiveOperationException {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return clazz.cast(constructor.newInstance());
            }
        }

        // No no-args constructor found, ask mod instantiators to build an instance
        for (Instantiator instantiator : getListeners(Instantiator.class)) {
            T instance = instantiator.newInstance(clazz);
            if (instance != null) {
                return instance;
            }
        }

        throw new InstantiationException("Class has no public no-args constructor, and no instantiator handled it either");
    }

    /**
     * Register a custom instance of a class for Rift to use rather than creating
     * one by invoking its public no-args constructor.
     */
    public <T> void setInstanceForListenerClass(Class<T> listenerClass, T instance) {
        listenerInstanceMap.put(listenerClass, instance);
    }

    /**
     * Adds a listener for a particular listener interface. This is an alternative
     * to registering the interface class in riftmod.json.
     */
    public <T> void addListener(Class<T> listenerInterface, T listener) {
        List<T> customInstances = customListenerInstances.get(listenerInterface);
        if (customInstances == null) {
            customInstances = new ArrayList<>();
            customListenerInstances.put(listenerInterface, customInstances);
        }
        customInstances.add(listener);

        List<T> loadedInstances = listeners.get(listenerInterface);
        if (loadedInstances != null) {
            loadedInstances.add(listener);
        }
    }
}
