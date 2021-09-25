package net.watersfall.thuwumcraft.api.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.wet.api.abilities.Ability;

public interface BerserkerWeapon extends Ability<ItemStack>
{
	public static final Identifier ID = new Identifier("thuwumcraft", "berserker_weapon");

	int getExperience();

	void setExperience(int xp);

	default int addExperience(int xp)
	{
		setExperience(xp + getExperience());
		return getExperience();
	}

	default int getMaxLevel()
	{
		return 3;
	}

	default int getMinLevel()
	{
		return 0;
	}

	default int getBaseCost()
	{
		return 200;
	}

	default int getCostModifier()
	{
		return 2;
	}

	default int getLevelCost(int level)
	{
		return (level) * getCostModifier() * getBaseCost() + getBaseCost();
	}

	default int getCurrentLevel()
	{
		for(int i = getMaxLevel(); i >= getMinLevel(); i--)
		{
			if(getExperience() > getLevelCost(i))
			{
				return i;
			}
		}
		return -1;
	}
}
