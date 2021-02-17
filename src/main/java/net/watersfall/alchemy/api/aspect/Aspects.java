package net.watersfall.alchemy.api.aspect;

import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.item.AspectItems;

import java.util.HashMap;

public class Aspects
{
	public static final HashMap<Identifier, Aspect> ASPECTS = new HashMap<>();

	public static final Aspect AIR = new Aspect(AlchemyMod.getId("air"), 0xffff00, AspectItems.AIR);
	public static final Aspect EARTH = new Aspect(AlchemyMod.getId("earth"), 0x00ff00, AspectItems.EARTH);
	public static final Aspect WATER = new Aspect(AlchemyMod.getId("water"), 0x0000ff, AspectItems.WATER);
	public static final Aspect FIRE = new Aspect(AlchemyMod.getId("fire"), 0xff0000, AspectItems.FIRE);

	public static Aspect register(Identifier id, Aspect aspect)
	{
		ASPECTS.put(id, aspect);
		return aspect;
	}
}
