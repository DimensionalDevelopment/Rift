package org.dimdev.riftloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModInfo {
    public File source;

    public String id;
    public String name;
    public String version;
    public List<Dependency> dependencies = new ArrayList<>();
    public List<String> authors = new ArrayList<>();
    public List<String> listeners = new ArrayList<>();
}
