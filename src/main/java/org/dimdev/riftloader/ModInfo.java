package org.dimdev.riftloader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModInfo {
    public File source;

    public String id;
    public String name;
    public String version;
    public Map<String, String> dependencies = new HashMap<>();
    public List<String> authors = new ArrayList<>();
    public List<String> listeners = new ArrayList<>();
}
