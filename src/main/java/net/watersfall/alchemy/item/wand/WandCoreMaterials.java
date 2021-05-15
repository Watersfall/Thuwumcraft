package net.watersfall.alchemy.item.wand;

public enum WandCoreMaterials implements WandCoreMaterial
{
	WOOD(50);

	private final double maxVis;

	WandCoreMaterials(double maxVis)
	{
		this.maxVis = maxVis;
	}

	@Override
	public double getMaxVis()
	{
		return maxVis;
	}
}
