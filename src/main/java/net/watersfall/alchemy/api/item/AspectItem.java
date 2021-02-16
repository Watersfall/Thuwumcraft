package net.watersfall.alchemy.api.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class AspectItem extends Item
{
	public AspectItem()
	{
		super(new FabricItemSettings().maxCount(1));
	}
}
