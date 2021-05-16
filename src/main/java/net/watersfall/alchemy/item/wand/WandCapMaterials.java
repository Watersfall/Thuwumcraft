package net.watersfall.alchemy.item.wand;

import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;

public enum WandCapMaterials implements WandCapMaterial
{
	IRON(AlchemyMod.getId("iron"), CapRechargeType.ENVIRONMENTAL);

	private final CapRechargeType rechargeType;
	private final Identifier id;

	WandCapMaterials(Identifier id, CapRechargeType rechargeType)
	{
		this.id = id;
		this.rechargeType = rechargeType;
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
}
