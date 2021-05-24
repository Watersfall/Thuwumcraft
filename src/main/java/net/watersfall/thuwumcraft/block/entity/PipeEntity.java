package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;

public class PipeEntity extends BlockEntity implements AspectContainer
{
	private static final int[][] positions = new int[][]{{1,0,0},{0,1,0},{0,0,1},{-1,0,0},{0,-1,0},{0,0,-1}};

	public AspectStack stack;

	public PipeEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.ASPECT_PIPE_ENTITY, pos, state);
		this.stack = AspectStack.EMPTY;
	}

	public static void tick(World world, BlockPos pos, BlockState state, PipeEntity pipe)
	{

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
				this.stack.increment(stack.getCount());
				return AspectStack.EMPTY;
			}
		}
		else
		{
			this.stack = stack;
			return AspectStack.EMPTY;
		}
		return AspectStack.EMPTY;
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
				return new AspectStack(stack.getAspect(), extract);
			}
		}
		return AspectStack.EMPTY;
	}

	@Override
	public int getSuction()
	{
		return 1;
	}
}
