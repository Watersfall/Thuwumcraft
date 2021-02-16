package net.watersfall.alchemy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.aspect.AspectStack;

import java.util.HashMap;

public class CrucibleEntity extends AbstractCauldronEntity implements AspectInventory
{
	private ItemStack inputStack;
	private HashMap<Aspect, AspectStack> aspects;

	public CrucibleEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.CRUCIBLE_ENTITY, pos, state);
		this.inputStack = ItemStack.EMPTY;
		this.aspects = new HashMap<>();
	}

	@Override
	public HashMap<Aspect, AspectStack> getAspects()
	{
		return this.aspects;
	}

	@Override
	public ItemStack getCurrentInput()
	{
		return inputStack;
	}

	@Override
	public void setCurrentInput(ItemStack stack)
	{
		this.inputStack = stack;
	}

	@Override
	public AspectStack getAspect(Aspect aspect)
	{
		return aspects.get(aspect);
	}

	@Override
	public AspectStack removeAspect(Aspect aspect)
	{
		return aspects.remove(aspect);
	}

	@Override
	public AspectStack removeAspect(Aspect aspect, int amount)
	{
		AspectStack stack = aspects.get(aspect);
		if(amount >= stack.getCount())
		{
			return aspects.remove(aspect);
		}
		else
		{
			stack.decrement(amount);
			return new AspectStack(aspect, amount);
		}
	}

	@Override
	public void addAspect(AspectStack aspect)
	{
		if(aspects.containsKey(aspect.getAspect()))
		{
			AspectStack stack = aspects.get(aspect.getAspect());
			stack.increment(aspect.getCount());
		}
		else
		{
			aspects.put(aspect.getAspect(), aspect);
		}
	}

	@Override
	public int aspectSize()
	{
		return this.aspects.size();
	}

	@Override
	public void setAspect(Aspect aspect, int amount)
	{
		this.aspects.put(aspect, new AspectStack(aspect, amount));
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		this.fromInventoryTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		this.toInventoryTag(tag);
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		this.fromTag(compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		return this.toTag(compoundTag);
	}

	@Override
	public float getMaxDisplayWaterLevel()
	{
		return this.waterLevel;
	}

}
