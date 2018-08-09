package org.dimdev.riftloader;

import net.minecraft.launchwrapper.Launch;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Main {
    private static final String[] libraries = {
            "https://www.dimdev.org/maven/org/dimdev/mixin/0.7.11-SNAPSHOT/mixin-0.7.11-SNAPSHOT.jar",
            "https://repo1.maven.org/maven2/org/ow2/asm/asm/6.2/asm-6.2.jar",
            "https://repo1.maven.org/maven2/org/ow2/asm/asm-commons/6.2/asm-commons-6.2.jar",
            "https://repo1.maven.org/maven2/org/ow2/asm/asm-tree/6.2/asm-tree-6.2.jar",
            "https://libraries.minecraft.net/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar"
    };
    public static final String VANILLA_SERVER = "https://launcher.mojang.com/mc/game/1.13/server/d0caafb8438ebd206f99930cfaecfa6c9a13dca0/server.jar";
    public static final String SPIGOT_SERVER = "https://cdn.getbukkit.org/spigot/spigot-1.13.jar";

    public static void main(String... args) throws Throwable {
        if (args.length == 0 || args[0].equals("--install")) {
            runClientInstaller();
        } else if (args[0].equals("--server")) {
            File serverJar = new File("server.jar");
            if (!serverJar.isFile()) {
                System.out.println("File 'server.jar' does not exist");
                System.out.println("Choose which server you'd like to download:");
                System.out.println("  1) Vanilla");
                System.out.println("  2) Spigot");
                System.out.print("Choice: ");

                URL url;
                String line = new Scanner(System.in).nextLine().toLowerCase();
                if (line.startsWith("1") || line.startsWith("v")) {
                    url = new URL(VANILLA_SERVER);
                } else if (line.startsWith("2") || line.startsWith("s")) {
                    url = new URL(SPIGOT_SERVER);
                } else {
                    System.err.println("Not a valid choice");
                    return;
                }

                System.out.println("Downloading server jar: " + url);
                new FileOutputStream(serverJar).getChannel().transferFrom(Channels.newChannel(url.openStream()), 0, Long.MAX_VALUE);
            }

            addURLToClasspath(serverJar.toURI().toURL());

            for (String url : libraries) {
                addURLToClasspath(getOrDownload(new File("libs"), new URL(url)).toURI().toURL());
            }

            List<String> argsList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
            argsList.add("--tweakClass");
            argsList.add("org.dimdev.riftloader.launch.RiftLoaderServerTweaker");

            System.out.println("Launching server...");
            Launch.main(argsList.toArray(new String[0]));
        }
    }

    private static File getOrDownload(File directory, URL url) throws IOException {
        String urlString = url.toString();
        File target = new File(directory, urlString.substring(urlString.lastIndexOf('/') + 1));
        if (target.isFile()) {
            return target;
        }
        target.getParentFile().mkdirs();

        System.out.println("Downloading library: " + urlString);
        new FileOutputStream(target).getChannel().transferFrom(Channels.newChannel(url.openStream()), 0, Long.MAX_VALUE);

        return target;
    }

    private static void addURLToClasspath(URL url) throws Exception {
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(ClassLoader.getSystemClassLoader(), url);
    }

    public static void runClientInstaller() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            File minecraftFolder;
            String userHome = System.getProperty("user.home");
            String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
            if (osName.contains("win")) {
                minecraftFolder = new File(userHome + "/AppData/Roaming/.minecraft");
            } else if (osName.contains("mac")) {
                minecraftFolder = new File(userHome + "/Library/Application Support/minecraft");
            } else {
                minecraftFolder = new File(userHome + "/.minecraft");
            }

            File target = new File(minecraftFolder, "versions/1.13-rift-@VERSION@/1.13-rift-@VERSION@.json");
            target.getParentFile().mkdirs();
            Files.copy(Main.class.getResourceAsStream("/profile.json"), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

            try {
                String source = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                if (source.startsWith("/")) {
                    source = source.substring(1);
                }
                target = new File(minecraftFolder, "libraries/org/dimdev/rift/@VERSION@/rift-@VERSION@.jar");
                target.getParentFile().mkdirs();
                Files.copy(Paths.get(source), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            JOptionPane.showMessageDialog(null,
                    "Rift @VERSION@ for Minecraft 1.13 has been successfully installed!\n\n" +
                    "It is available in the \"Version\" dropdown menu in the \"Launch Options\"\n" +
                    "section of the launcher.", "Rift Installer", JOptionPane.INFORMATION_MESSAGE);
        } catch (Throwable t) {
            StringWriter w = new StringWriter();
            t.printStackTrace(new PrintWriter(w));
            JOptionPane.showMessageDialog(null,
                    "An error occured while installing Rift, please report this to the issue\n" +
                    "tracker (https://github.com/DimensionalDevelopment/Rift/issues):\n\n" +
                    w.toString().replace("\t", "    "), "Rift Installer", JOptionPane.ERROR_MESSAGE);
        }
    }
}
