package net.watersfall.alchemy.multiblock.inventory;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.multiblock.MultiBlock;
import org.jetbrains.annotations.Nullable;

public class AlchemicalFurnaceInput implements AlchemicalFurnaceInventory
{
	private static final int[] AVAILABLE_SLOTS = new int[]{0,1,2,3,4,5,6,7,8};
	private final DefaultedList<ItemStack> contents;
	private World world;
	private BlockPos pos;
	private MultiBlock<?> multiBlock;

	public AlchemicalFurnaceInput(World world, BlockPos pos, MultiBlock<?> multiBlock)
	{
		contents = DefaultedList.ofSize(9, ItemStack.EMPTY);
		this.world = world;
		this.pos = pos;
		this.multiBlock = multiBlock;
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return this.contents;
	}

	@Override
	public MultiBlock<?> getMultiBlock()
	{
		return this.multiBlock;
	}

	@Override
	public int[] getAvailableSlots(Direction side)
	{
		return AVAILABLE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir)
	{
		return true;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir)
	{
		return false;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	@Override
	public void sync()
	{
		if(!world.isClient)
		{
			BlockEntity test = world.getBlockEntity(pos);
			if(test != null)
			{
				test.markDirty();
			}
			if(test instanceof BlockEntityClientSerializable)
			{
				((BlockEntityClientSerializable) test).sync();
			}
		}
	}
}
