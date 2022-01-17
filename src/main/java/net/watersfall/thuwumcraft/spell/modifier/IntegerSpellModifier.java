package net.watersfall.thuwumcraft.spell.modifier;

import net.minecraft.nbt.NbtCompound;

public class IntegerSpellModifier extends SpellModifier
{
	private int min;
	private int max;
	private int value;

	public IntegerSpellModifier(int min, int max, int value)
	{
		this.min = min;
		this.max = max;
		this.value = value;
	}

	public IntegerSpellModifier(NbtCompound nbt)
	{
		super(nbt);
	}

	@Override
	public void fromNbt(NbtCompound nbt)
	{
		this.min = nbt.getInt("min");
		this.max = nbt.getInt("max");
		this.value = nbt.getInt("value");
	}

	@Override
	public NbtCompound toNbt(NbtCompound nbt)
	{
		nbt.putInt("min", min);
		nbt.putInt("max", max);
		nbt.putInt("value", value);
		return nbt;
	}

	public int getMin()
	{
		return min;
	}

	public IntegerSpellModifier setMin(int min)
	{
		this.min = min;
		return this;
	}

	public int getMax()
	{
		return max;
	}

	public IntegerSpellModifier setMax(int max)
	{
		this.max = max;
		return this;
	}

	public int getValue()
	{
		return value;
	}

	public IntegerSpellModifier setValue(int value)
	{
		this.value = value;
		return this;
	}
}
