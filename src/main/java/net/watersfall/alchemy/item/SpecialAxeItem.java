package net.watersfall.alchemy.item;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;

public class SpecialAxeItem extends AxeItem
{
	protected SpecialAxeItem()
	{
		super(AlchemyToolMaterials.MAGIC, 5.0F, -3.0F, (new Item.Settings()).group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP).fireproof());
	}
}
