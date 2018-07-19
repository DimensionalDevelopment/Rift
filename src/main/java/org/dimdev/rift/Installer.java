package org.dimdev.rift;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Installer {
    public static void main(String... args) {
        try {
            File minecraftFolder = new File(System.getProperty("user.home") + (System.getProperty("os.name").contains("Windows") ? "/AppData/Roaming/.minecraft" : "/.minecraft"));
            File target = new File(minecraftFolder, "versions/1.13-rift-1.0.1/1.13-rift-1.0.1.json");
            target.getParentFile().mkdirs();
            Files.copy(Installer.class.getResourceAsStream("/profile.json"), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable t) {
                t.printStackTrace();
            }
            JOptionPane.showMessageDialog(null,
                    "Rift 1.0.1 for Minecraft 1.13 has been successfully installed!\n\n" +
                    "It is available in the \"Version\" dropdown menu in the \"Launch Options\"\n" +
                    "section of the launcher.", "Rift Installer", JOptionPane.INFORMATION_MESSAGE);
        } catch (Throwable t) {
            StringWriter w = new StringWriter();
            t.printStackTrace(new PrintWriter(w));
            JOptionPane.showMessageDialog(null,
                    "An error occured while installing Rift, please report this to the issue\n" +
                    "tracker (https://github.com/DimensionalDevelopment/Rift/issues):\n\n" + w.toString().replace("\t", "    "), "Rift Installer", JOptionPane.ERROR_MESSAGE);
        }
    }
}
