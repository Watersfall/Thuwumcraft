package net.watersfall.thuwumcraft.registry.tag;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftFluidTags
{
	public static final TagKey<Fluid> DIMENSIONAL_FLUID = TagKey.of(Registry.FLUID_KEY, Thuwumcraft.getId("dimensional_fluids"));
}
