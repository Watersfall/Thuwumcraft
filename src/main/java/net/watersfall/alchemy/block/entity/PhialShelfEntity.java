package net.watersfall.alchemy.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.inventory.BasicInventory;

public class PhialShelfEntity extends BlockEntity implements BasicInventory, BlockEntityClientSerializable
{
	private DefaultedList<ItemStack> contents;

	public PhialShelfEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.PHIAL_SHELF_ENTITY, pos, state);
		contents = DefaultedList.ofSize(6, ItemStack.EMPTY);
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return this.contents;
	}

	@Override
	public void readNbt(CompoundTag tag)
	{
		super.readNbt(tag);
		Inventories.readNbt(tag, this.contents);
	}

	@Override
	public CompoundTag writeNbt(CompoundTag tag)
	{
		super.writeNbt(tag);
		Inventories.writeNbt(tag, this.contents);
		return tag;
	}


	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		this.contents.clear();
		Inventories.readNbt(compoundTag, this.contents);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		return Inventories.writeNbt(compoundTag, this.contents);
	}
}
