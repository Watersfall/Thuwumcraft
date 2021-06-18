package net.watersfall.thuwumcraft.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.item.BerserkerWeapon;

public class BerserkerWeaponImpl implements BerserkerWeapon
{
	private int experience = 0;

	public BerserkerWeaponImpl() { }

	public BerserkerWeaponImpl(NbtCompound nbt, ItemStack stack)
	{
		this.fromNbt(nbt, stack);
	}

	@Override
	public Identifier getId()
	{
		return ID;
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, ItemStack stack)
	{
		tag.putInt("xp", experience);
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, ItemStack stack)
	{
		this.experience = tag.getInt("xp");
	}

	@Override
	public int getExperience()
	{
		return experience;
	}

	@Override
	public void setExperience(int xp)
	{
		experience = xp;
	}
}
