package net.watersfall.alchemy.multiblock.impl.component;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.shape.VoxelShape;
import net.watersfall.alchemy.block.AlchemicalFurnaceBlock;
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
		this.multiBlock.onUse(this.world, this.pos, player);
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
	public void setWorld(World world)
	{
		this.world = world;
	}

	@Override
	public MultiBlock<AlchemicalFurnaceComponent> getMultiBlock()
	{
		return this.multiBlock;
	}

	@Override
	public VoxelShape getOutline()
	{
		int index = this.multiBlock.getWorld().getBlockState(this.multiBlock.getPos()).get(AlchemicalFurnaceBlock.DIRECTION).getId() - 2;
		index *= 4;
		for(int i = 0; i < this.multiBlock.getComponents().length; i++)
		{
			if(this.multiBlock.getComponents()[i] != this)
			{
				index++;
			}
			else
			{
				break;
			}
		}
		return AlchemicalFurnaceMultiBlock.SHAPES[index];
	}

	@Override
	public CompoundTag write(CompoundTag tag)
	{
		return tag;
	}

	@Override
	public void read(BlockState state, CompoundTag tag)
	{

	}
}
