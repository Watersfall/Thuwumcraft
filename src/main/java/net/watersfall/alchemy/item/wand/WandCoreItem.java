package net.watersfall.alchemy.item.wand;

import net.minecraft.item.Item;

public class WandCoreItem extends Item
{
	private final WandCoreMaterial material;

	public WandCoreItem(WandCoreMaterial material, Settings settings)
	{
		super(settings);
		this.material = material;
	}

	public WandCoreMaterial getMaterial()
	{
		return this.material;
	}
}
