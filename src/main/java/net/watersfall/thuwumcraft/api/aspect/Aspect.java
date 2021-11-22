package net.watersfall.thuwumcraft.api.aspect;

import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.item.AspectItem;

/**
 * TODO: Real Magic
 *
 * Aspects and Essentia are the "physical" magic contained in the world.
 * It is present in all physical things, including living creatures, and
 * is a bit like a "chemical makeup" in a mundane sense. This essentia is
 * "flavored" vis, each slightly different from the other physically, but
 * magically very distinct. More advanced forms of thaumaturgical crafting
 * are based around this essentia, and it's very distinct properties.
 */
public class Aspect
{
	public static final Aspect EMPTY = new Aspect(new Identifier("thuwumcraft:empty"), -1, null);

	private final Identifier name;
	private final int color;
	private final AspectItem item;
	private final boolean primitive;
	private final Aspect[] components;

	public Aspect(Identifier name, int color, Aspect[] components)
	{
		this.name = name;
		this.color = color;
		this.primitive = false;
		this.components = components;
		this.item = new AspectItem(this);
	}

	protected Aspect(Identifier name, int color)
	{
		this.name = name;
		this.color = color;
		this.primitive = true;
		this.components = new Aspect[]{};
		this.item = new AspectItem(this);
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

	/**
	 * @return Whether this aspect is a primitive aspect (one that is not made up of other aspects)
	 */
	public boolean isPrimitive()
	{
		return primitive;
	}

	/**
	 * @return Returns the components that make up this aspect, or an empty list if primitive
	 */
	public Aspect[] getComponents()
	{
		return this.components;
	}
}
