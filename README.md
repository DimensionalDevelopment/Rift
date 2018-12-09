# Rift
Rift is a lightweight modding API, library, and mod loader for Minecraft 1.13. The main objective of Rift is to make a more modular and lightweight modding API for Minecraft. Find out more on [CurseForge](https://minecraft.curseforge.com/projects/rift).

This fork contains the Minecraft 1.13.1 port (in the `newerer` branch) and the Minecraft 1.13.2 port (in the `newerest` branch). For Minecraft 1.13 see [**here**](https://github.com/DimensionalDevelopment/Rift). Pull requests should go to either of those branches depending on Minecraft version, `jitpack` is for temporary publishing until everything is pulled into the main Rift repo.


## Playing on Minecraft 1.13.2
If you have any problems or find any incompatibilities using this please do report them [here](https://github.com/Chocohead/Rift/issues). Previous problems with Optifine and water/connected textures appear to have been fixed so there is nothing known to cause trouble at the moment.

### Using the Minecraft Launcher
The current version can be downloaded from [**here**](https://jitpack.io/com/github/Chocohead/Rift/jitpack-SNAPSHOT/Rift-jitpack-SNAPSHOT.jar) and installed just like the previous versions of Rift. Mods designed for 1.13 currently aren't supported but might be in future.

### Using MultiMC
*TODO*

## Modding on Minecraft 1.13.2
The fork of Forge Gradle 2 used for 1.13 has been improved for 1.13.2 so `setupDecompWorkspace` is now supported and is strongly encouraged to be used. Access transformers which would have otherwise silently failed will now explicitly crash so they can be fixed, which is helpful for updating. The locations of all found transformers can be found by running `setupDecompWorkspace`/`setupDevWorkspace` with `--info` and looking immediately after where the access transformer task is applied. Further improvements can be suggested [**here**](https://github.com/Chocohead/ForgeGradle/issues).

Any suggestions for Rift itself can be made as issues [here](https://github.com/Chocohead/Rift/issues), or pull requested directly. Feel free to join the [Discord server](https://discord.gg/f27hdrM) if you want to talk about stuff first or have any other questions.


### Setting up
For a bare bones setup which is ready to be tinkered with, see the [**Rift MDK**](https://github.com/DimensionalDevelopment/Rift-MDK/tree/1.13.2) ([download](https://github.com/DimensionalDevelopment/Rift-MDK/archive/1.13.2.zip)).

If you want to start from scratch, use this `build.gradle` in favour of the [Rift wiki's](https://github.com/DimensionalDevelopment/Rift/wiki/Making-mods-with-Rift#mod-structure), otherwise the information provided there is still accurate.
```groovy
buildscript {
	repositories {
		mavenCentral()
		maven { url 'https://www.jitpack.io' }
		maven { url 'https://files.minecraftforge.net/maven' }
	}
	dependencies {
		classpath 'com.github.Chocohead:ForgeGradle:moderniser-SNAPSHOT'
	}
}

apply plugin: 'net.minecraftforge.gradle.tweaker-client'
apply plugin: 'java'

group 'com.example'
version '1.0.0'
archivesBaseName = 'exampleMod'

sourceCompatibility = 1.8
targetCompatibility = 1.8

repositories {
	mavenCentral()
	maven { url 'https://www.dimdev.org/maven/' }
	maven { url 'https://www.jitpack.io' }
	maven { url 'http://repo.strezz.org/artifactory/list/Strezz-Central' }
}

dependencies {
	implementation 'com.github.Chocohead:Rift:jitpack-SNAPSHOT:dev'
}

minecraft {
	version = '1.13.2'
	mappings = 'snapshot_20181130'
	runDir = 'run'
	tweakClass = 'org.dimdev.riftloader.launch.RiftLoaderClientTweaker'
}
```
Remember to change the `group` and `archivesBaseName` for the mod you're making before publishing.


### Changes between Rift 1.13 and 1.13.2
Obviously the obfuscated mappings have changed so any access transformers will need to be updated. There is the beginnings of a tool provided for automatically doing this [here](https://github.com/Chocohead/Rift/blob/newerest/src/debug/java/com/chocohead/rift/AccessTransformerUpdater.java). The same tool can also be used for more easily adding access transformer entries as it can go straight from MCP name to matching Notch names.

There have also been a few API changes within Rift to account for changes made in Minecraft 1.13.1:
* The deprecated `CustomPayloadHandler` has been removed in favour of using `MessageAdder`
* `DimensionTypeAdder` no longer expects a set of `DimensionType`s but is a callback for registering them directly now `DimensionType` is not an enum. It provides a utility method `addDimensionType` for this purpose.
* `ClientTickable#tick` now provides the `Minecraft` client instance directly to match `ServerTickable#tick` providing `MinecraftServer`

There's been many MCP method name changes too but most (if not all) are obvious enough to not need listing here.
