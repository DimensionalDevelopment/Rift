package org.dimdev.riftloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.google.common.io.ByteStreams;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import org.dimdev.utils.ReflectionUtils;

import com.chocohead.rift.ClassMapping;
import com.chocohead.rift.MappingBlob;
import com.chocohead.rift.RemappingClassAdapter;

public class OptifineDevTransformer implements IClassTransformer {
	/** Vanilla (obfuscated) jar, used by Optifine to apply its patches */
	private final ZipFile vanilla;
	/** The Optifine transformer, we wrap this so we can interfere with what it does */
	private final IClassTransformer transformer;
	/** The current mappings we apply on top of what Optifine does */
	private final MappingBlob mappings;

	public OptifineDevTransformer() {
		File vanilla = new File(System.getProperty("vanilla-jar",
				System.getProperty("user.home") + "/.gradle/caches/minecraft/net/minecraft/minecraft/1.13.2/minecraft-1.13.2.jar"));
		//First check to see if the location we've ended up at is actually valid
		if (!vanilla.exists() || !vanilla.isFile()) {
			throw new IllegalStateException("Unable to find vanilla jar at " + vanilla);
		}
		//Now try to open it so we can pull classes out
		try {
			this.vanilla = new JarFile(vanilla);
		} catch (IOException e) {
			throw new RuntimeException("Unable to open vanilla jar at " + vanilla, e);
		}

		//We need the actual transformer to save a whole lot more reflecting into Optifine
		try {
			transformer = (IClassTransformer) Launch.classLoader.findClass(OptifineLoader.OPTIFINE_TRANSFORMER).newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Error creating Optifine class transformer", e);
		}

		//Fishing for mapping versions is guess work at best, we'll rely on being given what should be right
		File mappings = new File(System.getProperty("mappings", "NONE-PROVIDED"));
		if (!mappings.exists() || !mappings.isFile()) {
			throw new IllegalStateException("Unable to find mappings at " + vanilla);
		}
		MappingBlob temp = MappingBlob.read(mappings);
		Map<String, ClassMapping> map = new HashMap<>(temp.mappings);
		//Optifine extends a couple of vanilla classes which means mappings get missed
		addOptifineExtension(map, "net/optifine/render/AabbFrame", "cea");
		addOptifineExtension(map, "net/optifine/override/ChunkCacheOF", "aye");
		addOptifineExtension(map, "net/optifine/override/PlayerControllerOF", "crf");
		addOptifineExtension(map, "net/optifine/gui/GuiScreenOF", "ckd");

		this.mappings = new MappingBlob(map);

		try {
			ReflectionUtils.addURLToClasspath(vanilla.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error putting vanilla jar on classpath", e);
		}
	}

	private static void addOptifineExtension(Map<String, ClassMapping> map, String optiClass, String notchClass) {
		ClassMapping aabbFrame = new ClassMapping(optiClass, optiClass);
		aabbFrame.methods.putAll(map.get(notchClass).methods);
		aabbFrame.fields.putAll(map.get(notchClass).fields);
		map.put(optiClass, aabbFrame);
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		String notch = mappings.nameBridge.get(name.replace('.', '/'));

		if (notch != null) {
			ZipEntry entry = vanilla.getEntry(notch + ".class");

			if (entry != null) {
				try {
					byte[] vanillaClass = ByteStreams.toByteArray(vanilla.getInputStream(entry));
					byte[] optiClass = transformer.transform(notch, name, vanillaClass);

					if (optiClass != vanillaClass) {
						//System.out.println("Transforming vanilla class " + name + " => " + notch);
						ClassNode optiNode = toDeobfClassNode(optiClass);

						ClassWriter classWriter = new ClassWriter(0);
						optiNode.accept(classWriter);
						return classWriter.toByteArray();
					}
				} catch (IOException e) {
					throw new RuntimeException("Getting Optifine version of " + name, e);
				}
			}
		}

		return basicClass;
	}

	private ClassNode toDeobfClassNode(byte[] code) {
		ClassNode transformedNode = new ClassNode();
		new ClassReader(code).accept(new RemappingClassAdapter(transformedNode, mappings), ClassReader.EXPAND_FRAMES);
		return transformedNode;
	}
}