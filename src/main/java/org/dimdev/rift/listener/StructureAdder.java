package org.dimdev.rift.listener;

import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Map;

public interface StructureAdder {
    /** Register structure and structure and peice names in {@link net.minecraft.world.gen.feature.structure.StructureIO}.*/
    void registerStructureNames();

    /** Add structures to a map between lowercase structure names and structures. See {@link net.minecraft.world.gen.feature.Feature}*/
    void addStructuresToMap(Map<String, Structure<?>> map);
}
