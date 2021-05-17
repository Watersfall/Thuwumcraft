package net.watersfall.alchemy.item.wand;

import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;

public enum WandCoreMaterials implements WandCoreMaterial
{
	WOOD(AlchemyMod.getId("wood"), 50, 0x54321A);

	private final double maxVis;
	private final Identifier id;
	private final int color;

	WandCoreMaterials(Identifier id, double maxVis, int color)
	{
		this.id = id;
		this.maxVis = maxVis;
		this.color = color;
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

	@Override
	public int getColor()
	{
		return color;
	}


}
