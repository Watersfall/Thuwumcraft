package net.watersfall.thuwumcraft.world.structure;

import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftStructurePieceTypes
{
	public static final StructurePieceType UNKNOWN_PILLAR = UnknownPillarGenerator.Piece::of;

	public static void register()
	{
		Registry.register(Registry.STRUCTURE_PIECE, Thuwumcraft.getId("unknown_pillar_piece"), UNKNOWN_PILLAR);
	}
}
