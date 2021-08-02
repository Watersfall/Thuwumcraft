package net.watersfall.thuwumcraft.item;

import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.watersfall.thuwumcraft.api.aspect.Aspect;

public class ArcaneRuneItem extends Item implements ItemColorProvider
{
	private final Aspect aspect;

	public ArcaneRuneItem(Aspect aspect, Settings settings)
	{
		super(settings);
		this.aspect = aspect;
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}

	public int getColor(ItemStack stack, int tintIndex)
	{
		if(tintIndex == 1)
		{
			return aspect.getColor();
		}
		return -1;
	}
}
