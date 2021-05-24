package net.watersfall.thuwumcraft.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class LadleItem extends Item
{
	public LadleItem()
	{
		super(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP).maxCount(1));

	}

	public LadleItem(Settings settings)
	{
		super(settings);
	}
}
