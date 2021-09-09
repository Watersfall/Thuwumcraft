package net.watersfall.thuwumcraft.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.api.abilities.common.AspectStorageAbility;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;

import java.util.List;

public class PhialStorageAbility implements AspectStorageAbility<ItemStack>
{
	private AspectStack stack;

	public PhialStorageAbility(AspectStack stack)
	{
		this.stack = stack;
	}

	public PhialStorageAbility(Aspect aspect)
	{
		this(new AspectStack(aspect, 64));
	}

	public PhialStorageAbility(Aspect aspect, int count)
	{
		this(new AspectStack(aspect, count));
	}

	public PhialStorageAbility(NbtCompound tag, ItemStack stack)
	{
		fromNbt(tag, stack);
	}

	@Override
	public int getSize()
	{
		return stack.getCount();
	}

	@Override
	public AspectStack getAspect(Aspect aspect)
	{
		if(aspect == stack.getAspect())
		{
			return stack;
		}
		return AspectStack.EMPTY;
	}

	@Override
	public AspectStack removeAspect(Aspect aspect, int count)
	{
		AspectStack stack = this.getAspect(aspect);
		if(count > stack.getCount())
		{
			count = stack.getCount();
		}
		stack.decrement(count);
		this.setAspect(stack);
		return new AspectStack(aspect, count);
	}


	@Override
	public void setAspect(AspectStack stack)
	{
		this.stack = stack;
	}

	@Override
	public void addAspect(AspectStack stack)
	{
		if(stack.getAspect() == this.stack.getAspect())
		{
			this.stack.increment(stack.getCount());
		}
		else
		{
			this.setAspect(stack);
		}
	}

	@Override
	public List<AspectStack> getAspects()
	{
		return List.of(stack);
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, ItemStack t)
	{
		return this.stack.toNbt();
	}

	@Override
	public void fromNbt(NbtCompound tag, ItemStack t)
	{
		this.stack = new AspectStack(tag);
	}

	@Override
	public boolean isEmpty()
	{
		return stack.isEmpty();
	}
}
