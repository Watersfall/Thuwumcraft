package net.watersfall.alchemy.api.tag;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemyFluidTags
{
	public static final Tag<Fluid> DIMENSIONAL_FLUID = TagRegistry.fluid(AlchemyMod.getId("dimensional_fluids"));
}
