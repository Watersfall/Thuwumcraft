package net.watersfall.alchemy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.api.multiblock.MultiBlockComponent;
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
		super(AlchemyBlockEntities.CHILD_BLOCK_ENTITY, pos, state);
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
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		return tag;
	}
}
