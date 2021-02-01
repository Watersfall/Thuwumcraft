package net.watersfall.alchemy.multiblock.impl.component;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.watersfall.alchemy.multiblock.MultiBlock;
import net.watersfall.alchemy.multiblock.component.ItemComponent;
import net.watersfall.alchemy.multiblock.impl.inventory.AlchemicalFurnaceOutput;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemicalFurnaceOutputComponent extends AlchemicalFurnaceComponent implements ItemComponent
{
	private final AlchemicalFurnaceOutput inventory;

	public AlchemicalFurnaceOutputComponent(World world, MultiBlock<AlchemicalFurnaceComponent> multiBlock, BlockPos pos)
	{
		super(world, multiBlock, pos);
		this.inventory = new AlchemicalFurnaceOutput();
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
		tag.put("output_inventory", Inventories.toTag(new CompoundTag(), this.inventory.getContents()));
		return tag;
	}

	@Override
	public void read(BlockState state, CompoundTag tag)
	{
		super.read(state, tag);
		Inventories.fromTag(tag.getCompound("output_inventory"), this.inventory.getContents());
	}
}
