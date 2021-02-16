package net.watersfall.alchemy.api.aspect;

import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.item.AspectItem;
import net.watersfall.alchemy.api.item.AspectItems;

import java.util.HashMap;

public class Aspect
{
	private final String name;
	private final int color;
	private final AspectItem item;

	public Aspect(String name, int color, AspectItem item)
	{
		this.name = name;
		this.color = color;
		this.item = item;
	}

	/**
	 * Gets the translation key for the aspect <br>
	 * Formatted as {@code aspect.mod_id.aspect_name}
	 * @return the translation key
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Gets the 0xAARRGGBB color for this aspect
	 * @return the color
	 */
	public int getColor()
	{
		return this.color;
	}

	public AspectItem getItem()
	{
		return this.item;
	}

	public static final HashMap<Identifier, Aspect> ASPECTS = new HashMap<>();

	public static Aspect register(Identifier id, Aspect aspect)
	{
		ASPECTS.put(id, aspect);
		return aspect;
	}
}
