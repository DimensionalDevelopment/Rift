package org.dimdev.riftloader;

import net.minecraft.launchwrapper.Launch;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class Start {
    private static List<String> argList = new ArrayList<>();

    public static void main(String[] args) {
        Collections.addAll(argList, args);

        String minecraftFolder;
        String userHome = System.getProperty("user.home");
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (osName.contains("win")) {
            minecraftFolder = userHome + "/AppData/Roaming/.minecraft";
        } else if (osName.contains("mac")) {
            minecraftFolder = userHome + "/Library/Application Support/minecraft";
        } else {
            minecraftFolder = userHome + "/.minecraft";
        }
        File gameDir = new File(System.getProperty("user.dir"));
        File assetsDir = new File(minecraftFolder, "assets");
        addArg("--tweakClass", "org.dimdev.riftloader.launch.RiftLoaderTweaker");
        addArg("--version", "1.13");
        addArg("--gameDir", gameDir.getAbsolutePath());
        addArg("--assetsDir", assetsDir.getAbsolutePath());
        addArg("--assetIndex", "1.13");
        addArg("--accessToken", "0");

        Launch.main(argList.toArray(new String[0]));
    }

    private static void addArg(String name, String value) {
        if (value == null) {
            argList.add(name);
        } else {
            argList.add(name);
            argList.add(value);
        }
    }
}
