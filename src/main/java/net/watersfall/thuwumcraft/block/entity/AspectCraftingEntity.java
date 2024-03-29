package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.inventory.AspectCraftingInventory;
import net.watersfall.thuwumcraft.gui.AspectCraftingHandler;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;

import java.util.HashSet;
import java.util.Set;

public class AspectCraftingEntity extends BlockEntity
{
	public DefaultedList<ItemStack> contents;
	public Set<AspectCraftingHandler> handlers;

	public AspectCraftingEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.ASPECT_CRAFTING_TABLE, pos, state);
		this.contents = DefaultedList.ofSize(16, ItemStack.EMPTY);
		handlers = new HashSet<>();
	}

	public AspectCraftingInventory getInventory(ScreenHandler handler)
	{
		handlers.add((AspectCraftingHandler)handler);
		return new AspectCraftingInventory(handler);
	}

	public void close(ScreenHandler handler)
	{
		handlers.remove(handler);
	}

	@Override
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		Inventories.readNbt(tag, contents);
	}

	@Override
	public void writeNbt(NbtCompound tag)
	{
		DefaultedList<ItemStack> writeList = DefaultedList.ofSize(15, ItemStack.EMPTY);
		for(int i = 0; i < 15; i++)
		{
			writeList.set(i, this.contents.get(i));
		}
		super.writeNbt(tag);
		Inventories.writeNbt(tag, writeList);
	}
}
