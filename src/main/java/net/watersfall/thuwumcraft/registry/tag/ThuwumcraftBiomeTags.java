package net.watersfall.thuwumcraft.registry.tag;

import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftBiomeTags
{
	public static final TagKey<Biome> MAGICAL_FORESTS;

	static
	{
		MAGICAL_FORESTS = TagKey.of(Registry.BIOME_KEY, Thuwumcraft.getId("magical_forests"));
	}
}
