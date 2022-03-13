package net.watersfall.thuwumcraft.world.structure.pool;

import com.mojang.datafixers.util.Either;
import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.LegacySinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

/**
 * The constructor for LegacySinglePoolElement is protected
 */
public class SpecialSinglePoolElement extends LegacySinglePoolElement
{

	public SpecialSinglePoolElement(Either<Identifier, Structure> either, RegistryEntry<StructureProcessorList> registryEntry, StructurePool.Projection projection)
	{
		super(either, registryEntry, projection);
	}
}
