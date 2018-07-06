package org.dimdev.riftloader;

import net.minecraft.launchwrapper.Launch;

import java.io.File;

public abstract class Start {
    private static String argString;

    public static void main(String[] args) {
        argString = String.join(" ", args);

        String minecraftFolder = System.getProperty("user.home") + (System.getProperty("os.name").contains("Windows") ? "/AppData/Roaming/.minecraft" : "/.minecraft");
        File gameDir = new File(System.getProperty("user.dir"));
        File assetsDir = new File(minecraftFolder, "assets");
        addArg("--tweakClass", "org.dimdev.riftloader.launch.RiftLoaderTweaker");
        addArg("--version", "1.13-pre5");
        addArg("--gameDir", gameDir.getAbsolutePath());
        addArg("--assetsDir", assetsDir.getAbsolutePath());
        addArg("--assetIndex", "1.13");
        addArg("--accessToken", "0");

        Launch.main(argString.split(" "));
    }

    private static void addArg(String name, String value) {
        if (!argString.isEmpty() && !argString.endsWith(" ")) argString += " ";
        if (value == null) {
            argString += name;
        } else {
            argString += name + " " + value;
        }
    }
}
