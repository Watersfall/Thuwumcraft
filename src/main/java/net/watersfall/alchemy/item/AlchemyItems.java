package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import java.util.function.Supplier;

public class AlchemyItems
{
	public static final ItemGroup ALCHEMY_MOD_ITEM_GROUP;

	public static final WitchySpoonItem WITCHY_SPOON_ITEM;
	public static final Item THROW_BOTTLE;
	public static final Item LINGERING_BOTTLE;
	public static final Item LADLE_ITEM;
	public static final ApothecaryGuideItem APOTHECARY_GUIDE;
	public static final Item PEDESTAL_ITEM;
	public static final MagicalCoalItem MAGICAL_COAL_TIER_0;
	public static final MagicalCoalItem MAGICAL_COAL_TIER_1;
	public static final MagicalCoalItem MAGICAL_COAL_TIER_2;
	public static final Item MAGIC_DUST;
	public static final SpecialPickaxeItem SPECIAL_PICKAXE_ITEM;
	public static final SpecialAxeItem SPECIAL_AXE_ITEM;

	static
	{
		ALCHEMY_MOD_ITEM_GROUP = FabricItemGroupBuilder.build(AlchemyMod.getId("alchemy_mod_group"), displayGroupIcon());
		WITCHY_SPOON_ITEM = new WitchySpoonItem();
		THROW_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		LINGERING_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(ALCHEMY_MOD_ITEM_GROUP));
		LADLE_ITEM = new LadleItem();
		APOTHECARY_GUIDE = new ApothecaryGuideItem();
		PEDESTAL_ITEM = new BlockItem(AlchemyBlocks.PEDESTAL_BLOCK, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		MAGICAL_COAL_TIER_0 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 0);
		MAGICAL_COAL_TIER_1 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 1);
		MAGICAL_COAL_TIER_2 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 2);
		MAGIC_DUST = new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP).rarity(Rarity.UNCOMMON));
		SPECIAL_PICKAXE_ITEM = new SpecialPickaxeItem();
		SPECIAL_AXE_ITEM = new SpecialAxeItem();
	}

	public static void register()
	{
		Registry.register(Registry.ITEM, AlchemyMod.getId("pedestal"), AlchemyItems.PEDESTAL_ITEM);
		Registry.register(Registry.ITEM, AlchemyMod.getId("witchy_spoon"), AlchemyItems.WITCHY_SPOON_ITEM);
		Registry.register(Registry.ITEM, AlchemyMod.getId("ladle"), AlchemyItems.LADLE_ITEM);
		Registry.register(Registry.ITEM, AlchemyMod.getId("throw_bottle"), AlchemyItems.THROW_BOTTLE);
		Registry.register(Registry.ITEM, AlchemyMod.getId("lingering_bottle"), AlchemyItems.LINGERING_BOTTLE);
		Registry.register(Registry.ITEM, AlchemyMod.getId("apothecary_guide_book"), AlchemyItems.APOTHECARY_GUIDE);
		Registry.register(Registry.ITEM, AlchemyMod.getId("magical_coal_0"), AlchemyItems.MAGICAL_COAL_TIER_0);
		Registry.register(Registry.ITEM, AlchemyMod.getId("magical_coal_1"), AlchemyItems.MAGICAL_COAL_TIER_1);
		Registry.register(Registry.ITEM, AlchemyMod.getId("magical_coal_2"), AlchemyItems.MAGICAL_COAL_TIER_2);
		Registry.register(Registry.ITEM, AlchemyMod.getId("magic_dust"), AlchemyItems.MAGIC_DUST);
		Registry.register(Registry.ITEM, AlchemyMod.getId("magic_pickaxe"), AlchemyItems.SPECIAL_PICKAXE_ITEM);
		Registry.register(Registry.ITEM, AlchemyMod.getId("magic_axe"), AlchemyItems.SPECIAL_AXE_ITEM);
	}

	public static Supplier<ItemStack> displayGroupIcon()
	{
		return () -> new ItemStack(MAGICAL_COAL_TIER_2);
	}
}
