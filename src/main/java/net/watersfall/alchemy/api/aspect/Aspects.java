package net.watersfall.alchemy.api.aspect;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.item.AspectItems;
import net.watersfall.alchemy.block.BuddingElementalBlock;
import net.watersfall.alchemy.block.ElementalBlock;
import net.watersfall.alchemy.block.ElementalClusterBlock;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.CrystalItem;
import net.watersfall.alchemy.item.GlassPhialItem;

import java.util.HashMap;

public class Aspects
{
	public static final HashMap<Identifier, Aspect> ASPECTS = new HashMap<>();
	public static final HashMap<Aspect, GlassPhialItem> ASPECT_TO_PHIAL = new HashMap<>();
	public static final HashMap<Aspect, Item> ASPECT_TO_CRYSTAL = new HashMap<>();
	public static final HashMap<Aspect, ElementalClusterBlock> ASPECT_TO_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, ElementalClusterBlock> ASPECT_TO_SMALL_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, ElementalClusterBlock> ASPECT_TO_MEDIUM_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, ElementalClusterBlock> ASPECT_TO_LARGE_CLUSTER = new HashMap<>();
	public static final HashMap<Aspect, ElementalBlock> ASPECT_TO_CLUSTER_BLOCK = new HashMap<>();
	public static final HashMap<Aspect, BuddingElementalBlock> ASPECT_TO_BUDDING_CLUSTER = new HashMap<>();

	public static final Aspect AIR = new Aspect(AlchemyMod.getId("air"), 0xffff00, AspectItems.AIR);
	public static final Aspect EARTH = new Aspect(AlchemyMod.getId("earth"), 0x00ff00, AspectItems.EARTH);
	public static final Aspect WATER = new Aspect(AlchemyMod.getId("water"), 0x0000ff, AspectItems.WATER);
	public static final Aspect FIRE = new Aspect(AlchemyMod.getId("fire"), 0xe69836, AspectItems.FIRE);
	public static final Aspect ORDER = new Aspect(AlchemyMod.getId("order"), 0xdddddd, AspectItems.ORDER);
	public static final Aspect DISORDER = new Aspect(AlchemyMod.getId("disorder"), 0x222222, AspectItems.DISORDER);

	public static Aspect register(Identifier id, Aspect aspect)
	{
		ASPECTS.put(id, aspect);
		GlassPhialItem item = new GlassPhialItem(aspect);
		Item crystal = new CrystalItem(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP), aspect);
		ASPECT_TO_PHIAL.put(aspect, item);
		ASPECT_TO_CRYSTAL.put(aspect, crystal);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "phial/" + aspect.getId().getPath()), item);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "crystal/" + aspect.getId().getPath()), crystal);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "aspect/" + aspect.getId().getPath()), aspect.getItem());
		registerClusters(aspect);
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
		{
			ColorProviderRegistry.ITEM.register(
					((stack, tintIndex) -> {
						if(tintIndex == 0)
						{
							return ((GlassPhialItem)stack.getItem()).getAspect().getColor();
						}
						return -1;
					}),
					item
			);
			ColorProviderRegistry.ITEM.register(
					((stack, tintIndex) -> aspect.getColor()),
					crystal
			);
		}
		return aspect;
	}

	private static void registerClusters(Aspect aspect)
	{
		ElementalClusterBlock cluster = new ElementalClusterBlock(7, 3, FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER), aspect);
		ElementalClusterBlock large = new ElementalClusterBlock(5, 3, FabricBlockSettings.copyOf(Blocks.LARGE_AMETHYST_BUD), aspect);
		ElementalClusterBlock medium = new ElementalClusterBlock(4, 3, FabricBlockSettings.copyOf(Blocks.MEDIUM_AMETHYST_BUD), aspect);
		ElementalClusterBlock small = new ElementalClusterBlock(3, 3, FabricBlockSettings.copyOf(Blocks.SMALL_AMETHYST_BUD), aspect);
		ElementalBlock block = new ElementalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK), aspect);
		BuddingElementalBlock budding = new BuddingElementalBlock(FabricBlockSettings.copyOf(Blocks.BUDDING_AMETHYST), aspect);
		BlockItem clusterItem = new BlockItem(cluster, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem largeItem = new BlockItem(large, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem mediumItem = new BlockItem(medium, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem smallItem = new BlockItem(small, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem blockItem = new BlockItem(block, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		BlockItem buddingItem = new BlockItem(budding, new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
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
