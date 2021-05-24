package net.watersfall.thuwumcraft.api.tag;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class AlchemyFluidTags
{
	public static final Tag<Fluid> DIMENSIONAL_FLUID = TagRegistry.fluid(Thuwumcraft.getId("dimensional_fluids"));
}
