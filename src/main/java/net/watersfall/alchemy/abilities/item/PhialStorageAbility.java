package net.watersfall.alchemy.abilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.common.AspectStorageAbility;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.aspect.Aspects;

import java.util.ArrayList;
import java.util.List;

public class PhialStorageAbility implements AspectStorageAbility<ItemStack>
{
	private CompoundTag tag;

	public PhialStorageAbility(AspectStack stack)
	{
		tag = new CompoundTag();
		tag.putInt(stack.getAspect().getId().toString(), stack.getCount());
	}

	public PhialStorageAbility(Aspect aspect)
	{
		this(new AspectStack(aspect, 64));
	}

	public PhialStorageAbility(Aspect aspect, int count)
	{
		this(new AspectStack(aspect, count));
	}

	public PhialStorageAbility(CompoundTag tag, ItemStack stack)
	{
		this.tag = tag;
	}

	@Override
	public CompoundTag getTag()
	{
		return this.tag;
	}

	@Override
	public int getSize()
	{
		return this.tag.getSize();
	}

	@Override
	public AspectStack getAspect(Aspect aspect)
	{
		if(this.tag.contains(aspect.getId().toString()))
		{
			return new AspectStack(aspect, this.tag.getInt(aspect.getId().toString()));
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
		this.tag.putInt(stack.getAspect().getId().toString(), stack.getCount());
	}

	@Override
	public void addAspect(AspectStack stack)
	{
		if(this.tag.contains(stack.getAspect().getId().toString()))
		{
			AspectStack stack1 = this.getAspect(stack.getAspect());
			stack.increment(stack1.getCount());
		}
		this.setAspect(stack);
	}

	@Override
	public List<AspectStack> getAspects()
	{
		List<AspectStack> list = new ArrayList<>();
		tag.getKeys().forEach((key) -> {
			list.add(this.getAspect(Aspects.getAspectById(Identifier.tryParse(key))));
		});
		return list;
	}

	@Override
	public CompoundTag toNbt(CompoundTag tag, ItemStack t)
	{
		return this.tag;
	}

	@Override
	public void fromNbt(CompoundTag tag, ItemStack t)
	{
		this.tag = tag;
	}
}
