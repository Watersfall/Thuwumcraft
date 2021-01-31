package com.watersfall.alchemy.multiblock.impl.component;

import com.watersfall.alchemy.multiblock.MultiBlock;
import com.watersfall.alchemy.multiblock.component.ItemComponent;
import com.watersfall.alchemy.multiblock.impl.inventory.AlchemicalFurnaceOutput;
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
}
