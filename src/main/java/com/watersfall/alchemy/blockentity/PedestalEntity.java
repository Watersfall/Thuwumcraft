package com.watersfall.alchemy.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class PedestalEntity extends BlockEntity implements BlockEntityClientSerializable
{
	private ItemStack stack;

	public PedestalEntity()
	{
		super(AlchemyModBlockEntities.PEDESTAL_ENTITY);
		stack = ItemStack.EMPTY;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		this.stack = ItemStack.fromTag(compoundTag.getCompound("pedestal_item"));
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		compoundTag.put("pedestal_item", this.stack.toTag(new CompoundTag()));
		return compoundTag;
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
	}
}
