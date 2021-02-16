package net.watersfall.alchemy.api.item;

import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;

public class AspectItems
{
	public static final AspectItem AIR;
	public static final AspectItem EARTH;
	public static final AspectItem FIRE;
	public static final AspectItem WATER;

	static
	{
		AIR = new AspectItem();
		EARTH = new AspectItem();
		FIRE = new AspectItem();
		WATER = new AspectItem();
	}
}
