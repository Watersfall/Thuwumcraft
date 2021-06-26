package net.watersfall.thuwumcraft.api.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.Aspects;

/**
 *
 */
public class AspectItem extends Item
{
	private static final ItemGroup ASPECT_GROUP = FabricItemGroupBuilder.build(Thuwumcraft.getId("aspect_group"), () -> Aspects.AIR.getItem().getDefaultStack());

	private final Aspect aspect;

	public AspectItem(Aspect aspect)
	{
		super(new FabricItemSettings().maxCount(1).group(ASPECT_GROUP));
		this.aspect = aspect;
	}

	public Aspect getAspect()
	{
		return aspect;
	}
}
