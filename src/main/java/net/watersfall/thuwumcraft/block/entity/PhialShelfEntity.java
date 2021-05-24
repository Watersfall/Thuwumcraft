package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.inventory.BasicInventory;

public class PhialShelfEntity extends BlockEntity implements BasicInventory, BlockEntityClientSerializable
{
	private DefaultedList<ItemStack> contents;

	public PhialShelfEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.PHIAL_SHELF_ENTITY, pos, state);
		contents = DefaultedList.ofSize(6, ItemStack.EMPTY);
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return this.contents;
	}

	@Override
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		Inventories.readNbt(tag, this.contents);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		Inventories.writeNbt(tag, this.contents);
		return tag;
	}


	@Override
	public void fromClientTag(NbtCompound compoundTag)
	{
		this.contents.clear();
		Inventories.readNbt(compoundTag, this.contents);
	}

	@Override
	public NbtCompound toClientTag(NbtCompound compoundTag)
	{
		return Inventories.writeNbt(compoundTag, this.contents);
	}
}
