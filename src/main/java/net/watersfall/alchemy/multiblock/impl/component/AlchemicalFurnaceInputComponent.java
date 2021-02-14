package net.watersfall.alchemy.multiblock.impl.component;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.watersfall.alchemy.multiblock.MultiBlock;
import net.watersfall.alchemy.multiblock.component.ItemComponent;
import net.watersfall.alchemy.multiblock.impl.inventory.AlchemicalFurnaceInput;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemicalFurnaceInputComponent extends AlchemicalFurnaceComponent implements ItemComponent
{
	private AlchemicalFurnaceInput inventory;

	public AlchemicalFurnaceInputComponent(World world, MultiBlock<AlchemicalFurnaceComponent> multiBlock, BlockPos pos)
	{
		super(world, multiBlock, pos);
		this.inventory = new AlchemicalFurnaceInput(world, pos);
	}

	@Override
	public SidedInventory getInventory()
	{
		return this.inventory;
	}

	@Override
	public CompoundTag write(CompoundTag tag)
	{
		super.write(tag);
		tag.put("input_inventory", Inventories.toTag(new CompoundTag(), this.inventory.getContents()));
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		super.read(tag);
		Inventories.fromTag(tag.getCompound("input_inventory"), this.inventory.getContents());
	}

	@Override
	public void setWorld(World world)
	{
		super.setWorld(world);
		this.inventory.setWorld(world);
	}
}
