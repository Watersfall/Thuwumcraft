package net.watersfall.alchemy.api.aspect;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.item.AspectItems;
import net.watersfall.alchemy.item.AlchemyItems;
import net.watersfall.alchemy.item.GlassPhialItem;

import java.util.HashMap;

public class Aspects
{
	public static final HashMap<Identifier, Aspect> ASPECTS = new HashMap<>();
	public static final HashMap<Aspect, GlassPhialItem> ASPECT_TO_PHIAL = new HashMap<>();
	public static final HashMap<Aspect, Item> ASPECT_TO_CRYSTAL = new HashMap<>();

	public static final Aspect AIR = new Aspect(AlchemyMod.getId("air"), 0xffff00, AspectItems.AIR);
	public static final Aspect EARTH = new Aspect(AlchemyMod.getId("earth"), 0x00ff00, AspectItems.EARTH);
	public static final Aspect WATER = new Aspect(AlchemyMod.getId("water"), 0x0000ff, AspectItems.WATER);
	public static final Aspect FIRE = new Aspect(AlchemyMod.getId("fire"), 0xff0000, AspectItems.FIRE);

	public static Aspect register(Identifier id, Aspect aspect)
	{
		ASPECTS.put(id, aspect);
		GlassPhialItem item = new GlassPhialItem(aspect);
		Item crystal = new Item(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		ASPECT_TO_PHIAL.put(aspect, item);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "phial/" + aspect.getId().getPath()), item);
		Registry.register(Registry.ITEM, new Identifier(aspect.getId().getNamespace(), "crystal/" + aspect.getId().getPath()), crystal);
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

	public static Aspect getAspectById(Identifier id)
	{
		return ASPECTS.getOrDefault(id, Aspect.EMPTY);
	}
}
