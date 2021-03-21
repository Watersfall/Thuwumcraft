package net.watersfall.alchemy.abilities.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import net.watersfall.alchemy.api.abilities.block.AspectContainer;
import net.watersfall.alchemy.api.aspect.AspectStack;

public class AspectPipeAbility implements AspectContainer
{
	private AspectStack stack;

	public AspectPipeAbility()
	{
		stack = AspectStack.EMPTY;
	}

	@Override
	public boolean canInsert(AspectStack stack, Direction direction)
	{
		return !stack.isEmpty() && (this.stack.isEmpty() || stack.getAspect() == this.stack.getAspect());
	}

	@Override
	public boolean canExtract(Direction direction)
	{
		return !this.stack.isEmpty();
	}

	@Override
	public AspectStack insert(AspectStack stack)
	{
		this.stack.increment(stack.getCount());
		return AspectStack.EMPTY;
	}

	@Override
	public AspectStack extract(AspectStack stack)
	{
		AspectStack stack1 = this.stack;
		this.stack = AspectStack.EMPTY;
		return stack1;
	}

	public void fromTag(CompoundTag tag)
	{
		this.stack = new AspectStack(tag.getCompound("pipe_stack"));
	}

	public CompoundTag toTag(CompoundTag tag)
	{
		CompoundTag compound = new CompoundTag();
		compound.putString("aspect", stack.getAspect().getId().toString());
		compound.putInt("count", stack.getCount());
		tag.put("pipe_stack", compound);
		return tag;
	}
}
