package net.watersfall.thuwumcraft.registry.tag;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftBlockTags
{
	public static final TagKey<Block> ESSENTIA_REFINERIES;

	static
	{
		ESSENTIA_REFINERIES = TagKey.of(Registry.BLOCK_KEY, Thuwumcraft.getId("essentia_refineries"));
	}
}
