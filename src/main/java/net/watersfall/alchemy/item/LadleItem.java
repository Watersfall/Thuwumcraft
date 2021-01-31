package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class LadleItem extends Item
{
	public LadleItem()
	{
		super(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(1));

	}

	public LadleItem(Settings settings)
	{
		super(settings);
	}
}
