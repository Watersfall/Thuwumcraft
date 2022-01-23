package net.watersfall.thuwumcraft.registry.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.tag.Tag;
import net.minecraft.world.biome.Biome;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftBiomeTags
{
	public static final Tag<Biome> MAGICAL_FORESTS;

	static
	{
		MAGICAL_FORESTS = TagFactory.BIOME.create(Thuwumcraft.getId("magical_forests"));
	}
}
