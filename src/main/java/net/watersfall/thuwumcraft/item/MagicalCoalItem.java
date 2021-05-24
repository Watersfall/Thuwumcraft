package net.watersfall.thuwumcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

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

	@Override
	public Rarity getRarity(ItemStack stack)
	{
		switch(tier)
		{
			case 0:
				return Rarity.UNCOMMON;
			case 1:
				return Rarity.RARE;
			case 2:
				return Rarity.EPIC;
			default:
				return Rarity.COMMON;
		}
	}
}
