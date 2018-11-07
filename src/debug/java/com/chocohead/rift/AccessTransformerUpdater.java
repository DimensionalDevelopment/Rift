package com.chocohead.rift;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class AccessTransformerUpdater {
	public static class ClassMapping {
		public final Set<String> constructors = new HashSet<>();
		public final Map<String, String> methods = new HashMap<>();
		public final Map<String, String> fields = new HashMap<>();
		public final String notchName, mcpName;

		public ClassMapping(String notchName, String mcpName) {
			this.notchName = notchName;
			this.mcpName = mcpName;
		}
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		Future<Map<String, ClassMapping>> srgTask = executor.submit(() -> {
			Map<String, String> files = download("http://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_config/1.13.1-20180929.143449/mcp_config-1.13.1-20180929.143449.zip", Sets.newHashSet("config/joined.tsrg", "config/constructors.txt"));
			try (BufferedReader contents = new BufferedReader(new StringReader(files.get("config/joined.tsrg"))); BufferedReader constructors = new BufferedReader(new StringReader(files.get("config/constructors.txt")))) {
				Map<String, ClassMapping> classes = new HashMap<>();

				ClassMapping current = null;
				for (String line = contents.readLine(); line != null; line = contents.readLine()) {
					if (line.charAt(0) != '\t') {
						String[] parts = line.split(" ");
						if (parts.length != 2)
							throw new IllegalStateException("Unexpected line split: " + Arrays.toString(parts) + " from " + line);

						classes.put(parts[1], current = new ClassMapping(parts[0], parts[1]));
					} else {
						String[] parts = line.substring(1).split(" ");
						switch (parts.length) {
							case 2: //Field
								current.fields.put(parts[1], parts[0]);
								break;

							case 3: //Method
								current.methods.put(parts[2], parts[0] + ' ' + parts[1]);
								break;

							default:
								throw new IllegalStateException("Unexpected line split: " + Arrays.toString(parts) + " from " + line);
						}
					}
				}

				Pattern classFinder = Pattern.compile("L([^;]+);");
				for (String line = constructors.readLine(); line != null; line = constructors.readLine()) {
					if (line.startsWith("#")) continue;
	                String[] parts = line.split(" ");

	                if (parts.length != 3)
	                	throw new IllegalStateException("Unexpected line length: " + Arrays.toString(parts) + " from " + line);

	                current = classes.get(parts[1]);
	                if (current == null) {
	                	//Anonymous classes will often not have mappings, ATing them would be pointless as they're inaccessible anyway
	                	//System.err.println("Unable to find " + parts[1] + " for constructor");
	                	continue;
	                }

					String desc = parts[2];
					StringBuffer buf = new StringBuffer("<init> ");

			        Matcher matcher = classFinder.matcher(desc);
			        while (matcher.find()) {
			        	ClassMapping type = classes.get(matcher.group(1));
			            matcher.appendReplacement(buf, Matcher.quoteReplacement('L' + (type == null ? matcher.group(1) : type.notchName) + ';'));
			        }
			        matcher.appendTail(buf);

			        current.constructors.add(buf.toString());
				}

				return classes;
			} catch (IOException e) {
				throw new RuntimeException("Error processing SRG mappings", e);
			}
		});
		Future<Map<String, String>> mcpTask = executor.submit(() -> {
			Map<String, String> files = download(String.format("http://export.mcpbot.bspk.rs/mcp_snapshot_nodoc/%1$s/mcp_snapshot_nodoc-%1$s.zip", "20181106-1.13.1"), Sets.newHashSet("methods.csv", "fields.csv"));
			try (BufferedReader methodList = new BufferedReader(new StringReader(files.get("methods.csv"))); BufferedReader fieldList = new BufferedReader(new StringReader(files.get("fields.csv")))) {
				Map<String, String> mappings = new HashMap<>();

				readMcpMappings(methodList, mappings);
				readMcpMappings(fieldList, mappings);

				return mappings;
			} catch (IOException e) {
				throw new RuntimeException("Error unjoining SRG names", e);
			}
		});

		Map<String, ClassMapping> srg = srgTask.get();
		Map<String, String> mcp = mcpTask.get();
		executor.shutdown();

		List<String> transforms = new ArrayList<>();
		Set<String> seenTransforms = new HashSet<>();
		for (String line : new String[] {
			"public method bcr a (Ljava/lang/String;Lbcr;)V # net.minecraft.block.Block/register\r\n",
			"public method bcr a (Lpc;Lbcr;)V # net.minecraft.block.Block/register\r\n",
			"public method bcr$c a (Lbhq;)Lbcr$c; # net.minecraft.block.Block$Properties/sound\r\n",
			"public method bcr$c a (I)Lbcr$c; # net.minecraft.block.Block$Properties/lightValue\r\n",
			"public method bcr$c b ()Lbcr$c; # net.minecraft.block.Block$Properties/zeroHardnessAndResistance\r\n",
			"public method bcr$c c ()Lbcr$c; # net.minecraft.block.Block$Properties/needsRandomTick\r\n",
			"public method bcr$c d ()Lbcr$c; # net.minecraft.block.Block$Properties/variableOpacity\r\n",
			"\r\n",
			"public method asz a (Lari;)V # net.minecraft.item.Item/register\r\n",
			"public method asz a (Lbcr;Larx;)V # net.minecraft.item.Item/register\r\n",
			"public method asz a (Lbcr;Lasw;)V # net.minecraft.item.Item/register\r\n",
			"public method asz a (Ljava/lang/String;Lasz;)V # net.minecraft.item.Item/register\r\n",
			"public method asz a (Lpc;Lasz;)V # net.minecraft.item.Item/register\r\n",
			"public method asz b (Lbcj;)V # net.minecraft.item.Item/register\r\n",
			"\r\n",
			"public method bym a (Ljava/lang/String;Lbym;)V # net.minecraft.fluid.Fluid/register\r\n",
			"public method bym a (Lpc;Lbym;)V # net.minecraft.fluid.Fluid/register\r\n",
			"public method bfl <init> (Lbyl;Lbcj$c;)V # net.minecraft.block.BlockFlowingFluid/<init>",
			"\r\n",
			"public method ayt a (ILjava/lang/String;Layt;)V # net.minecraft.world.biome.Biome/register",
			"\r\n",
			"public method fl <init> (Lpc;ZLfk$a;)V # net.minecraft.particles.ParticleType/<init>",
			"public method fl a (Ljava/lang/String;Z)V # net.minecraft.particles.ParticleType/register",
			"public method fl a (Ljava/lang/String;ZLfk$a;)V # net.minecraft.particles.ParticleType/register",
			"public class fn # net.minecraft.particles.BasicParticleType",
			"public method fn <init> (Lpc;Z)V # net.minecraft.particles.BasicParticleType/<init>",
			"\r\n",
			"public method awa a (Ljava/lang/String;Lawa;)V # net.minecraft.enchantment.Enchantment/func_210770_a",
			"public method aeg a (ILjava/lang/String;Laeg;)V # net.minecraft.potion.Potion/func_210759_a",
			"\r\n",
			"public method wh a (Ljava/lang/String;)V # net.minecraft.util.SoundEvent/register",
			"\r\n",
			"public method axz <init> (ILjava/lang/String;)V # net.minecraft.world.WorldType/<init>",
			"public method axz <init> (ILjava/lang/String;I)V # net.minecraft.world.WorldType/<init>",
			"public method axz <init> (ILjava/lang/String;Ljava/lang/String;I)V # net.minecraft.world.WorldType/<init>",
			"public class bmz # net.minecraft.world.gen.IChunkGeneratorFactory",
			"public class bnc # net.minecraft.world.chunk.ChunkStatus",
			"public method bwp b (Ljava/lang/Class;Ljava/lang/String;)V registerStructure # net.minecraft.world.gen.feature.structure.StructureIO/registerStructure",
			"public method bwp a (Ljava/lang/Class;Ljava/lang/String;)V # net.minecraft.world.gen.feature.structure.StructureIO/registerStructure"
		}) {
			line = line.trim();
			if (line.isEmpty()) {
				transforms.add("");
				continue;
			}

			String name = line.substring(line.lastIndexOf(' ') + 1);
			int split = name.lastIndexOf('/');
			if (split <= 0) {
				transforms.add("#??? " + line);
				System.err.println("Bad line: " + line);
				continue;
			}
			String targetClass = name.substring(0, split++).replace('.', '/');
			String targetMethod = name.substring(split);

			ClassMapping mappings = srg.get(targetClass);
			if (mappings == null) {
				transforms.add("#??? " + line);
				System.err.println("Unable to find mapping for " + targetClass + " (from " + line + ')');
				continue;
			}

			String[] result;
			if ("<init>".equals(targetMethod)) {
				//We're looking for constructors (which are inherently ambiguous without parameters)
				switch (mappings.constructors.size()) {
				case 0:
					System.err.println("Unable to find any constructors for " + targetClass);
					continue;

				case 1: {
					System.out.println("Found constructor for " + targetClass);
					String match = Iterables.getOnlyElement(mappings.constructors);
					result = new String[] {match};
				}

				default:
					System.out.println("Fuzzing " + targetClass + " constructors, found " + mappings.constructors.size());
					result = mappings.constructors.toArray(new String[0]);
				}
			} else {
				//Find all the possible SRG -> MCP mappings for the target class
				Set<Entry<String, String>> options = mcp.entrySet().stream().filter(entry -> mappings.methods.containsKey(entry.getKey())).collect(Collectors.toSet());

				Set<String> matches;
				if (!targetMethod.startsWith("func_")) {
					//Find all the matching MCP names for the target
					matches = options.stream().filter(entry -> entry.getValue().equals(targetMethod)).map(Entry::getKey).collect(Collectors.toSet());
				} else {
					//Looks like there isn't an MCP name for the target, search by SRG instead
					matches = options.stream().map(Entry::getKey).filter(entry -> entry.equals(targetMethod)).collect(Collectors.toSet());
				}

				switch (matches.size()) {
				case 0:
					System.err.println("Unable to find any matching method names for " + targetMethod + " (did find " + options + ')');
					continue;

				case 1: {
					System.out.println("Found mapping for " + targetMethod);
					String match = Iterables.getOnlyElement(matches);
					result = new String[] {mappings.methods.get(match)};
					break;
				}

				default:
					System.out.println("Fuzzing " + targetMethod + ", found " + matches.size() + " matches");
					result = matches.stream().map(mappings.methods::get).toArray(String[]::new);
				}
			}

			//System.out.println(Arrays.toString(result));
			List<String> written = new ArrayList<>();

			for (String transform : result) {
				transform = mappings.notchName + ' ' + transform;

				//Fuzzed mappings can result in already transforming some methods
				if (!seenTransforms.contains(transform)) {
					seenTransforms.add(transform);
					transforms.add(transform + " # " + line);

					written.add(transform);
				}
			}

			System.out.println(written);
		}

		System.out.println();
		System.out.println();
		transforms.forEach(System.out::println);
	}

	static void readMcpMappings(BufferedReader contents, Map<String, String> mappings) throws IOException {
		contents.readLine(); //Skip the header line

		for (String line = contents.readLine(); line != null; line = contents.readLine()) {
			int first = line.indexOf(',');

			String srg = line.substring(0, first++);
			String mcp = line.substring(first, line.indexOf(',', first));

			mappings.put(srg, mcp);
		}
	}

	private static Map<String, String> download(String url, Set<String> files) {
		HttpURLConnection connection = null;
		try {
			URL target = new URL(url);
			connection = (HttpURLConnection) target.openConnection();

			try (InputStream in = connection.getInputStream(); ZipInputStream zip = new ZipInputStream(in)) {
				Map<String, String> out = new HashMap<>();

				for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
					//System.out.println(entry.getName());

		            if (!entry.isDirectory()) {
		            	if (files.remove(entry.getName())) {
							byte[] content = new byte[4096];
							ByteArrayOutputStream bytes = new ByteArrayOutputStream();

							int read;
							while ((read = zip.read(content)) != -1) {
								bytes.write(content, 0, read);
							}

							out.put(entry.getName(), new String(bytes.toByteArray(), Charsets.UTF_8));
							if (files.isEmpty()) return out;
						}	
		            }

		            zip.closeEntry();
				}
			}

			throw new RuntimeException("Unable to find targets in " + url + " (missed " + files + ')');
		} catch (ZipException e) {
			throw new SecurityException("Invalid (non?) zip response from " + url, e);
		} catch (IOException e) {
			throw new RuntimeException("Error downloading mappings from " + url, e);
		} finally {
			IOUtils.close(connection);
		}
	}
}