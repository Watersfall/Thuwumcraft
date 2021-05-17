package net.watersfall.alchemy.item.wand;

import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;

public enum WandCapMaterials implements WandCapMaterial
{
	IRON(AlchemyMod.getId("iron"), CapRechargeType.ENVIRONMENTAL, 0xA5A5A5);

	private final CapRechargeType rechargeType;
	private final Identifier id;
	private final int color;

	WandCapMaterials(Identifier id, CapRechargeType rechargeType, int color)
	{
		this.id = id;
		this.rechargeType = rechargeType;
		this.color = color;
		WandCapMaterial.REGISTRY.register(id, this);
	}

	@Override
	public CapRechargeType getRechargeType()
	{
		return this.rechargeType;
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
