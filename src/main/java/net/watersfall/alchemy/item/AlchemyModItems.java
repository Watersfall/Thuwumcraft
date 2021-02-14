package net.watersfall.alchemy.item;

import net.watersfall.alchemy.block.AlchemyModBlocks;
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
	public static final MagicalCoalItem MAGICAL_COAL_TIER_0;
	public static final MagicalCoalItem MAGICAL_COAL_TIER_1;
	public static final MagicalCoalItem MAGICAL_COAL_TIER_2;

	static
	{
		WITCHY_SPOON_ITEM = new WitchySpoonItem();
		THROW_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(ItemGroup.BREWING));
		LADLE_ITEM = new LadleItem();
		APOTHECARY_GUIDE = new ApothecaryGuideItem();
		PEDESTAL_ITEM = new BlockItem(AlchemyModBlocks.PEDESTAL_BLOCK, new FabricItemSettings().group(ItemGroup.BREWING));
		MAGICAL_COAL_TIER_0 = new MagicalCoalItem(new FabricItemSettings().group(ItemGroup.MATERIALS), 0);
		MAGICAL_COAL_TIER_1 = new MagicalCoalItem(new FabricItemSettings().group(ItemGroup.MATERIALS), 1);
		MAGICAL_COAL_TIER_2 = new MagicalCoalItem(new FabricItemSettings().group(ItemGroup.MATERIALS), 2);
	}
}
