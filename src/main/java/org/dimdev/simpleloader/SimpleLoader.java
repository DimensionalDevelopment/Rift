package org.dimdev.simpleloader;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.simpleloader.listener.InitializationListener;
import org.dimdev.utils.InstanceListMap;
import org.dimdev.utils.InstanceMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

public class SimpleLoader {
    public static final SimpleLoader instance = new SimpleLoader();
    private static final Logger log = LogManager.getLogger("SimpleLoader");
    private static final Gson GSON = new Gson();

    public final File modsDir = new File(Launch.minecraftHome, "mods");
    public final File configDir = new File(Launch.minecraftHome, "config");

    public Map<String, ModInfo> modInfoMap = new HashMap<>();
    public List<Class<?>> listenerClasses = new ArrayList<>();
    private InstanceMap listenerInstanceMap = new InstanceMap();
    public InstanceListMap listeners = new InstanceListMap();

    public void load() {
        findMods(modsDir);
        sortMods();
        initMods();
    }

    /**
     * Looks for riftmods (jars containing a 'riftmod.json' at their root) in
     * the 'modsDir' directory (creating it if it doesn't exist) and loads them
     * into the 'modInfoMap'.
     **/
    public void findMods(File modsDir) {
        // Load jar mods
        log.info("Searching for mods in " + modsDir);
        modsDir.mkdirs();
        for (File file : modsDir.listFiles()) {
            if (!file.getName().endsWith(".jar")) continue;

            try (JarFile jar = new JarFile(file)) {
                // Check if the file contains a 'riftmod.json'
                if (!file.isFile()) continue; // Inside try since there may be a SecurityException
                JarEntry entry = jar.getJarEntry("riftmod.json");
                if (entry == null) {
                    log.debug("Skipping " + file + " since it does not contain riftmod.json");
                    continue;
                }
                parseJSON(jar.getInputStream(entry), file);
            } catch (ZipException e) {
                log.error("Could not read file " + file + " as a jar file", e);
            } catch (Throwable t) {
                log.error("Exception while checking if file " + file + " is a mod", t);
            }
        }

        // Load classpath mods
        log.info("Searching mods on classpath");
        try {
            Enumeration<URL> urls = ClassLoader.getSystemResources("riftmod.json");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                InputStream in = url.openStream();

                // Convert jar utls to file urls (from JarUrlConnection.parseSpecs)
                if (url.getProtocol().equals("jar")) {
                    String spec = url.getFile();

                    int separator = spec.indexOf("!/");
                    if (separator == -1) {
                        throw new MalformedURLException("no !/ found in url spec:" + spec);
                    }

                    url = new URL(spec.substring(0, separator));
                }

                parseJSON(in, new File(url.toURI()));
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        log.info("Loaded " + modInfoMap.size() + " mods");
    }

    private void parseJSON(InputStream in, File source) {
        try {
            // Parse the 'riftmod.json' and make a ModInfo
            ModInfo modInfo = GSON.fromJson(new InputStreamReader(in), ModInfo.class);
            modInfo.modSource = source;

            // Make sure the id isn't null and there aren't any duplicates
            if (modInfo.id == null) {
                log.error("Mod file " + modInfo.modSource + "'s riftmod.json is missing a 'id' field");
                return;
            } else if (modInfoMap.containsKey(modInfo.id)) {
                throw new ModConflictException("Duplicate mod '" + modInfo.id + "': " + modInfoMap.get(modInfo.id).modSource + ", " + modInfo.id);
            }

            // Add the mod to the 'id -> mod info' map
            modInfoMap.put(modInfo.id, modInfo);
            log.debug("Successfully loaded mod '" + modInfo.id + " from file " + modInfo.modSource);
        } catch (JsonParseException e) {
            throw new RuntimeException("Could not read riftmod.json in " + source, e);
        }
    }

    public void sortMods() {
        log.debug("Sorting mods"); // TODO
    }

    public void initMods() {
        log.info("Initializing mods");
        // Load all the mod jars
        for (ModInfo modInfo : modInfoMap.values()) {
            try {
                Launch.classLoader.addURL(modInfo.modSource.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        // Load the listener classes
        for (ModInfo modInfo : modInfoMap.values()) {
            if (modInfo.listeners != null) {
                for (String listenerClassName : modInfo.listeners) {
                    Class<?> listenerClass;
                    try {
                        listenerClass = Launch.classLoader.findClass(listenerClassName);
                        listenerClasses.add(listenerClass);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to find listener class " + e);
                    }
                }
            }
        }

        for (InitializationListener listener : getListeners(InitializationListener.class)) {
            listener.onInitialization();
        }

        log.info("Done initializing mods");
    }

    public void addMod(ModInfo mod) {
        modInfoMap.put(mod.id, mod);
    }

    public Collection<ModInfo> getMods() {
        return modInfoMap.values();
    }

    public <T> List<T> getListeners(Class<T> listenerInterface) {
        List<T> listenerInstances = listeners.get(listenerInterface);

        if (listenerInstances == null) { // TODO: Manually load them instead?
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
                // Get the instance of that class, or create a new one if it wasn't instantiated yet
                T listenerInstance = listenerInterface.cast(listenerInstanceMap.get(listenerClass));
                if (listenerInstance == null) {
                    try {
                        listenerInstance = listenerInterface.cast(listenerClass.newInstance());
                        listenerInstanceMap.castAndPut(listenerClass, listenerInstance);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to create listener instance", e);
                    }
                }

                listenerInstances.add(listenerInstance);
            }
        }
    }
}
