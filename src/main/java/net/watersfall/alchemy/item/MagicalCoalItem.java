package net.watersfall.alchemy.item;

import net.minecraft.item.Item;

public class MagicalCoalItem extends Item
{
	private final int tier;

	public MagicalCoalItem(Settings settings, int tier)
	{
		super(settings);
		this.tier = tier;
	}

	public int getTier()
	{
		return this.tier;
	}

	public int getBurnTime()
	{
		return (int)Math.pow(this.tier + 1, 2) * 160;
	}
}
