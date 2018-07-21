package org.dimdev.rift;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public class Installer {
    public static void main(String... args) {
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
