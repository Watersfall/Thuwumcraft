package net.watersfall.alchemy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AspectPipeEntity extends BlockEntity
{
	private static final int[][] positions = new int[][]{{1,0,0},{0,1,0},{0,0,1},{-1,0,0},{0,-1,0},{0,0,-1}};

	public AspectPipeEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.ASPECT_PIPE_ENTITY, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, AspectPipeEntity pipe)
	{

	}
}
