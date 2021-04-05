package net.watersfall.alchemy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.inventory.AspectCraftingInventory;
import net.watersfall.alchemy.screen.AspectCraftingHandler;

import java.util.HashSet;
import java.util.Set;

public class AspectCraftingEntity extends BlockEntity
{
	public DefaultedList<ItemStack> contents;
	public Set<AspectCraftingHandler> handlers;

	public AspectCraftingEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.ASPECT_CRAFTING_ENTITY, pos, state);
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
}
