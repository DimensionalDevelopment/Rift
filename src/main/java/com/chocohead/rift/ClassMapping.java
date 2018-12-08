package com.chocohead.rift;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassMapping implements Serializable {
	private static final long serialVersionUID = -1724841536245456196L;

	public final Set<String> constructors = new HashSet<>();
	public final Map<String, String> methods = new HashMap<>();
	public final Map<String, String> fields = new HashMap<>();
	public final String notchName, mcpName;

	public ClassMapping(String notchName, String mcpName) {
		this.notchName = notchName;
		this.mcpName = mcpName;
	}
}