package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockComponent;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;

public class ChildBlockEntity extends SyncableBlockEntity
{
	private MultiBlockComponent component;

	public ChildBlockEntity(BlockEntityType<? extends ChildBlockEntity> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	public ChildBlockEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.CHILD_BLOCK, pos, state);
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
	public void writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
	}

	@Override
	public NbtCompound toClientTag(NbtCompound nbt)
	{
		return nbt;
	}

	@Override
	public void fromClientTag(NbtCompound nbt)
	{

	}
}
