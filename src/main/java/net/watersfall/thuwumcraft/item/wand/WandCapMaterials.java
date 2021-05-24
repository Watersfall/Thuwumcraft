package net.watersfall.thuwumcraft.item.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.AlchemyMod;
import net.watersfall.thuwumcraft.item.AlchemyItems;

import java.util.function.Supplier;

public enum WandCapMaterials implements WandCapMaterial
{
	IRON(AlchemyMod.getId("iron"), CapRechargeType.ENVIRONMENTAL, 0xA5A5A5, () -> new ItemStack(AlchemyItems.IRON_CAP));

	private final CapRechargeType rechargeType;
	private final Identifier id;
	private final int color;
	private final Supplier<ItemStack> stack;

	WandCapMaterials(Identifier id, CapRechargeType rechargeType, int color, Supplier<ItemStack> stack)
	{
		this.id = id;
		this.rechargeType = rechargeType;
		this.color = color;
		this.stack = stack;
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

	@Override
	public ItemStack getItemStack()
	{
		return stack.get().copy();
	}
}
