package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.item.AspectItem;
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
	public static final Item NECROMANCY_HEART;
	public static final Item NECROMANCY_RIBCAGE;
	public static final CustomMobSpawnerItem CUSTOM_SPAWNER;
	public static final Item SPAWN_EGG_BASE;
	public static final BlockItem NEKOMANCY_TABLE;
	public static final BlockItem CRAFTING_HOPPER;
	public static final Item THUWUMIUM_INGOT;
	public static final PickaxeItem THUWUMIUM_PICKAXE;
	public static final AxeItem THUWUMIUM_AXE;
	public static final HoeItem THUWUMIUM_HOE;
	public static final ShovelItem THUWUMIUM_SHOVEL;
	public static final SwordItem THUWUMIUM_SWORD;
	public static final Item THUWUMIC_MAGNIFYING_GLASS;
	public static final ArmorItem THUWUMIUM_HELMET;
	public static final ArmorItem THUWUMIUM_CHESTPLATE;
	public static final ArmorItem THUWUMIUM_LEGGINGS;
	public static final ArmorItem THUWUMIUM_BOOTS;
	public static final ArmorItem GOGGLES;
	public static final BlockItem VIS_LIQUIFIER;
	public static final BlockItem ASPECT_CRAFTING_BLOCK;
	public static final List<Item> ITEMS;

	static
	{
		ITEMS = new ArrayList<>();
		ALCHEMY_MOD_ITEM_GROUP = FabricItemGroupBuilder.create(AlchemyMod
				.getId("alchemy_mod_group"))
				.icon(AlchemyItems::getDisplayIcon)
				.appendItems(AlchemyItems::getStacks)
				.build();
		WITCHY_SPOON_ITEM = register(AlchemyMod.getId("witchy_spoon"), new WitchySpoonItem());
		THROW_BOTTLE = register(AlchemyMod.getId("throw_bottle"), new Item(new FabricItemSettings().maxCount(64).group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP)));
		LINGERING_BOTTLE = register(AlchemyMod.getId("lingering_bottle"), new Item(new FabricItemSettings().maxCount(64).group(ALCHEMY_MOD_ITEM_GROUP)));
		LADLE_ITEM = register(AlchemyMod.getId("ladle"), new LadleItem());
		APOTHECARY_GUIDE = register(AlchemyMod.getId("apothecary_guide_book"), new ApothecaryGuideItem());
		PEDESTAL_ITEM = register(AlchemyMod.getId("pedestal"), new BlockItem(AlchemyBlocks.PEDESTAL_BLOCK, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP)));
		MAGICAL_COAL_TIER_0 =  register(AlchemyMod.getId("magical_coal_0"), new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 0));
		MAGICAL_COAL_TIER_1 =register(AlchemyMod.getId("magical_coal_1"), new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 1));
		MAGICAL_COAL_TIER_2 =register(AlchemyMod.getId("magical_coal_2"), new MagicalCoalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), 2));
		MAGIC_DUST = register(AlchemyMod.getId("magic_dust"), new MagicDustItem());
		SPECIAL_PICKAXE_ITEM =register(AlchemyMod.getId("magic_pickaxe"), new SpecialPickaxeItem());
		SPECIAL_AXE_ITEM = register(AlchemyMod.getId("magic_axe"), new SpecialAxeItem());
		JAR_ITEM = register(AlchemyMod.getId("jar"), new BlockItem(AlchemyBlocks.JAR_BLOCK, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP)));
		EMPTY_PHIAL_ITEM = register(AlchemyMod.getId("phial/empty"), new GlassPhialItem(Aspect.EMPTY));
		PHIAL_SHELF_ITEM = register(AlchemyMod.getId("phial_shelf"), new BlockItem(AlchemyBlocks.PHIAL_SHELF_BLOCK, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP)));
		RESEARCH_BOOK_ITEM = register(AlchemyMod.getId("research_book"), new ResearchBookItem(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		ASPECT_PIPE_ITEM = register(AlchemyMod.getId("aspect_pipe"), new BlockItem(AlchemyBlocks.ASPECT_PIPE_BLOCK, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		NECROMANCY_SKULL = register(AlchemyMod.getId("necromancy_skull"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		NECROMANCY_ARM = register(AlchemyMod.getId("necromancy_arm"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		NECROMANCY_LEG = register(AlchemyMod.getId("necromancy_leg"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		NECROMANCY_HEART = register(AlchemyMod.getId("necromancy_heart"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		NECROMANCY_RIBCAGE = register(AlchemyMod.getId("necromancy_ribcage"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		CUSTOM_SPAWNER = register(AlchemyMod.getId("custom_spawner"), new CustomMobSpawnerItem(AlchemyBlocks.CUSTOM_SPAWNER, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		SPAWN_EGG_BASE = register(AlchemyMod.getId("spawn_egg_base"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		NEKOMANCY_TABLE = register(AlchemyMod.getId("nekomancy_table"), new BlockItem(AlchemyBlocks.NEKOMANCY_TABLE, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		CRAFTING_HOPPER = register(AlchemyMod.getId("crafting_hopper"), new BlockItem(AlchemyBlocks.CRAFTING_HOPPER, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_INGOT = register(AlchemyMod.getId("thuwumium_ingot"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_PICKAXE = register(AlchemyMod.getId("thuwumium_pickaxe"), new OpenPickaxeItem(AlchemyToolMaterials.THUWUMIUM, 1, -2.8F, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_AXE = register(AlchemyMod.getId("thuwumium_axe"), new OpenAxeItem(AlchemyToolMaterials.THUWUMIUM, 5, -3.0F, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_HOE = register(AlchemyMod.getId("thuwumium_hoe"), new OpenHoeItem(AlchemyToolMaterials.THUWUMIUM, -2, -1.0F, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_SHOVEL = register(AlchemyMod.getId("thuwumium_shovel"), new ShovelItem(AlchemyToolMaterials.THUWUMIUM, 1.5F, -3.0F, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_SWORD = register(AlchemyMod.getId("thuwumium_sword"), new SwordItem(AlchemyToolMaterials.THUWUMIUM, 3, -2.4F, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_HELMET = register(AlchemyMod.getId("thuwumium_helmet"), new ArmorItem(AlchemyArmorMaterials.THUWUMIUM, EquipmentSlot.HEAD, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_CHESTPLATE = register(AlchemyMod.getId("thuwumium_chestplate"), new ArmorItem(AlchemyArmorMaterials.THUWUMIUM, EquipmentSlot.CHEST, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_LEGGINGS = register(AlchemyMod.getId("thuwumium_leggings"), new ArmorItem(AlchemyArmorMaterials.THUWUMIUM, EquipmentSlot.LEGS, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIUM_BOOTS = register(AlchemyMod.getId("thuwumium_boots"), new ArmorItem(AlchemyArmorMaterials.THUWUMIUM, EquipmentSlot.FEET, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		GOGGLES = register(AlchemyMod.getId("goggles"), new ArmorItem(AlchemyArmorMaterials.GOGGLES, EquipmentSlot.HEAD, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		THUWUMIC_MAGNIFYING_GLASS = register(AlchemyMod.getId("magnifying_glass"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		VIS_LIQUIFIER = register(AlchemyMod.getId("vis_liquifier"), new BlockItem(AlchemyBlocks.VIS_LIQUIFIER, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		ASPECT_CRAFTING_BLOCK = register(AlchemyMod.getId("aspect_crafting_block"), new BlockItem(AlchemyBlocks.ASPECT_CRAFTING_BLOCK, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
	}

	private static <T extends Item> T register(Identifier id, T item)
	{
		Registry.register(Registry.ITEM, id, item);
		ITEMS.add(item);
		return item;
	}

	private static void getStacks(List<ItemStack> stack)
	{
		ITEMS.forEach((item) -> {
			if(item instanceof BlockItem)
			{
				stack.add(item.getDefaultStack());
			}
		});
		ITEMS.forEach((item) -> {
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
