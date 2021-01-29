package com.watersfall.alchemy.item;

import com.watersfall.alchemy.block.AlchemyModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class AlchemyModItems
{
	public static final WitchySpoonItem WITCHY_SPOON_ITEM = new WitchySpoonItem();
	public static final Item THROW_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(ItemGroup.BREWING));
	public static final Item LADLE_ITEM = new LadleItem();
	public static final ApothecaryGuideItem APOTHECARY_GUIDE = new ApothecaryGuideItem();
	public static final Item PEDESTAL_ITEM = new BlockItem(AlchemyModBlocks.PEDESTAL_BLOCK, new FabricItemSettings().group(ItemGroup.BREWING));
}
