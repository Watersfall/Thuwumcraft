package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.item.AspectItem;
import net.watersfall.alchemy.api.item.AspectItems;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import java.util.ArrayList;
import java.util.List;

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
	public static final MagicDustItem MAGIC_DUST;
	public static final SpecialPickaxeItem SPECIAL_PICKAXE_ITEM;
	public static final SpecialAxeItem SPECIAL_AXE_ITEM;
	public static final Item JAR_ITEM;
	public static final GlassPhialItem EMPTY_PHIAL_ITEM;
	public static final Item PHIAL_SHELF_ITEM;
	public static final ResearchBookItem RESEARCH_BOOK_ITEM;
	public static final BlockItem ASPECT_PIPE_ITEM;
	public static final Item NECROMANCY_SKULL;
	public static final Item NECROMANCY_ARM;
	public static final Item NECROMANCY_LEG;
	public static final Item NECROMANCY_TORSO;
	public static final Item NECROMANCY_RIBCAGE;
	public static final BlockItem CUSTOM_SPAWNER;

	static
	{
		ALCHEMY_MOD_ITEM_GROUP = FabricItemGroupBuilder.create(AlchemyMod
				.getId("alchemy_mod_group"))
				.icon(AlchemyItems::getDisplayIcon)
				.appendItems(AlchemyItems::getStacks)
				.build();
		WITCHY_SPOON_ITEM = new WitchySpoonItem();
		THROW_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		LINGERING_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(ALCHEMY_MOD_ITEM_GROUP));
		LADLE_ITEM = new LadleItem();
		APOTHECARY_GUIDE = new ApothecaryGuideItem();
		PEDESTAL_ITEM = new BlockItem(AlchemyBlocks.PEDESTAL_BLOCK, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		MAGICAL_COAL_TIER_0 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 0);
		MAGICAL_COAL_TIER_1 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 1);
		MAGICAL_COAL_TIER_2 = new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 2);
		MAGIC_DUST = new MagicDustItem();
		SPECIAL_PICKAXE_ITEM = new SpecialPickaxeItem();
		SPECIAL_AXE_ITEM = new SpecialAxeItem();
		JAR_ITEM = new BlockItem(AlchemyBlocks.JAR_BLOCK, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		EMPTY_PHIAL_ITEM = new GlassPhialItem(Aspect.EMPTY);
		PHIAL_SHELF_ITEM = new BlockItem(AlchemyBlocks.PHIAL_SHELF_BLOCK, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		RESEARCH_BOOK_ITEM = new ResearchBookItem(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP));
		ASPECT_PIPE_ITEM = new BlockItem(AlchemyBlocks.ASPECT_PIPE_BLOCK, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP));
		NECROMANCY_SKULL = new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP));
		NECROMANCY_ARM = new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP));
		NECROMANCY_LEG = new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP));
		NECROMANCY_TORSO = new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP));
		NECROMANCY_RIBCAGE = new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP));
		CUSTOM_SPAWNER = new BlockItem(AlchemyBlocks.CUSTOM_SPAWNER, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP));
	}

	public static void register()
	{
		register(AlchemyMod.getId("pedestal"), AlchemyItems.PEDESTAL_ITEM);
		register(AlchemyMod.getId("witchy_spoon"), AlchemyItems.WITCHY_SPOON_ITEM);
		register(AlchemyMod.getId("ladle"), AlchemyItems.LADLE_ITEM);
		register(AlchemyMod.getId("throw_bottle"), AlchemyItems.THROW_BOTTLE);
		register(AlchemyMod.getId("lingering_bottle"), AlchemyItems.LINGERING_BOTTLE);
		register(AlchemyMod.getId("apothecary_guide_book"), AlchemyItems.APOTHECARY_GUIDE);
		register(AlchemyMod.getId("magical_coal_0"), AlchemyItems.MAGICAL_COAL_TIER_0);
		register(AlchemyMod.getId("magical_coal_1"), AlchemyItems.MAGICAL_COAL_TIER_1);
		register(AlchemyMod.getId("magical_coal_2"), AlchemyItems.MAGICAL_COAL_TIER_2);
		register(AlchemyMod.getId("magic_dust"), AlchemyItems.MAGIC_DUST);
		register(AlchemyMod.getId("magic_pickaxe"), AlchemyItems.SPECIAL_PICKAXE_ITEM);
		register(AlchemyMod.getId("magic_axe"), AlchemyItems.SPECIAL_AXE_ITEM);
		register(AlchemyMod.getId("jar"), AlchemyItems.JAR_ITEM);
		register(AlchemyMod.getId("aspect/air"), AspectItems.AIR);
		register(AlchemyMod.getId("aspect/earth"), AspectItems.EARTH);
		register(AlchemyMod.getId("aspect/fire"), AspectItems.FIRE);
		register(AlchemyMod.getId("aspect/water"), AspectItems.WATER);
		register(AlchemyMod.getId("phial/empty"), EMPTY_PHIAL_ITEM);
		register(AlchemyMod.getId("phial_shelf"), PHIAL_SHELF_ITEM);
		register(AlchemyMod.getId("research_book"), RESEARCH_BOOK_ITEM);
		register(AlchemyMod.getId("aspect_pipe"), ASPECT_PIPE_ITEM);
		register(AlchemyMod.getId("necromancy_skull"), NECROMANCY_SKULL);
		register(AlchemyMod.getId("necromancy_arm"), NECROMANCY_ARM);
		register(AlchemyMod.getId("necromancy_leg"), NECROMANCY_LEG);
		register(AlchemyMod.getId("necromancy_torso"), NECROMANCY_TORSO);
		register(AlchemyMod.getId("necromancy_ribcage"), NECROMANCY_RIBCAGE);
		register(AlchemyMod.getId("custom_spawner"), CUSTOM_SPAWNER);
	}

	private static final List<Item> items = new ArrayList<>();

	private static void register(Identifier id, Item item)
	{
		Registry.register(Registry.ITEM, id, item);
		items.add(item);
	}

	private static void getStacks(List<ItemStack> stack)
	{
		items.forEach((item) -> {
			if(item instanceof BlockItem)
			{
				stack.add(item.getDefaultStack());
			}
		});
		items.forEach((item) -> {
			if(!(item instanceof BlockItem) && item != EMPTY_PHIAL_ITEM && !(item instanceof AspectItem))
			{
				stack.add(item.getDefaultStack());
			}
		});
		Aspects.ASPECT_TO_CLUSTER_BLOCK.values().forEach((item) -> {
			stack.add(BlockItem.BLOCK_ITEMS.get(item).getDefaultStack());
		});
		Aspects.ASPECT_TO_BUDDING_CLUSTER.values().forEach((item) -> {
			stack.add(BlockItem.BLOCK_ITEMS.get(item).getDefaultStack());
		});
		Aspects.ASPECT_TO_SMALL_CLUSTER.values().forEach((item) -> {
			stack.add(BlockItem.BLOCK_ITEMS.get(item).getDefaultStack());
		});
		Aspects.ASPECT_TO_MEDIUM_CLUSTER.values().forEach((item) -> {
			stack.add(BlockItem.BLOCK_ITEMS.get(item).getDefaultStack());
		});
		Aspects.ASPECT_TO_LARGE_CLUSTER.values().forEach((item) -> {
			stack.add(BlockItem.BLOCK_ITEMS.get(item).getDefaultStack());
		});
		Aspects.ASPECT_TO_CLUSTER.values().forEach((item) -> {
			stack.add(BlockItem.BLOCK_ITEMS.get(item).getDefaultStack());
		});
		Aspects.ASPECT_TO_CRYSTAL.values().forEach((item) -> {
			stack.add(item.getDefaultStack());
		});
		stack.add(EMPTY_PHIAL_ITEM.getDefaultStack());
		Aspects.ASPECT_TO_PHIAL.values().forEach((item) -> {
			stack.add(item.getDefaultStack());
		});
	}

	private static ItemStack getDisplayIcon()
	{
		return MAGICAL_COAL_TIER_2.getDefaultStack();
	}
}
