package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.block.AlchemyModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import java.util.function.Supplier;

public class AlchemyModItems
{
	public static final ItemGroup ALCHEMY_MOD_ITEM_GROUP;

	public static final WitchySpoonItem WITCHY_SPOON_ITEM;
	public static final Item THROW_BOTTLE;
	public static final Item LADLE_ITEM;
	public static final ApothecaryGuideItem APOTHECARY_GUIDE;
	public static final Item PEDESTAL_ITEM;
	public static final MagicalCoalItem MAGICAL_COAL_TIER_0;
	public static final MagicalCoalItem MAGICAL_COAL_TIER_1;
	public static final MagicalCoalItem MAGICAL_COAL_TIER_2;
	public static final Item MAGIC_DUST;

	static
	{
		ALCHEMY_MOD_ITEM_GROUP = FabricItemGroupBuilder.build(AlchemyMod.getId("alchemy_mod_group"), displayGroupIcon());
		WITCHY_SPOON_ITEM = new WitchySpoonItem();
		THROW_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(AlchemyModItems.ALCHEMY_MOD_ITEM_GROUP));
		LADLE_ITEM = new LadleItem();
		APOTHECARY_GUIDE = new ApothecaryGuideItem();
		PEDESTAL_ITEM = new BlockItem(AlchemyModBlocks.PEDESTAL_BLOCK, new FabricItemSettings().group(AlchemyModItems.ALCHEMY_MOD_ITEM_GROUP));
		MAGICAL_COAL_TIER_0 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyModItems.ALCHEMY_MOD_ITEM_GROUP), 0);
		MAGICAL_COAL_TIER_1 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyModItems.ALCHEMY_MOD_ITEM_GROUP), 1);
		MAGICAL_COAL_TIER_2 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyModItems.ALCHEMY_MOD_ITEM_GROUP), 2);
		MAGIC_DUST = new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP).rarity(Rarity.UNCOMMON));
	}

	public static Supplier<ItemStack> displayGroupIcon()
	{
		return () -> new ItemStack(MAGICAL_COAL_TIER_2);
	}
}
