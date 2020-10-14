package com.watersfall.poisonedweapons.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class AlchemyModItems
{
    public static final WitchySpoonItem WITCHY_SPOON_ITEM = new WitchySpoonItem();
    public static final Item THROW_BOTTLE = new Item(new FabricItemSettings().maxCount(64).group(ItemGroup.BREWING));
}
