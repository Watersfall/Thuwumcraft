package net.watersfall.alchemy.world.structure;

import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyStructurePieceTypes
{
	public static final StructurePieceType UNKNOWN_PILLAR = UnknownPillarGenerator.Piece::of;

	public static void register()
	{
		Registry.register(Registry.STRUCTURE_PIECE, AlchemyMod.getId("unknown_pillar_piece"), UNKNOWN_PILLAR);
	}
}
