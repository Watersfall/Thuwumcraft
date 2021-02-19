package net.watersfall.alchemy.api.aspect;

import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.item.AspectItem;

public class Aspect
{
	public static final Aspect EMPTY = new Aspect(null, -1, null);

	private final Identifier name;
	private final int color;
	private final AspectItem item;

	public Aspect(Identifier name, int color, AspectItem item)
	{
		this.name = name;
		this.color = color;
		this.item = item;
	}

	/**
	 * Gets the translation key for the aspect <br>
	 * Formatted as {@code item.mod_id.aspect_id}
	 * @return the translation key
	 */
	public String getTranslationKey()
	{
		return this.item.getTranslationKey();
	}

	/**
	 * Gets the identifier of the aspect
	 * @return the identifier
	 */
	public Identifier getId()
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

	/**
	 * Gets the Item that displays this aspect in the creative menu,
	 * tooltips, and other item menus
	 * @return this aspect's item
	 */
	public AspectItem getItem()
	{
		return this.item;
	}
}
