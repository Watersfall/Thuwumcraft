package net.watersfall.alchemy.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.lookup.AspectContainer;

public class EssentiaRefineryBlockEntity extends BlockEntity implements AspectContainer, BlockEntityClientSerializable
{
	private AspectStack stack;
	private static final int MAX_COUNT = 64;

	public EssentiaRefineryBlockEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.ESSENTIA_REFINERY, pos, state);
		stack = AspectStack.EMPTY;
	}

	@Override
	public AspectStack insert(AspectStack stack)
	{
		if(stack.isEmpty())
		{
			return stack;
		}
		if(!this.stack.isEmpty())
		{
			if(this.stack.getAspect() == stack.getAspect())
			{
				int increment = Math.min(MAX_COUNT - this.stack.getCount(), stack.getCount());
				this.stack.increment(increment);
				stack.decrement(increment);
				this.sync();
				return stack;
			}
		}
		else
		{
			this.stack = stack;
			this.sync();
			return AspectStack.EMPTY;
		}
		return stack;
	}

	@Override
	public AspectStack extract(AspectStack stack)
	{
		if(stack.isEmpty())
		{
			return stack;
		}
		if(!this.stack.isEmpty())
		{
			if(this.stack.getAspect() == stack.getAspect())
			{
				int extract = Math.min(stack.getCount(), this.stack.getCount());
				this.stack.decrement(extract);
				this.sync();
				return new AspectStack(stack.getAspect(), extract);
			}
		}
		return AspectStack.EMPTY;
	}

	public AspectStack getStack()
	{
		return this.stack;
	}

	@Override
	public int getSuction()
	{
		return 2;
	}

	@Override
	public void fromClientTag(NbtCompound tag)
	{
		this.stack = AspectStack.EMPTY;
		if(tag.contains("aspect"))
		{
			Aspect aspect = Aspects.getAspectById(Identifier.tryParse(tag.getString("aspect")));
			int count = tag.getInt("count");
			this.stack = new AspectStack(aspect, count);
		}
	}

	@Override
	public NbtCompound toClientTag(NbtCompound tag)
	{
		if(!this.stack.isEmpty())
		{
			tag.putString("aspect", this.stack.getAspect().getId().toString());
			tag.putInt("count", this.stack.getCount());
		}
		return tag;
	}
}
