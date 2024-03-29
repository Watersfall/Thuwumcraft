package net.watersfall.thuwumcraft.api.aspect;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class AspectStack
{
	/**
	 * An ItemStack with an Aspect instead of an Item
	 */
	public static final AspectStack EMPTY = new AspectStack(Aspect.EMPTY, 1);

	private final Aspect aspect;
	private int count;

	public AspectStack(Aspect aspect, int amount)
	{
		this.aspect = aspect;
		this.count = amount;
	}

	public AspectStack(NbtCompound tag)
	{
		this.aspect = Aspects.getAspectById(Identifier.tryParse(tag.getString("aspect")));
		this.count = tag.getInt("count");
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}

	public int getCount()
	{
		return this.count;
	}

	public int setCount(int amount)
	{
		this.count = amount;
		return this.count;
	}

	public int increment(int amount)
	{
		return setCount(getCount() + amount);
	}

	public int decrement(int amount)
	{
		return increment(-amount);
	}

	public boolean isEmpty()
	{
		return this.aspect == null || this.aspect == Aspect.EMPTY || this.count < 1;
	}

	public AspectStack copy()
	{
		return new AspectStack(this.getAspect(), this.getCount());
	}

	public NbtCompound toNbt()
	{
		NbtCompound tag = new NbtCompound();
		tag.putString("aspect", aspect.getId().toString());
		tag.putInt("count", count);
		return tag;
	}

	@Override
	public boolean equals(Object other)
	{
		if(other != null)
		{
			if(this == other)
			{
				return true;
			}
			else if(other instanceof AspectStack stack)
			{
				return stack.aspect == this.aspect && stack.count == this.count;
			}
		}
		return false;
	}

	public ItemStack toItemStack()
	{
		return new ItemStack(aspect.getItem(), getCount());
	}
}