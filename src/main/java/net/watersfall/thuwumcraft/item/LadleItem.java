package net.watersfall.thuwumcraft.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

public class LadleItem extends Item
{
	public LadleItem()
	{
		super(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP).maxCount(1));

	}

	public LadleItem(Settings settings)
	{
		super(settings);
	}
}
