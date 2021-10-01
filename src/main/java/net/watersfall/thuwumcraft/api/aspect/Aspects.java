package net.watersfall.thuwumcraft.api.aspect;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.block.BuddingElementalBlock;
import net.watersfall.thuwumcraft.block.DecorativeStaffBlock;
import net.watersfall.thuwumcraft.block.ElementalBlock;
import net.watersfall.thuwumcraft.block.ElementalClusterBlock;
import net.watersfall.thuwumcraft.item.CrystalItem;
import net.watersfall.thuwumcraft.item.DecorativeStaffBlockItem;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.HashMap;

public class Aspects
{
	public static final HashMap<Identifier, Aspect> ASPECTS = new HashMap<>();
	public static final HashMap<Aspect, Item> ASPECT_TO_CRYSTAL = new HashMap<>();
	public static final HashMap<Aspect, ElementalClusterBlock> ASPECT_TO_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, ElementalClusterBlock> ASPECT_TO_SMALL_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, ElementalClusterBlock> ASPECT_TO_MEDIUM_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, ElementalClusterBlock> ASPECT_TO_LARGE_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, ElementalBlock> ASPECT_TO_CLUSTER_BLOCK = new HashMap<>();
	public static final HashMap<Aspect, BuddingElementalBlock> ASPECT_TO_BUDDING_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, DecorativeStaffBlock> DECORATIVE_STAFF_BLOCKS = new HashMap<>();
	public static final HashMap<Aspect, DecorativeStaffBlockItem> DECORATIVE_STAFF_ITEMS = new HashMap<>();

	public static final Aspect AIR = new Aspect(Thuwumcraft.getId("air"), 0xEAE249);
	public static final Aspect EARTH = new Aspect(Thuwumcraft.getId("earth"), 0x4E3C00);
	public static final Aspect WATER = new Aspect(Thuwumcraft.getId("water"), 0x4BAFF6);
	public static final Aspect FIRE = new Aspect(Thuwumcraft.getId("fire"), 0xFF7D00);
	public static final Aspect ORDER = new Aspect(Thuwumcraft.getId("order"), 0xFFF200);
	public static final Aspect DISORDER = new Aspect(Thuwumcraft.getId("disorder"), 0x55457A);
	public static final Aspect METAL = new Aspect(Thuwumcraft.getId("metal"), 0xdddddd, new Aspect[]{EARTH, ORDER});

	public static Aspect register(Identifier id, Aspect aspect)
	{
		ASPECTS.put(id, aspect);
		Item crystal = new CrystalItem(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP), aspect);
		ASPECT_TO_CRYSTAL.put(aspect, crystal);
		DecorativeStaffBlock staff = new DecorativeStaffBlock(aspect, FabricBlockSettings.copyOf(Blocks.TALL_GRASS).sounds(BlockSoundGroup.WOOD).luminance(15));
		DECORATIVE_STAFF_BLOCKS.put(aspect, staff);
		DecorativeStaffBlockItem staffItem = new DecorativeStaffBlockItem(aspect, staff, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
		DECORATIVE_STAFF_ITEMS.put(aspect, staffItem);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "aspect/" + aspect.getId().getPath()), aspect.getItem());
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "crystal/" + aspect.getId().getPath()), crystal);
		Registry.register(Registry.BLOCK, new Identifier(aspect.getId().getNamespace(), "staff/decorative/" + aspect.getId().getPath()), staff);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "staff/decorative/" + aspect.getId().getPath()), staffItem);
		if(aspect.isPrimitive())
		{
			registerClusters(aspect);
		}
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
		{
			ColorProviderRegistry.ITEM.register(
					((stack, tintIndex) -> aspect.getColor()),
					crystal
			);
			ColorProviderRegistry.ITEM.register(
					((stack, tintIndex) -> tintIndex == 1 ? aspect.getColor() : -1),
					staffItem
			);
			ColorProviderRegistry.BLOCK.register(
					(state, world, pos, tintIndex) -> ((DecorativeStaffBlock)state.getBlock()).getAspect().getColor(),
					staff
			);
			ColorProviderRegistry.ITEM.register((stack, tintIndex) -> aspect.getColor(), aspect.getItem());
			BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), staff);
		}
		return aspect;
	}

	private static void registerClusters(Aspect aspect)
	{
		ElementalClusterBlock cluster = new ElementalClusterBlock(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER).luminance(9), aspect);
		ElementalClusterBlock large = new ElementalClusterBlock(5, 3, FabricBlockSettings.copyOf(Blocks.LARGE_AMETHYST_BUD).luminance(7), aspect);
		ElementalClusterBlock medium = new ElementalClusterBlock(4, 3, FabricBlockSettings.copyOf(Blocks.MEDIUM_AMETHYST_BUD).luminance(5), aspect);
		ElementalClusterBlock small = new ElementalClusterBlock(3, 3, FabricBlockSettings.copyOf(Blocks.SMALL_AMETHYST_BUD).luminance(4), aspect);
		ElementalBlock block = new ElementalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK), aspect);
		BuddingElementalBlock budding = new BuddingElementalBlock(FabricBlockSettings.copyOf(Blocks.BUDDING_AMETHYST), aspect);
		BlockItem clusterItem = new BlockItem(cluster, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem largeItem = new BlockItem(large, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem mediumItem = new BlockItem(medium, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem smallItem = new BlockItem(small, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem blockItem = new BlockItem(block, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem buddingItem = new BlockItem(budding, new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
		ASPECT_TO_CLUSTER.put(aspect, cluster);
		ASPECT_TO_LARGE_CLUSTER.put(aspect, large);
		ASPECT_TO_MEDIUM_CLUSTER.put(aspect, medium);
		ASPECT_TO_SMALL_CLUSTER.put(aspect, small);
		ASPECT_TO_CLUSTER_BLOCK.put(aspect, block);
		ASPECT_TO_BUDDING_CLUSTER.put(aspect, budding);
		Registry.register(Registry.BLOCK, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_cluster"), cluster);
		Registry.register(Registry.BLOCK, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_large"), large);
		Registry.register(Registry.BLOCK, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_medium"), medium);
		Registry.register(Registry.BLOCK, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_small"), small);
		Registry.register(Registry.BLOCK, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_block"), block);
		Registry.register(Registry.BLOCK, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_budding"), budding);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_cluster"), clusterItem);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_large"), largeItem);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_medium"), mediumItem);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_small"), smallItem);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_block"), blockItem);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "cluster/" + aspect.getId().getPath() + "_crystal_budding"), buddingItem);
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
		{
			BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), cluster, large, medium, small);
		}
	}

	public static Aspect getAspectById(Identifier id)
	{
		return ASPECTS.getOrDefault(id, Aspect.EMPTY);
	}
}
