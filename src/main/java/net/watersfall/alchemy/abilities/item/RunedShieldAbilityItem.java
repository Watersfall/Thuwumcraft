package net.watersfall.alchemy.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.common.RunedShieldAbility;

public class RunedShieldAbilityItem implements RunedShieldAbility<ItemStack>
{
	private CompoundTag tag;
	public static final Identifier ID = AlchemyMod.getId("runed_shield_ability");

	public RunedShieldAbilityItem(CompoundTag tag)
	{
		this.fromNbt(tag);
	}

	public RunedShieldAbilityItem(int shield, int max, int recharge)
	{
		this.tag = new CompoundTag();
		this.setShieldAmount(shield);
		this.setMaxAmount(max);
		this.setRechargeRate(recharge);
	}

	@Override
	public Identifier getId()
	{
		return ID;
	}

	@Override
	public CompoundTag toNbt(CompoundTag tag)
	{
		return this.tag;
	}

	@Override
	public void fromNbt(CompoundTag tag)
	{
		this.tag = tag;
	}

	@Override
	public int getShieldAmount()
	{
		return this.tag.getInt("shield_amount");
	}

	@Override
	public int getMaxAmount()
	{
		return this.tag.getInt("max_shield_amount");
	}

	@Override
	public int getRechargeRate()
	{
		return this.tag.getInt("recharge_rate");
	}

	@Override
	public void setShieldAmount(int amount)
	{
		this.tag.putInt("shield_amount", amount);
	}

	@Override
	public void setMaxAmount(int amount)
	{
		this.tag.putInt("max_shield_amount", amount);
	}

	@Override
	public void setRechargeRate(int amount)
	{
		this.tag.putInt("recharge_rate", amount);
	}
}
