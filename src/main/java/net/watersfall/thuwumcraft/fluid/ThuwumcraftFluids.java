package net.watersfall.thuwumcraft.fluid;

import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;

public class ThuwumcraftFluids
{
	public static final DimensionalFluid DIMENSIONAL_STILL;
	public static final DimensionalFluid DIMENSIONAL_FLOWING;

	static
	{
		DIMENSIONAL_STILL = new DimensionalFluid.Still();
		DIMENSIONAL_FLOWING = new DimensionalFluid.Flowing();
	}

	public static void register()
	{
		Registry.register(Registry.FLUID, Thuwumcraft.getId("dimensional_fluid"), DIMENSIONAL_STILL);
		Registry.register(Registry.FLUID, Thuwumcraft.getId("dimensional_fluid_flowing"), DIMENSIONAL_FLOWING);
	}
}
