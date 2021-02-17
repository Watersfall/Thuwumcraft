package net.watersfall.alchemy.multiblock.component;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.multiblock.MultiBlock;
import net.watersfall.alchemy.api.multiblock.component.ItemComponent;
import net.watersfall.alchemy.multiblock.inventory.AlchemicalFurnaceFuel;

public class AlchemicalFurnaceFuelComponent extends AlchemicalFurnaceComponent implements ItemComponent
{
	private final AlchemicalFurnaceFuel inventory;

	public AlchemicalFurnaceFuelComponent(World world, MultiBlock<AlchemicalFurnaceComponent> multiBlock, BlockPos pos)
	{
		super(world, multiBlock, pos);
		this.inventory = new AlchemicalFurnaceFuel();
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
		tag.put("fuel_inventory", Inventories.toTag(new CompoundTag(), this.inventory.getContents()));
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		super.read(tag);
		Inventories.fromTag(tag.getCompound("fuel_inventory"), this.inventory.getContents());
	}

	@Override
	public void tick()
	{
		super.tick();
	}
}
