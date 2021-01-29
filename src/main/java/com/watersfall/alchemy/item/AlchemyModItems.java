package com.watersfall.alchemy.item;

import com.watersfall.alchemy.block.AlchemyModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class AlchemyModItems
{
	public static final WitchySpoonItem WITCHY_SPOON_ITEM;
	public static final Item THROW_BOTTLE;
	public static final Item LADLE_ITEM;
	public static final ApothecaryGuideItem APOTHECARY_GUIDE;
	public static final Item PEDESTAL_ITEM;

	static
	{
		WITCHY_SPOON_ITEM = new WitchySpoonItem();
		THROW_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(ItemGroup.BREWING));
		LADLE_ITEM = new LadleItem();
		APOTHECARY_GUIDE = new ApothecaryGuideItem();
		PEDESTAL_ITEM = new BlockItem(AlchemyModBlocks.PEDESTAL_BLOCK, new FabricItemSettings().group(ItemGroup.BREWING));
	}
}
