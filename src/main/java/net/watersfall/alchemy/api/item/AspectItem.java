package net.watersfall.alchemy.api.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.aspect.Aspects;

public class AspectItem extends Item
{
	private static final ItemGroup ASPECT_GROUP = FabricItemGroupBuilder.build(AlchemyMod.getId("aspect_group"), () -> Aspects.AIR.getItem().getDefaultStack());

	public AspectItem()
	{
		super(new FabricItemSettings().maxCount(1).group(ASPECT_GROUP));
	}
}
