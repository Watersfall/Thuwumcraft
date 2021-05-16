package net.watersfall.alchemy.item.wand;

import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;

public enum WandCoreMaterials implements WandCoreMaterial
{
	WOOD(AlchemyMod.getId("wood"), 50);

	private final double maxVis;
	private final Identifier id;

	WandCoreMaterials(Identifier id, double maxVis)
	{
		this.id = id;
		this.maxVis = maxVis;
		WandCoreMaterial.REGISTRY.register(id, this);
	}

	@Override
	public double getMaxVis()
	{
		return maxVis;
	}

	@Override
	public Identifier getId()
	{
		return id;
	}


}
