package net.watersfall.thuwumcraft.multiblock.component;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlock;
import net.watersfall.thuwumcraft.api.multiblock.component.ItemComponent;
import net.watersfall.thuwumcraft.multiblock.inventory.AlchemicalFurnaceFuel;

public class AlchemicalFurnaceFuelComponent extends AlchemicalFurnaceComponent implements ItemComponent
{
	private final AlchemicalFurnaceFuel inventory;

	public AlchemicalFurnaceFuelComponent(World world, MultiBlock<AlchemicalFurnaceComponent> multiBlock, BlockPos pos)
	{
		super(world, multiBlock, pos);
		this.inventory = new AlchemicalFurnaceFuel(multiBlock);
	}

	@Override
	public SidedInventory getInventory()
	{
		return this.inventory;
	}

	@Override
	public NbtCompound write(NbtCompound tag)
	{
		super.write(tag);
		tag.put("fuel_inventory", Inventories.writeNbt(new NbtCompound(), this.inventory.getContents()));
		return tag;
	}

	@Override
	public void read(NbtCompound tag)
	{
		super.read(tag);
		Inventories.readNbt(tag.getCompound("fuel_inventory"), this.inventory.getContents());
	}

	@Override
	public void tick()
	{
		super.tick();
	}
}
