package com.watersfall.alchemy.blockentity;

import com.watersfall.alchemy.multiblock.MultiBlockComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;

public class ChildBlockEntity extends BlockEntity
{
	private MultiBlockComponent component;

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
}
