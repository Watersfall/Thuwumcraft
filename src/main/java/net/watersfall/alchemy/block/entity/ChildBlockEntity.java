package net.watersfall.alchemy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.multiblock.MultiBlockComponent;
import net.minecraft.block.entity.BlockEntity;

public class ChildBlockEntity extends BlockEntity
{
	private MultiBlockComponent component;

	public ChildBlockEntity(BlockEntityType<? extends ChildBlockEntity> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public ChildBlockEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyModBlockEntities.CHILD_BLOCK_ENTITY, pos, state);
	}

	public MultiBlockComponent getComponent()
	{
		return component;
	}

	public void setComponent(MultiBlockComponent component)
	{
		this.component = component;
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		return tag;
	}
}
