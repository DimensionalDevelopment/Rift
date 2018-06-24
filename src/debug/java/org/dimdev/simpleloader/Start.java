package org.dimdev.simpleloader;

import net.minecraft.launchwrapper.Launch;
import org.dimdev.simpleloader.launch.SimpleLoaderTweaker;

import java.io.File;

public abstract class Start {
    private static String argString;

    public static void main(String[] args) {
        argString = String.join(" ", args);

        String minecraftFolder = System.getProperty("user.home") + (System.getProperty("os.name").contains("Windows") ? "/AppData/Roaming/.minecraft" : "/.minecraft");
        File gameDir = new File(System.getProperty("user.dir"));
        File assetsDir = new File(minecraftFolder, "assets");
        addArg("--tweakClass", SimpleLoaderTweaker.class.getName());
        addArg("--version", "1.13-pre3");
        addArg("--gameDir", gameDir.getAbsolutePath());
        addArg("--assetsDir", assetsDir.getAbsolutePath());
        addArg("--assetIndex", "1.13");

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
