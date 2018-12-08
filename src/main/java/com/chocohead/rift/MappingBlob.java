package com.chocohead.rift;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MappingBlob implements Serializable {
	private static final long serialVersionUID = 1807933665347803447L;

	public final Map<String, ClassMapping> mappings;
	public final Map<String, String> nameBridge;

	public MappingBlob(Map<String, ClassMapping> mappings) {
		this.mappings = Collections.unmodifiableMap(mappings);
		nameBridge = Collections.unmodifiableMap(mappings.entrySet().parallelStream().collect(Collectors.toMap(mapping -> mapping.getValue().mcpName, Entry::getKey)));
	}

	public void write(File out) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(out))) {
			oos.writeObject(this);
		} catch (IOException e) {
			throw new RuntimeException("Error serialising mapping blob", e);
		}
	}

	public static MappingBlob read(File file) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			return (MappingBlob) ois.readObject();
		} catch (IOException e) {
			throw new RuntimeException("Error deserialising mapping blob", e);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Impossible?", e);
		}
	}
}