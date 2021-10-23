package net.watersfall.thuwumcraft.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.abilities.item.WandFocusAbilityImpl;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.api.item.AspectItem;
import net.watersfall.thuwumcraft.entity.golem.goal.ExtractFromInventoryGoal;
import net.watersfall.thuwumcraft.entity.golem.goal.InsertIntoInventoryGoal;
import net.watersfall.thuwumcraft.entity.golem.goal.PickupItemGoal;
import net.watersfall.thuwumcraft.item.*;
import net.watersfall.thuwumcraft.item.armor.AlchemistArmorItem;
import net.watersfall.thuwumcraft.item.armor.AlchemyArmorMaterials;
import net.watersfall.thuwumcraft.item.armor.SpeedBootsItem;
import net.watersfall.thuwumcraft.item.golem.GolemBellItem;
import net.watersfall.thuwumcraft.item.golem.GolemItem;
import net.watersfall.thuwumcraft.item.golem.GolemMarkerItem;
import net.watersfall.thuwumcraft.item.golem.GolemSealItem;
import net.watersfall.thuwumcraft.item.tool.*;
import net.watersfall.thuwumcraft.item.wand.*;
import net.watersfall.thuwumcraft.spell.Spell;
import net.watersfall.thuwumcraft.spell.SpellAction;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.ArrayList;
import java.util.List;

public class ThuwumcraftItems
{
	public static ItemGroup ALCHEMY_MOD_ITEM_GROUP;

	public static BlockItem GREATWOOD_LOG_BLOCK;
	public static BlockItem STRIPPED_GREATWOOD_LOG_BLOCK;
	public static BlockItem GREATWOOD_WOOD_BLOCK;
	public static BlockItem STRIPPED_GREATWOOD_WOOD_BLOCK;
	public static BlockItem GREATWOOD_PLANKS_BLOCK;
	public static BlockItem GREATWOOD_SLAB_BLOCK;
	public static BlockItem GREATWOOD_STAIRS_BLOCK;
	public static BlockItem GREATWOOD_LEAVES_BLOCK;
	public static BlockItem SILVERWOOD_LOG_BLOCK;
	public static BlockItem STRIPPED_SILVERWOOD_LOG_BLOCK;
	public static BlockItem SILVERWOOD_WOOD_BLOCK;
	public static BlockItem STRIPPED_SILVERWOOD_WOOD_BLOCK;
	public static BlockItem SILVERWOOD_PLANKS_BLOCK;
	public static BlockItem SILVERWOOD_SLAB_BLOCK;
	public static BlockItem SILVERWOOD_STAIRS_BLOCK;
	public static BlockItem SILVERWOOD_LEAVES;
	public static BlockItem SILVERWOOD_SAPLING_BLOCK;
	public static BlockItem ARCANE_STONE_BLOCK;
	public static BlockItem ARCANE_STONE_SLAB_BLOCK;
	public static BlockItem ARCANE_STONE_STAIRS_BLOCK;
	public static BlockItem PHIAL_SHELF_BLOCK;
	public static BlockItem JAR_BLOCK;
	public static BlockItem BRASS_PIPE_BLOCK;
	public static BlockItem NEKOMANCY_TABLE_BLOCK;
	public static BlockItem CRAFTING_HOPPER_BLOCK;
	public static BlockItem VIS_LIQUIFIER_BLOCK;
	public static BlockItem ASPECT_CRAFTING_TABLE_BLOCK;
	public static BlockItem POTION_SPRAYER_BLOCK;
	public static BlockItem ESSENTIA_SMELTERY_BLOCK;
	public static BlockItem ESSENTIA_REFINERY_BLOCK;
	public static BlockItem ARCANE_LAMP_BLOCK;
	public static BlockItem DEEPSLATE_GRASS_BLOCK;
	public static BlockItem GLOWING_DEEPSLATE_BLOCK;
	public static BlockItem ARCANE_SEAL_BLOCK;
	public static BlockItem SPAWNER_FRAME_BLOCK;
	public static BlockItem WAND_WORKBENCH_BLOCK;
	public static BlockItem HUNGRY_CHEST_BLOCK;
	public static BlockItem CUSTOM_SPAWNER_BLOCK;

	public static WitchySpoonItem WITCHY_SPOON;
	public static Item THROW_BOTTLE;
	public static Item LINGERING_BOTTLE;
	public static Item LADLE;
	public static ApothecaryGuideItem APOTHECARY_GUIDE;
	public static Item PEDESTAL;
	public static MagicalCoalItem MAGICAL_COAL_TIER_0;
	public static MagicalCoalItem MAGICAL_COAL_TIER_1;
	public static MagicalCoalItem MAGICAL_COAL_TIER_2;
	public static MagicDustItem MAGIC_DUST;
	public static SpecialPickaxeItem SPECIAL_PICKAXE;
	public static SpecialAxeItem SPECIAL_AXE;
	public static SpecialHoeItem SPECIAL_HOE;
	public static SpecialSwordItem SPECIAL_SWORD;
	public static SpecialBattleaxeItem SPECIAL_BATTLEAXE;
	public static SpecialBowItem SPECIAL_BOW;
	public static SpecialShovelItem SPECIAL_SHOVEL;
	public static GlassPhialItem GLASS_PHIAL;
	public static ResearchBookItem RESEARCH_BOOK;
	public static Item NECROMANCY_SKULL;
	public static Item NECROMANCY_ARM;
	public static Item NECROMANCY_LEG;
	public static Item NECROMANCY_HEART;
	public static Item NECROMANCY_RIBCAGE;
	public static Item SPAWN_EGG_BASE;
	public static Item THUWUMIUM_INGOT;
	public static PickaxeItem THUWUMIUM_PICKAXE;
	public static AxeItem THUWUMIUM_AXE;
	public static HoeItem THUWUMIUM_HOE;
	public static ShovelItem THUWUMIUM_SHOVEL;
	public static SwordItem THUWUMIUM_SWORD;
	public static Item THUWUMIC_MAGNIFYING_GLASS;
	public static ArmorItem THUWUMIUM_HELMET;
	public static ArmorItem THUWUMIUM_CHESTPLATE;
	public static ArmorItem THUWUMIUM_LEGGINGS;
	public static ArmorItem THUWUMIUM_BOOTS;
	public static ArmorItem GOGGLES;
	public static Item BRASS_INGOT;
	public static AlchemistArmorItem ALCHEMIST_HOOD;
	public static AlchemistArmorItem ALCHEMIST_ROBE;
	public static AlchemistArmorItem ALCHEMIST_LEGGINGS;
	public static AlchemistArmorItem ALCHEMIST_SHOES;
	public static ArmorItem FORTRESS_HELMET;
	public static ArmorItem FORTRESS_CHESTPLATE;
	public static ArmorItem FORTRESS_LEGGINGS;
	public static ArmorItem FORTRESS_BOOTS;
	public static SpeedBootsItem BOOTS_OF_BLINDING_SPEED;
	public static BucketItem DIMENSIONAL_FLUID_BUCKET;
	public static EyeOfTheUnknownItem EYE_OF_THE_UNKNOWN;
	public static CastingStaffItem SNOW_STAFF;
	public static CastingStaffItem ICE_STAFF;
	public static ContinuousCastingStaffItem WATER_STAFF;
	public static Item ICE_PROJECTILE;
	public static ContinuousCastingStaffItem FIRE_STAFF;
	public static CastingStaffItem SAND_STAFF;
	public static PortableHoleWand WAND_OF_HOLES;
	public static WandItem WAND;
	public static WandFocusItem WAND_FOCUS;
	public static WandCoreItem WOOD_CORE;
	public static WandCoreItem GREATWOOD_CORE;
	public static WandCoreItem SILVERWOOD_CORE;
	public static WandCapItem IRON_CAP;
	public static WandCapItem BRASS_CAP;
	public static WandCapItem THUWUMIUM_CAP;
	public static AlchemyBindingItem ALCHEMY_BINDING;
	public static ArcaneRuneItem AIR_RUNE, WATER_RUNE, FIRE_RUNE, EARTH_RUNE, ORDER_RUNE, DISORDER_RUNE;
	public static Item GOGGLES_OVERLAY;
	public static Item GOLEM;
	public static GolemBellItem GOLEM_BELL;
	public static GolemSealItem PICKUP_SEAL;
	public static GolemSealItem STOCK_SEAL;
	public static GolemMarkerItem WHITE_GOLEM_MARKER;
	public static GolemMarkerItem ORANGE_GOLEM_MARKER;
	public static GolemMarkerItem MAGENTA_GOLEM_MARKER;
	public static GolemMarkerItem LIGHT_BLUE_GOLEM_MARKER;
	public static GolemMarkerItem YELLOW_GOLEM_MARKER;
	public static GolemMarkerItem LIME_GOLEM_MARKER;
	public static GolemMarkerItem PINK_GOLEM_MARKER;
	public static GolemMarkerItem GRAY_GOLEM_MARKER;
	public static GolemMarkerItem LIGHT_GRAY_GOLEM_MARKER;
	public static GolemMarkerItem CYAN_GOLEM_MARKER;
	public static GolemMarkerItem PURPLE_GOLEM_MARKER;
	public static GolemMarkerItem BLUE_GOLEM_MARKER;
	public static GolemMarkerItem BROWN_GOLEM_MARKER;
	public static GolemMarkerItem GREEN_GOLEM_MARKER;
	public static GolemMarkerItem RED_GOLEM_MARKER;
	public static GolemMarkerItem BLACK_GOLEM_MARKER;
	public static List<Item> ITEMS;

	public static void register()
	{
		ITEMS = new ArrayList<>();
		ALCHEMY_MOD_ITEM_GROUP = FabricItemGroupBuilder.create(Thuwumcraft
				.getId("alchemy_mod_group"))
				.icon(ThuwumcraftItems::getDisplayIcon)
				.appendItems(ThuwumcraftItems::getStacks)
				.build();

		GREATWOOD_LOG_BLOCK = register(ThuwumcraftBlocks.GREATWOOD_LOG, defaultSettings());
		STRIPPED_GREATWOOD_LOG_BLOCK = register(ThuwumcraftBlocks.STRIPPED_GREATWOOD_LOG, defaultSettings());
		GREATWOOD_WOOD_BLOCK = register(ThuwumcraftBlocks.GREATWOOD_WOOD, defaultSettings());
		STRIPPED_GREATWOOD_WOOD_BLOCK = register(ThuwumcraftBlocks.STRIPPED_GREATWOOD_WOOD, defaultSettings());
		GREATWOOD_PLANKS_BLOCK = register(ThuwumcraftBlocks.GREATWOOD_PLANKS, defaultSettings());
		GREATWOOD_SLAB_BLOCK = register(ThuwumcraftBlocks.GREATWOOD_SLAB, defaultSettings());
		GREATWOOD_STAIRS_BLOCK = register(ThuwumcraftBlocks.GREATWOOD_STAIRS, defaultSettings());
		GREATWOOD_LEAVES_BLOCK = register(ThuwumcraftBlocks.GREATWOOD_LEAVES, defaultSettings());
		SILVERWOOD_LOG_BLOCK = register(ThuwumcraftBlocks.SILVERWOOD_LOG, defaultSettings());
		STRIPPED_SILVERWOOD_LOG_BLOCK = register(ThuwumcraftBlocks.STRIPPED_SILVERWOOD_LOG, defaultSettings());
		SILVERWOOD_WOOD_BLOCK = register(ThuwumcraftBlocks.SILVERWOOD_WOOD, defaultSettings());
		STRIPPED_SILVERWOOD_WOOD_BLOCK = register(ThuwumcraftBlocks.STRIPPED_SILVERWOOD_WOOD, defaultSettings());
		SILVERWOOD_PLANKS_BLOCK = register(ThuwumcraftBlocks.SILVERWOOD_PLANKS, defaultSettings());
		SILVERWOOD_SLAB_BLOCK = register(ThuwumcraftBlocks.SILVERWOOD_SLAB, defaultSettings());
		SILVERWOOD_STAIRS_BLOCK = register(ThuwumcraftBlocks.SILVERWOOD_STAIRS, defaultSettings());
		SILVERWOOD_LEAVES = register(ThuwumcraftBlocks.SILVERWOOD_LEAVES, defaultSettings());
		ARCANE_STONE_BLOCK = register(ThuwumcraftBlocks.ARCANE_STONE, defaultSettings());
		ARCANE_STONE_SLAB_BLOCK = register(ThuwumcraftBlocks.ARCANE_STONE_SLAB, defaultSettings());
		ARCANE_STONE_STAIRS_BLOCK = register(ThuwumcraftBlocks.ARCANE_STONE_STAIRS, defaultSettings());
		PEDESTAL = register(ThuwumcraftBlocks.PEDESTAL, defaultSettings());
		JAR_BLOCK = register(ThuwumcraftBlocks.JAR, defaultSettings());
		PHIAL_SHELF_BLOCK = register(ThuwumcraftBlocks.PHIAL_SHELF, defaultSettings());
		BRASS_PIPE_BLOCK = register(ThuwumcraftBlocks.BRASS_PIPE, defaultSettings());
		NEKOMANCY_TABLE_BLOCK = register(ThuwumcraftBlocks.NEKOMANCY_TABLE, defaultSettings());
		CRAFTING_HOPPER_BLOCK = register(ThuwumcraftBlocks.CRAFTING_HOPPER, defaultSettings());
		VIS_LIQUIFIER_BLOCK = register(ThuwumcraftBlocks.VIS_LIQUIFIER, defaultSettings());
		ASPECT_CRAFTING_TABLE_BLOCK = register(ThuwumcraftBlocks.ASPECT_CRAFTING_TABLE, defaultSettings());
		POTION_SPRAYER_BLOCK = register(ThuwumcraftBlocks.POTION_SPRAYER, defaultSettings());
		ESSENTIA_SMELTERY_BLOCK = register(ThuwumcraftBlocks.ESSENTIA_SMELTERY, defaultSettings());
		ESSENTIA_REFINERY_BLOCK = register(ThuwumcraftBlocks.ESSENTIA_REFINERY, defaultSettings());
		ARCANE_LAMP_BLOCK = register(ThuwumcraftBlocks.ARCANE_LAMP, defaultSettings());
		DEEPSLATE_GRASS_BLOCK = register(ThuwumcraftBlocks.DEEPSLATE_GRASS, defaultSettings());
		GLOWING_DEEPSLATE_BLOCK = register(ThuwumcraftBlocks.GLOWING_DEEPSLATE, defaultSettings());
		SILVERWOOD_SAPLING_BLOCK = register(ThuwumcraftBlocks.SILVERWOOD_SAPLING, defaultSettings());
		ARCANE_SEAL_BLOCK = register(ThuwumcraftBlocks.ARCANE_SEAL, defaultSettings());
		SPAWNER_FRAME_BLOCK = register(ThuwumcraftBlocks.SPAWNER_FRAME, defaultSettings());
		WAND_WORKBENCH_BLOCK = register(ThuwumcraftBlocks.WAND_WORKBENCH, defaultSettings());
		CUSTOM_SPAWNER_BLOCK = register(ThuwumcraftBlocks.CUSTOM_SPAWNER, defaultSettings());
		HUNGRY_CHEST_BLOCK = register(ThuwumcraftBlocks.HUNGRY_CHEST, defaultSettings());

		WITCHY_SPOON = register(Thuwumcraft.getId("witchy_spoon"), new WitchySpoonItem());
		THROW_BOTTLE = register(Thuwumcraft.getId("throw_bottle"), new SpecialGlassBottleItem(new FabricItemSettings().maxCount(64).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), Items.SPLASH_POTION::getDefaultStack));
		LINGERING_BOTTLE = register(Thuwumcraft.getId("lingering_bottle"), new SpecialGlassBottleItem(new FabricItemSettings().maxCount(64).group(ALCHEMY_MOD_ITEM_GROUP), Items.LINGERING_POTION::getDefaultStack));
		LADLE = register(Thuwumcraft.getId("ladle"), new LadleItem());
		APOTHECARY_GUIDE = register(Thuwumcraft.getId("apothecary_guide_book"), new ApothecaryGuideItem());
		MAGICAL_COAL_TIER_0 =register(Thuwumcraft.getId("magical_coal_0"), new MagicalCoalItem(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), 0));
		MAGICAL_COAL_TIER_1 =register(Thuwumcraft.getId("magical_coal_1"), new MagicalCoalItem(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), 1));
		MAGICAL_COAL_TIER_2 =register(Thuwumcraft.getId("magical_coal_2"), new MagicalCoalItem(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), 2));
		MAGIC_DUST = register(Thuwumcraft.getId("magic_dust"), new MagicDustItem());
		SPECIAL_PICKAXE =register(Thuwumcraft.getId("magic_pickaxe"), new SpecialPickaxeItem());
		SPECIAL_AXE = register(Thuwumcraft.getId("magic_axe"), new SpecialAxeItem());
		SPECIAL_HOE = register(Thuwumcraft.getId("magic_hoe"), new SpecialHoeItem());
		SPECIAL_SWORD = register(Thuwumcraft.getId("magic_sword"), new SpecialSwordItem());
		SPECIAL_BATTLEAXE = register(Thuwumcraft.getId("magic_battleaxe"), new SpecialBattleaxeItem());
		SPECIAL_BOW = register(Thuwumcraft.getId("magic_bow"), new SpecialBowItem(defaultSettings().maxCount(1)));
		SPECIAL_SHOVEL = register(Thuwumcraft.getId("magic_shovel"), new SpecialShovelItem());
		GLASS_PHIAL = register(Thuwumcraft.getId("phial"), new GlassPhialItem());
		RESEARCH_BOOK = register(Thuwumcraft.getId("research_book"), new ResearchBookItem(defaultSettings()));
		NECROMANCY_SKULL = register(Thuwumcraft.getId("necromancy_skull"), new Item(defaultSettings()));
		NECROMANCY_ARM = register(Thuwumcraft.getId("necromancy_arm"), new Item(defaultSettings()));
		NECROMANCY_LEG = register(Thuwumcraft.getId("necromancy_leg"), new Item(defaultSettings()));
		NECROMANCY_HEART = register(Thuwumcraft.getId("necromancy_heart"), new Item(defaultSettings()));
		NECROMANCY_RIBCAGE = register(Thuwumcraft.getId("necromancy_ribcage"), new Item(defaultSettings()));
		SPAWN_EGG_BASE = register(Thuwumcraft.getId("spawn_egg_base"), new Item(defaultSettings()));
		THUWUMIUM_INGOT = register(Thuwumcraft.getId("thuwumium_ingot"), new Item(defaultSettings()));
		THUWUMIUM_PICKAXE = register(Thuwumcraft.getId("thuwumium_pickaxe"), new OpenPickaxeItem(AlchemyToolMaterials.THUWUMIUM, 1, -2.8F, defaultSettings()));
		THUWUMIUM_AXE = register(Thuwumcraft.getId("thuwumium_axe"), new OpenAxeItem(AlchemyToolMaterials.THUWUMIUM, 5, -3.0F, defaultSettings()));
		THUWUMIUM_HOE = register(Thuwumcraft.getId("thuwumium_hoe"), new OpenHoeItem(AlchemyToolMaterials.THUWUMIUM, -2, -1.0F, defaultSettings()));
		THUWUMIUM_SHOVEL = register(Thuwumcraft.getId("thuwumium_shovel"), new ShovelItem(AlchemyToolMaterials.THUWUMIUM, 1.5F, -3.0F, defaultSettings()));
		THUWUMIUM_SWORD = register(Thuwumcraft.getId("thuwumium_sword"), new SwordItem(AlchemyToolMaterials.THUWUMIUM, 3, -2.4F, defaultSettings()));
		THUWUMIUM_HELMET = register(Thuwumcraft.getId("thuwumium_helmet"), new ArmorItem(AlchemyArmorMaterials.THUWUMIUM, EquipmentSlot.HEAD, defaultSettings()));
		THUWUMIUM_CHESTPLATE = register(Thuwumcraft.getId("thuwumium_chestplate"), new ArmorItem(AlchemyArmorMaterials.THUWUMIUM, EquipmentSlot.CHEST, defaultSettings()));
		THUWUMIUM_LEGGINGS = register(Thuwumcraft.getId("thuwumium_leggings"), new ArmorItem(AlchemyArmorMaterials.THUWUMIUM, EquipmentSlot.LEGS, defaultSettings()));
		THUWUMIUM_BOOTS = register(Thuwumcraft.getId("thuwumium_boots"), new ArmorItem(AlchemyArmorMaterials.THUWUMIUM, EquipmentSlot.FEET, defaultSettings()));
		GOGGLES = register(Thuwumcraft.getId("goggles"), new ArmorItem(AlchemyArmorMaterials.GOGGLES, EquipmentSlot.HEAD, defaultSettings()));
		THUWUMIC_MAGNIFYING_GLASS = register(Thuwumcraft.getId("magnifying_glass"), new Item(defaultSettings()));
		BRASS_INGOT = register(Thuwumcraft.getId("brass_ingot"), new Item(defaultSettings()));
		ALCHEMIST_HOOD = register(Thuwumcraft.getId("alchemist_hood"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, defaultSettings(), 0.15F));
		ALCHEMIST_ROBE = register(Thuwumcraft.getId("alchemist_robe"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.CHEST, defaultSettings(), 0.15F));
		ALCHEMIST_LEGGINGS = register(Thuwumcraft.getId("alchemist_leggings"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.LEGS, defaultSettings(), 0.15F));
		ALCHEMIST_SHOES = register(Thuwumcraft.getId("alchemist_shoes"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.FEET, defaultSettings(), 0.15F));
		FORTRESS_HELMET = register(Thuwumcraft.getId("fortress_helmet"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.HEAD, defaultSettings()));
		FORTRESS_CHESTPLATE = register(Thuwumcraft.getId("fortress_chestplate"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.CHEST, defaultSettings()));
		FORTRESS_LEGGINGS = register(Thuwumcraft.getId("fortress_leggings"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.LEGS, defaultSettings()));
		FORTRESS_BOOTS = register(Thuwumcraft.getId("fortress_boots"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.FEET, defaultSettings()));
		BOOTS_OF_BLINDING_SPEED = register(Thuwumcraft.getId("boots_of_blinding_speed"), new SpeedBootsItem(AlchemyArmorMaterials.MAGIC, 0.5F));
		DIMENSIONAL_FLUID_BUCKET = register(Thuwumcraft.getId("dimensional_fluid_bucket"), new BucketItem(ThuwumcraftFluids.DIMENSIONAL_STILL, defaultSettings().maxCount(1)));
		EYE_OF_THE_UNKNOWN = register(Thuwumcraft.getId("eye_of_the_unknown"), new EyeOfTheUnknownItem(defaultSettings().rarity(Rarity.EPIC)));
		ICE_STAFF = register(Thuwumcraft.getId("ice_staff"), new CastingStaffItem(defaultSettings().maxCount(1), SpellAction.ICE, 10, 20));
		SNOW_STAFF = register(Thuwumcraft.getId("snow_staff"), new CastingStaffItem(defaultSettings().maxCount(1), SpellAction.SNOW, 5, 10));
		WATER_STAFF = register(Thuwumcraft.getId("water_staff"), new ContinuousCastingStaffItem(defaultSettings().maxCount(1), SpellAction.WATER, 10, 1));
		FIRE_STAFF = register(Thuwumcraft.getId("fire_staff"), new ContinuousCastingStaffItem(defaultSettings().maxCount(1), SpellAction.FIRE, 10, 1));
		ICE_PROJECTILE = register(Thuwumcraft.getId("ice_projectile"), new Item(new FabricItemSettings()));
		SAND_STAFF = register(Thuwumcraft.getId("sand_staff"), new CastingStaffItem(defaultSettings().maxCount(1), SpellAction.SAND, 10, 10));
		WAND_OF_HOLES = register(Thuwumcraft.getId("wand_of_holes"), new PortableHoleWand(defaultSettings().maxCount(1)));
		WAND = register(Thuwumcraft.getId("wand"), new WandItem(defaultSettings().maxCount(1)));
		WAND_FOCUS = register(Thuwumcraft.getId("wand_focus"), new WandFocusItem(defaultSettings().maxCount(1)));
		WOOD_CORE = register(Thuwumcraft.getId("wood_core"), new WandCoreItem(WandCoreMaterials.WOOD, defaultSettings().maxCount(1)));
		GREATWOOD_CORE = register(Thuwumcraft.getId("greatwood_core"), new WandCoreItem(WandCoreMaterials.GREATWOOD, defaultSettings().maxCount(1)));
		SILVERWOOD_CORE = register(Thuwumcraft.getId("silverwood_core"), new WandCoreItem(WandCoreMaterials.SILVERWOOD, defaultSettings().maxCount(1)));
		IRON_CAP = register(Thuwumcraft.getId("iron_cap"), new WandCapItem(WandCapMaterials.IRON, defaultSettings().maxCount(1)));
		BRASS_CAP = register(Thuwumcraft.getId("brass_cap"), new WandCapItem(WandCapMaterials.BRASS, defaultSettings().maxCount(1)));
		THUWUMIUM_CAP = register(Thuwumcraft.getId("thuwumium_cap"), new WandCapItem(WandCapMaterials.THUWUMIUM, defaultSettings().maxCount(1)));
		ALCHEMY_BINDING = register(Thuwumcraft.getId("alchemy_binding"), new AlchemyBindingItem(defaultSettings()));
		AIR_RUNE = register(Thuwumcraft.getId("air_rune"), new ArcaneRuneItem(Aspects.AIR, defaultSettings().maxCount(1)));
		EARTH_RUNE = register(Thuwumcraft.getId("earth_rune"), new ArcaneRuneItem(Aspects.EARTH, defaultSettings().maxCount(1)));
		FIRE_RUNE = register(Thuwumcraft.getId("fire_rune"), new ArcaneRuneItem(Aspects.FIRE, defaultSettings().maxCount(1)));
		WATER_RUNE = register(Thuwumcraft.getId("water_rune"), new ArcaneRuneItem(Aspects.WATER, defaultSettings().maxCount(1)));
		ORDER_RUNE = register(Thuwumcraft.getId("order_rune"), new ArcaneRuneItem(Aspects.ORDER, defaultSettings().maxCount(1)));
		DISORDER_RUNE = register(Thuwumcraft.getId("disorder_rune"), new ArcaneRuneItem(Aspects.DISORDER, defaultSettings().maxCount(1)));
		GOGGLES_OVERLAY = register(Thuwumcraft.getId("goggles_overlay_item"), new Item(new FabricItemSettings()));
		GOLEM = register("golem", new GolemItem(defaultSettings()));
		GOLEM_BELL = register("golemancy_bell", new GolemBellItem(defaultSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
		PICKUP_SEAL = register("pickup_golem_seal", new GolemSealItem(defaultSettings(), new PickupItemGoal(null), new InsertIntoInventoryGoal(null)));
		STOCK_SEAL = register("stock_golem_seal", new GolemSealItem(defaultSettings(), new ExtractFromInventoryGoal(null), new InsertIntoInventoryGoal(null)));
		WHITE_GOLEM_MARKER = register(DyeColor.WHITE);
		ORANGE_GOLEM_MARKER = register(DyeColor.ORANGE);
		MAGENTA_GOLEM_MARKER = register(DyeColor.MAGENTA);
		LIGHT_BLUE_GOLEM_MARKER = register(DyeColor.LIGHT_BLUE);
		YELLOW_GOLEM_MARKER = register(DyeColor.YELLOW);
		LIME_GOLEM_MARKER = register(DyeColor.LIME);
		PINK_GOLEM_MARKER = register(DyeColor.PINK);
		GRAY_GOLEM_MARKER = register(DyeColor.GRAY);
		LIGHT_GRAY_GOLEM_MARKER = register(DyeColor.LIGHT_GRAY);
		CYAN_GOLEM_MARKER = register(DyeColor.CYAN);
		PURPLE_GOLEM_MARKER = register(DyeColor.PURPLE);
		BLUE_GOLEM_MARKER = register(DyeColor.BLUE);
		BROWN_GOLEM_MARKER = register(DyeColor.BROWN);
		GREEN_GOLEM_MARKER = register(DyeColor.GREEN);
		RED_GOLEM_MARKER = register(DyeColor.RED);
		BLACK_GOLEM_MARKER = register(DyeColor.BLACK);
	}

	private static GolemMarkerItem register(DyeColor color)
	{
		return register("golem_marker/" + color.asString(), new GolemMarkerItem(defaultSettings().maxCount(1), color));
	}

	private static <T extends Item> T register(Identifier id, T item)
	{
		Registry.register(Registry.ITEM, id, item);
		ITEMS.add(item);
		return item;
	}

	private static <T extends Item> T register(String id, T item)
	{
		return register(Thuwumcraft.getId(id), item);
	}

	private static BlockItem register(Block block, FabricItemSettings settings)
	{
		BlockItem item = Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), new BlockItem(block, settings));
		ITEMS.add(item);
		return item;
	}

	private static FabricItemSettings defaultSettings()
	{
		return new FabricItemSettings().group(ALCHEMY_MOD_ITEM_GROUP);
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
			if(!(item instanceof BlockItem) && !(item instanceof AspectItem))
			{
				item.appendStacks(ALCHEMY_MOD_ITEM_GROUP, (DefaultedList<ItemStack>)stack);
			}
		});
		Spell.REGISTRY.getSpells().forEach(spell -> {
			ItemStack focus = new ItemStack(WAND_FOCUS, 1);
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(focus);
			provider.addAbility(new WandFocusAbilityImpl(spell, focus));
			stack.add(focus);
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
		stack.add(GLASS_PHIAL.getDefaultStack());
	}

	private static ItemStack getDisplayIcon()
	{
		return GOGGLES.getDefaultStack();
	}
}
