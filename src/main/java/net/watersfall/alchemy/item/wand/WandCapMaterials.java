package net.watersfall.alchemy.item.wand;

public enum WandCapMaterials implements WandCapMaterial
{
	IRON(CapRechargeType.ENVIRONMENTAL);

	private final CapRechargeType rechargeType;

	WandCapMaterials(CapRechargeType rechargeType)
	{
		this.rechargeType = rechargeType;
	}

	@Override
	public CapRechargeType getRechargeType()
	{
		return this.rechargeType;
	}
}
