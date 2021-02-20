package net.watersfall.alchemy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class PhialShelfEntity extends BlockEntity
{
	public PhialShelfEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.PHIAL_SHELF_ENTITY, pos, state);
	}
}
