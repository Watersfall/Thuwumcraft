package net.watersfall.alchemy.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.watersfall.alchemy.multiblock.MultiBlockComponent;
import net.minecraft.block.entity.BlockEntity;

public class ChildBlockEntity extends BlockEntity
{
	private MultiBlockComponent component;

	public ChildBlockEntity(BlockEntityType<? extends ChildBlockEntity> type)
	{
		super(type);
	}

	public ChildBlockEntity()
	{
		super(AlchemyModBlockEntities.CHILD_BLOCK_ENTITY);
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
	public void fromTag(BlockState state, CompoundTag tag)
	{
		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		return tag;
	}
}
