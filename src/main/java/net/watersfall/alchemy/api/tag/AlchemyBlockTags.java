package net.watersfall.alchemy.api.tag;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyBlockTags
{
	public static final Tag<Block> ESSENTIA_REFINERIES;

	static
	{
		ESSENTIA_REFINERIES = TagRegistry.block(AlchemyMod.getId("essentia_refineries"));
	}
}
