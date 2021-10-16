package net.watersfall.thuwumcraft.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.abilities.item.WandFocusAbilityImpl;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.api.item.AspectItem;
import net.watersfall.thuwumcraft.item.*;
import net.watersfall.thuwumcraft.item.armor.AlchemistArmorItem;
import net.watersfall.thuwumcraft.item.armor.AlchemyArmorMaterials;
import net.watersfall.thuwumcraft.item.armor.SpeedBootsItem;
import net.watersfall.thuwumcraft.item.tool.*;
import net.watersfall.thuwumcraft.item.wand.*;
import net.watersfall.thuwumcraft.spell.Spell;
import net.watersfall.thuwumcraft.spell.SpellAction;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.ArrayList;
import java.util.List;

public class ThuwumcraftItems
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
	public static final SpecialShovelItem SPECIAL_SHOVEL_ITEM;
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
	public static final ArmorItem FORTRESS_BOOTS;
	public static final SpeedBootsItem BOOTS_OF_BLINDING_SPEED;
	public static final BlockItem ARCANE_LAMP_ITEM;
	public static final BucketItem DIMENSIONAL_FLUID_BUCKET;
	public static final EyeOfTheUnknownItem EYE_OF_THE_UNKNOWN_ITEM;
	public static final BlockItem DEEPSLATE_GRASS;
	public static final BlockItem GLOWING_DEEPSLATE;
	public static final CastingStaffItem SNOW_STAFF;
	public static final CastingStaffItem ICE_STAFF;
	public static final ContinuousCastingStaffItem WATER_STAFF;
	public static final Item ICE_PROJECTILE_ITEM;
	public static final ContinuousCastingStaffItem FIRE_STAFF;
	public static final CastingStaffItem SAND_STAFF;
	public static final PortableHoleWand WAND_OF_HOLES;
	public static final WandItem WAND;
	public static final WandFocusItem WAND_FOCUS;
	public static final WandCoreItem WOOD_CORE;
	public static final WandCoreItem GREATWOOD_CORE;
	public static final WandCoreItem SILVERWOOD_CORE;
	public static final WandCapItem IRON_CAP;
	public static final WandCapItem BRASS_CAP;
	public static final WandCapItem THUWUMIUM_CAP;
	public static final BlockItem SILVERWOOD_SAPLING;
	public static final AlchemyBindingItem ALCHEMY_BINDING;
	public static final BlockItem ARCANE_STONE;
	public static final BlockItem ARCANE_STONE_SLAB;
	public static final ArcaneRuneItem AIR_RUNE, WATER_RUNE, FIRE_RUNE, EARTH_RUNE, ORDER_RUNE, DISORDER_RUNE;
	public static final BlockItem ARCANE_SEAL;
	public static final BlockItem GREATWOOD_LOG;
	public static final BlockItem SILVERWOOD_LOG;
	public static final BlockItem SPAWNER_FRAME;
	public static final BlockItem WAND_WORKBENCH;
	public static final Item GOGGLES_OVERLAY_ITEM;
	public static final List<Item> ITEMS;

	static
	{
		ITEMS = new ArrayList<>();
		ALCHEMY_MOD_ITEM_GROUP = FabricItemGroupBuilder.create(Thuwumcraft
				.getId("alchemy_mod_group"))
				.icon(ThuwumcraftItems::getDisplayIcon)
				.appendItems(ThuwumcraftItems::getStacks)
				.build();
		WITCHY_SPOON_ITEM = register(Thuwumcraft.getId("witchy_spoon"), new WitchySpoonItem());
		THROW_BOTTLE = register(Thuwumcraft.getId("throw_bottle"), new SpecialGlassBottleItem(new FabricItemSettings().maxCount(64).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), Items.SPLASH_POTION::getDefaultStack));
		LINGERING_BOTTLE = register(Thuwumcraft.getId("lingering_bottle"), new SpecialGlassBottleItem(new FabricItemSettings().maxCount(64).group(ALCHEMY_MOD_ITEM_GROUP), Items.LINGERING_POTION::getDefaultStack));
		LADLE_ITEM = register(Thuwumcraft.getId("ladle"), new LadleItem());
		APOTHECARY_GUIDE = register(Thuwumcraft.getId("apothecary_guide_book"), new ApothecaryGuideItem());
		PEDESTAL_ITEM = register(Thuwumcraft.getId("pedestal"), new BlockItem(ThuwumcraftBlocks.PEDESTAL_BLOCK, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP)));
		MAGICAL_COAL_TIER_0 =  register(Thuwumcraft.getId("magical_coal_0"), new MagicalCoalItem(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), 0));
		MAGICAL_COAL_TIER_1 =register(Thuwumcraft.getId("magical_coal_1"), new MagicalCoalItem(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), 1));
		MAGICAL_COAL_TIER_2 =register(Thuwumcraft.getId("magical_coal_2"), new MagicalCoalItem(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), 2));
		MAGIC_DUST = register(Thuwumcraft.getId("magic_dust"), new MagicDustItem());
		SPECIAL_PICKAXE_ITEM =register(Thuwumcraft.getId("magic_pickaxe"), new SpecialPickaxeItem());
		SPECIAL_AXE_ITEM = register(Thuwumcraft.getId("magic_axe"), new SpecialAxeItem());
		SPECIAL_HOE_ITEM = register(Thuwumcraft.getId("magic_hoe"), new SpecialHoeItem());
		SPECIAL_SWORD_ITEM = register(Thuwumcraft.getId("magic_sword"), new SpecialSwordItem());
		SPECIAL_BATTLEAXE_ITEM = register(Thuwumcraft.getId("magic_battleaxe"), new SpecialBattleaxeItem());
		SPECIAL_BOW_ITEM = register(Thuwumcraft.getId("magic_bow"), new SpecialBowItem(defaultSettings().maxCount(1)));
		SPECIAL_SHOVEL_ITEM = register(Thuwumcraft.getId("magic_shovel"), new SpecialShovelItem());
		JAR_ITEM = register(Thuwumcraft.getId("jar"), new BlockItem(ThuwumcraftBlocks.JAR_BLOCK, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP)));
		EMPTY_PHIAL_ITEM = register(Thuwumcraft.getId("phial"), new GlassPhialItem());
		PHIAL_SHELF_ITEM = register(Thuwumcraft.getId("phial_shelf"), new BlockItem(ThuwumcraftBlocks.PHIAL_SHELF_BLOCK, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP)));
		RESEARCH_BOOK_ITEM = register(Thuwumcraft.getId("research_book"), new ResearchBookItem(defaultSettings()));
		ASPECT_PIPE_ITEM = register(Thuwumcraft.getId("aspect_pipe"), new BlockItem(ThuwumcraftBlocks.ASPECT_PIPE_BLOCK, defaultSettings()));
		NECROMANCY_SKULL = register(Thuwumcraft.getId("necromancy_skull"), new Item(defaultSettings()));
		NECROMANCY_ARM = register(Thuwumcraft.getId("necromancy_arm"), new Item(defaultSettings()));
		NECROMANCY_LEG = register(Thuwumcraft.getId("necromancy_leg"), new Item(defaultSettings()));
		NECROMANCY_HEART = register(Thuwumcraft.getId("necromancy_heart"), new Item(defaultSettings()));
		NECROMANCY_RIBCAGE = register(Thuwumcraft.getId("necromancy_ribcage"), new Item(defaultSettings()));
		CUSTOM_SPAWNER = register(Thuwumcraft.getId("custom_spawner"), new CustomMobSpawnerItem(ThuwumcraftBlocks.CUSTOM_SPAWNER, defaultSettings()));
		SPAWN_EGG_BASE = register(Thuwumcraft.getId("spawn_egg_base"), new Item(defaultSettings()));
		NEKOMANCY_TABLE = register(Thuwumcraft.getId("nekomancy_table"), new BlockItem(ThuwumcraftBlocks.NEKOMANCY_TABLE, defaultSettings()));
		CRAFTING_HOPPER = register(Thuwumcraft.getId("crafting_hopper"), new BlockItem(ThuwumcraftBlocks.CRAFTING_HOPPER, defaultSettings()));
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
		VIS_LIQUIFIER = register(Thuwumcraft.getId("vis_liquifier"), new BlockItem(ThuwumcraftBlocks.VIS_LIQUIFIER, defaultSettings()));
		ASPECT_CRAFTING_BLOCK = register(Thuwumcraft.getId("aspect_crafting_block"), new BlockItem(ThuwumcraftBlocks.ASPECT_CRAFTING_BLOCK, defaultSettings()));
		BRASS_INGOT = register(Thuwumcraft.getId("brass_ingot"), new Item(defaultSettings()));
		POTION_SPRAYER_ITEM = register(Thuwumcraft.getId("potion_sprayer"), new BlockItem(ThuwumcraftBlocks.POTION_SPRAYER_BLOCK, defaultSettings()));
		ESSENTIA_SMELTERY_ITEM = register(Thuwumcraft.getId("essentia_smeltery"), new BlockItem(ThuwumcraftBlocks.ESSENTIA_SMELTERY, defaultSettings()));
		ESSENTIA_REFINERY = register(Thuwumcraft.getId("essentia_refinery"), new BlockItem(ThuwumcraftBlocks.ESSENTIA_REFINERY_BLOCK, defaultSettings()));
		ALCHEMIST_HOOD = register(Thuwumcraft.getId("alchemist_hood"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, defaultSettings(), 0.15F));
		ALCHEMIST_ROBE = register(Thuwumcraft.getId("alchemist_robe"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.CHEST, defaultSettings(), 0.15F));
		ALCHEMIST_LEGGINGS = register(Thuwumcraft.getId("alchemist_leggings"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.LEGS, defaultSettings(), 0.15F));
		ALCHEMIST_SHOES = register(Thuwumcraft.getId("alchemist_shoes"), new AlchemistArmorItem(ArmorMaterials.LEATHER, EquipmentSlot.FEET, defaultSettings(), 0.15F));
		FORTRESS_HELMET = register(Thuwumcraft.getId("fortress_helmet"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.HEAD, defaultSettings()));
		FORTRESS_CHESTPLATE = register(Thuwumcraft.getId("fortress_chestplate"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.CHEST, defaultSettings()));
		FORTRESS_LEGGINGS = register(Thuwumcraft.getId("fortress_leggings"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.LEGS, defaultSettings()));
		FORTRESS_BOOTS = register(Thuwumcraft.getId("fortress_boots"), new ArmorItem(AlchemyArmorMaterials.FORTRESS, EquipmentSlot.FEET, defaultSettings()));
		BOOTS_OF_BLINDING_SPEED = register(Thuwumcraft.getId("boots_of_blinding_speed"), new SpeedBootsItem(AlchemyArmorMaterials.MAGIC, 0.5F));
		ARCANE_LAMP_ITEM = register(Thuwumcraft.getId("arcane_lamp"), new BlockItem(ThuwumcraftBlocks.ARCANE_LAMP_BLOCK, defaultSettings()));
		DIMENSIONAL_FLUID_BUCKET = register(Thuwumcraft.getId("dimensional_fluid_bucket"), new BucketItem(ThuwumcraftFluids.DIMENSIONAL_STILL, defaultSettings().maxCount(1)));
		EYE_OF_THE_UNKNOWN_ITEM = register(Thuwumcraft.getId("eye_of_the_unknown"), new EyeOfTheUnknownItem(defaultSettings().rarity(Rarity.EPIC)));
		DEEPSLATE_GRASS = register(Thuwumcraft.getId("deepslate_grass"), new BlockItem(ThuwumcraftBlocks.DEEPSLATE_GRASS, defaultSettings()));
		GLOWING_DEEPSLATE = register(Thuwumcraft.getId("glowing_deepslate"), new BlockItem(ThuwumcraftBlocks.GLOWING_DEEPSLATE, defaultSettings()));
		ICE_STAFF = register(Thuwumcraft.getId("ice_staff"), new CastingStaffItem(defaultSettings().maxCount(1), SpellAction.ICE, 10, 20));
		SNOW_STAFF = register(Thuwumcraft.getId("snow_staff"), new CastingStaffItem(defaultSettings().maxCount(1), SpellAction.SNOW, 5, 10));
		WATER_STAFF = register(Thuwumcraft.getId("water_staff"), new ContinuousCastingStaffItem(defaultSettings().maxCount(1), SpellAction.WATER, 10, 1));
		FIRE_STAFF = register(Thuwumcraft.getId("fire_staff"), new ContinuousCastingStaffItem(defaultSettings().maxCount(1), SpellAction.FIRE, 10, 1));
		ICE_PROJECTILE_ITEM = register(Thuwumcraft.getId("ice_projectile"), new Item(new FabricItemSettings()));
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
		SILVERWOOD_SAPLING = register(Thuwumcraft.getId("silverwood_sapling"), new BlockItem(ThuwumcraftBlocks.SILVERWOOD_SAPLING, defaultSettings()));
		ALCHEMY_BINDING = register(Thuwumcraft.getId("alchemy_binding"), new AlchemyBindingItem(defaultSettings()));
		ARCANE_STONE = register(Thuwumcraft.getId("arcane_stone"), new BlockItem(ThuwumcraftBlocks.ARCANE_STONE, defaultSettings()));
		ARCANE_STONE_SLAB = register(Thuwumcraft.getId("arcane_stone_slab"), new BlockItem(ThuwumcraftBlocks.ARCANE_STONE_SLAB, defaultSettings()));
		AIR_RUNE = register(Thuwumcraft.getId("air_rune"), new ArcaneRuneItem(Aspects.AIR, defaultSettings().maxCount(1)));
		EARTH_RUNE = register(Thuwumcraft.getId("earth_rune"), new ArcaneRuneItem(Aspects.EARTH, defaultSettings().maxCount(1)));
		FIRE_RUNE = register(Thuwumcraft.getId("fire_rune"), new ArcaneRuneItem(Aspects.FIRE, defaultSettings().maxCount(1)));
		WATER_RUNE = register(Thuwumcraft.getId("water_rune"), new ArcaneRuneItem(Aspects.WATER, defaultSettings().maxCount(1)));
		ORDER_RUNE = register(Thuwumcraft.getId("order_rune"), new ArcaneRuneItem(Aspects.ORDER, defaultSettings().maxCount(1)));
		DISORDER_RUNE = register(Thuwumcraft.getId("disorder_rune"), new ArcaneRuneItem(Aspects.DISORDER, defaultSettings().maxCount(1)));
		ARCANE_SEAL = register(ThuwumcraftBlocks.ARCANE_SEAL, defaultSettings());
		GREATWOOD_LOG = register(ThuwumcraftBlocks.GREATWOOD_LOG, defaultSettings());
		SILVERWOOD_LOG = register(ThuwumcraftBlocks.SILVERWOOD_LOG, defaultSettings());
		SPAWNER_FRAME = register(ThuwumcraftBlocks.SPAWNER_FRAME, defaultSettings());
		WAND_WORKBENCH = register(ThuwumcraftBlocks.WAND_WORKBENCH, defaultSettings());
		GOGGLES_OVERLAY_ITEM = register(Thuwumcraft.getId("goggles_overlay_item"), new Item(new FabricItemSettings()));
	}

	private static <T extends Item> T register(Identifier id, T item)
	{
		Registry.register(Registry.ITEM, id, item);
		ITEMS.add(item);
		return item;
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
		stack.add(EMPTY_PHIAL_ITEM.getDefaultStack());
	}

	private static ItemStack getDisplayIcon()
	{
		return MAGICAL_COAL_TIER_2.getDefaultStack();
	}
}
