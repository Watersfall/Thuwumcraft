package net.watersfall.thuwumcraft.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.common.RunedShieldAbility;

public class RunedShieldAbilityItem implements RunedShieldAbility<ItemStack>
{
	private NbtCompound tag;
	public static final Identifier ID = Thuwumcraft.getId("runed_shield_ability");

	public RunedShieldAbilityItem(NbtCompound tag, ItemStack stack)
	{
		this.fromNbt(tag, stack);
	}

	public RunedShieldAbilityItem(int shield, int max, int recharge)
	{
		this.tag = new NbtCompound();
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
	public NbtCompound toNbt(NbtCompound tag, ItemStack t)
	{
		return this.tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, ItemStack t)
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
