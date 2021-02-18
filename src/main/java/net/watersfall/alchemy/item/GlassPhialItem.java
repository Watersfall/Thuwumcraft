package net.watersfall.alchemy.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.watersfall.alchemy.api.aspect.Aspect;

public class GlassPhialItem extends Item
{
	private final Aspect aspect;

	public GlassPhialItem(Aspect aspect)
	{
		super(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		this.aspect = aspect;
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}
}
