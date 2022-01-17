package net.watersfall.thuwumcraft.util;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemHelper
{
	public static Item getItem(String id)
	{
		return Registry.ITEM.get(new Identifier(id));
	}
}
