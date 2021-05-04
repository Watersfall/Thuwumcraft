package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.item.AspectItem;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.fluid.AlchemyFluids;
import net.watersfall.alchemy.item.armor.AlchemistArmorItem;
import net.watersfall.alchemy.item.armor.AlchemyArmorMaterials;
import net.watersfall.alchemy.item.armor.SpeedBootsItem;
import net.watersfall.alchemy.item.tool.*;

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
	public static final SpecialHoeItem SPECIAL_HOE_ITEM;
	public static final SpecialSwordItem SPECIAL_SWORD_ITEM;
	public static final SpecialBattleaxeItem SPECIAL_BATTLEAXE_ITEM;
	public static final SpecialBowItem SPECIAL_BOW_ITEM;
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
	public static final Item BRASS_INGOT;
	public static final BlockItem POTION_SPRAYER_ITEM;
	public static final BlockItem ESSENTIA_SMELTERY_ITEM;
	public static final BlockItem ESSENTIA_REFINERY;
	public static final AlchemistArmorItem ALCHEMIST_HOOD;
	public static final AlchemistArmorItem ALCHEMIST_ROBE;
	public static final AlchemistArmorItem ALCHEMIST_LEGGINGS;
	public static final AlchemistArmorItem ALCHEMIST_SHOES;
	public static final ArmorItem FORTRESS_HELMET;
	public static final ArmorItem FORTRESS_CHESTPLATE;
	public static final ArmorItem FORTRESS_LEGGINGS;
	public static final SpeedBootsItem BOOTS_OF_BLINDING_SPEED;
	public static final BlockItem ARCANE_LAMP_ITEM;
	public static final BucketItem DIMENSIONAL_FLUID_BUCKET;
	public static final EyeOfTheUnknownItem EYE_OF_THE_UNKNOWN_ITEM;
	public static final BlockItem DEEPSLATE_GRASS;
	public static final BlockItem GLOWING_DEEPSLATE;
	public static final CastingStaffItem SNOW_STAFF;
	public static final CastingStaffItem ICE_STAFF;
	public static final Item ICE_PROJECTILE_ITEM;
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
		SPECIAL_HOE_ITEM = register(AlchemyMod.getId("magic_hoe"), new SpecialHoeItem());
		SPECIAL_SWORD_ITEM = register(AlchemyMod.getId("magic_sword"), new SpecialSwordItem());
		SPECIAL_BATTLEAXE_ITEM = register(AlchemyMod.getId("magic_battleaxe"), new SpecialBattleaxeItem());
		SPECIAL_BOW_ITEM = register(AlchemyMod.getId("magic_bow"), new SpecialBowItem(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP).maxCount(1)));
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
		BRASS_INGOT = register(AlchemyMod.getId("brass_ingot"), new Item(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		POTION_SPRAYER_ITEM = register(AlchemyMod.getId("potion_sprayer"), new BlockItem(AlchemyBlocks.POTION_SPRAYER_BLOCK, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		ESSENTIA_SMELTERY_ITEM = register(AlchemyMod.getId("essentia_smeltery"), new BlockItem(AlchemyBlocks.ESSENTIA_SMELTERY, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		ESSENTIA_REFINERY = register(AlchemyMod.getId("essentia_refinery"), new BlockItem(AlchemyBlocks.ESSENTIA_REFINERY_BLOCK, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		ALCHEMIST_HOOD = register(AlchemyMod.getId("alchemist_hood"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP), 0.15F));
		ALCHEMIST_ROBE = register(AlchemyMod.getId("alchemist_robe"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.CHEST, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP), 0.15F));
		ALCHEMIST_LEGGINGS = register(AlchemyMod.getId("alchemist_leggings"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.LEGS, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP), 0.15F));
		ALCHEMIST_SHOES = register(AlchemyMod.getId("alchemist_shoes"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.FEET, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP), 0.15F));
		FORTRESS_HELMET = register(AlchemyMod.getId("fortress_helmet"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.HEAD, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		FORTRESS_CHESTPLATE = register(AlchemyMod.getId("fortress_chestplate"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.CHEST, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		FORTRESS_LEGGINGS = register(AlchemyMod.getId("fortress_leggings"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.LEGS, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		BOOTS_OF_BLINDING_SPEED = register(AlchemyMod.getId("boots_of_blinding_speed"), new SpeedBootsItem(AlchemyArmorMaterials.MAGIC, 0.5F));
		ARCANE_LAMP_ITEM = register(AlchemyMod.getId("arcane_lamp"), new BlockItem(AlchemyBlocks.ARCANE_LAMP_BLOCK, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		DIMENSIONAL_FLUID_BUCKET = register(AlchemyMod.getId("dimensional_fluid_bucket"), new BucketItem(AlchemyFluids.DIMENSIONAL_STILL, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP).maxCount(1)));
		EYE_OF_THE_UNKNOWN_ITEM = register(AlchemyMod.getId("eye_of_the_unknown"), new EyeOfTheUnknownItem(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP).rarity(Rarity.EPIC)));
		DEEPSLATE_GRASS = register(AlchemyMod.getId("deepslate_grass"), new BlockItem(AlchemyBlocks.DEEPSLATE_GRASS, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		GLOWING_DEEPSLATE = register(AlchemyMod.getId("glowing_deepslate"), new BlockItem(AlchemyBlocks.GLOWING_DEEPSLATE, new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP)));
		ICE_STAFF = register(AlchemyMod.getId("ice_staff"), new CastingStaffItem(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP).maxCount(1), CastingStaffItem.SpellAction.ICE, 10, 20));
		SNOW_STAFF = register(AlchemyMod.getId("snow_staff"), new CastingStaffItem(new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP).maxCount(1), CastingStaffItem.SpellAction.SNOW, 5, 10));
		ICE_PROJECTILE_ITEM = register(AlchemyMod.getId("ice_projectile"), new Item(new FabricItemSettings()));
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
