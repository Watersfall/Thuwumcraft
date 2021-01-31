package net.watersfall.alchemy.multiblock.impl.component;

import net.minecraft.util.shape.VoxelShape;
import net.watersfall.alchemy.multiblock.MultiBlock;
import net.watersfall.alchemy.multiblock.MultiBlockComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.alchemy.multiblock.impl.multiblock.AlchemicalFurnaceMultiBlock;

public class AlchemicalFurnaceComponent implements MultiBlockComponent
{
	private World world;
	private MultiBlock<AlchemicalFurnaceComponent> multiBlock;
	private BlockPos pos;

	public AlchemicalFurnaceComponent(World world, MultiBlock<AlchemicalFurnaceComponent> multiBlock, BlockPos pos)
	{
		this.world = world;
		this.multiBlock = multiBlock;
		this.pos = pos;
	}

	@Override
	public void onBreak()
	{
		this.multiBlock.markInvalid();
	}

	@Override
	public void onUse(PlayerEntity player)
	{
		this.multiBlock.onUse();
	}

	@Override
	public void tick()
	{

	}

	@Override
	public BlockPos getPos()
	{
		return this.pos;
	}

	@Override
	public World getWorld()
	{
		return this.world;
	}

	@Override
	public MultiBlock<AlchemicalFurnaceComponent> getMultiBlock()
	{
		return this.multiBlock;
	}

	@Override
	public VoxelShape getOutline()
	{
		if(this.multiBlock.getComponents()[AlchemicalFurnaceMultiBlock.BOTTOM_LEFT] == this)
		{
			return AlchemicalFurnaceMultiBlock.SHAPES[AlchemicalFurnaceMultiBlock.BOTTOM_LEFT];
		}
		else if(this.multiBlock.getComponents()[AlchemicalFurnaceMultiBlock.INPUT] == this)
		{
			return AlchemicalFurnaceMultiBlock.SHAPES[AlchemicalFurnaceMultiBlock.INPUT];
		}
		else if(this.multiBlock.getComponents()[AlchemicalFurnaceMultiBlock.OUTPUT] == this)
		{
			return AlchemicalFurnaceMultiBlock.SHAPES[AlchemicalFurnaceMultiBlock.OUTPUT];
		}
		else
		{
			return AlchemicalFurnaceMultiBlock.SHAPES[AlchemicalFurnaceMultiBlock.TOP_RIGHT];
		}
	}
}
