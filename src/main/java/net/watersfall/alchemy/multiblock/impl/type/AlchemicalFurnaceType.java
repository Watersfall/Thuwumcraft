package net.watersfall.alchemy.multiblock.impl.type;

import net.watersfall.alchemy.block.AlchemyModBlocks;
import net.watersfall.alchemy.multiblock.MultiBlockRegistry;
import net.watersfall.alchemy.multiblock.MultiBlockType;
import net.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceComponent;
import net.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceInputComponent;
import net.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceOutputComponent;
import net.watersfall.alchemy.multiblock.impl.multiblock.AlchemicalFurnaceMultiBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemicalFurnaceType implements MultiBlockType<AlchemicalFurnaceMultiBlock>
{
	public static final AlchemicalFurnaceType INSTANCE = new AlchemicalFurnaceType();

	private AlchemicalFurnaceType(){}

	@Override
	public boolean matches(World world, BlockPos pos)
	{
		return true;
	}

	@Override
	public AlchemicalFurnaceMultiBlock create(World world, BlockPos pos)
	{
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		AlchemicalFurnaceComponent[] components = new AlchemicalFurnaceComponent[4];
		AlchemicalFurnaceMultiBlock multiBlock = new AlchemicalFurnaceMultiBlock(world, pos, components);
		for(int x = 0; x < 2; x++)
		{
			for(int y = 0; y < 2; y++)
			{
				int index = x * 2 + y;
				mutablePos.set(pos.getX() + x, pos.getY() + y, pos.getZ());
				world.setBlockState(mutablePos.toImmutable(), AlchemyModBlocks.CHILD_BLOCK.getDefaultState());
				AlchemicalFurnaceComponent component;
				if(index == AlchemicalFurnaceMultiBlock.INPUT)
				{
					component = new AlchemicalFurnaceInputComponent(world, multiBlock, mutablePos.toImmutable());
				}
				else if(index == AlchemicalFurnaceMultiBlock.OUTPUT)
				{
					component = new AlchemicalFurnaceOutputComponent(world, multiBlock, mutablePos.toImmutable());
				}
				else if(index == AlchemicalFurnaceMultiBlock.BOTTOM_LEFT)
				{
					component = new AlchemicalFurnaceOutputComponent(world, multiBlock, mutablePos.toImmutable());
					world.setBlockState(mutablePos.toImmutable(), AlchemyModBlocks.ALCHEMICAL_FURNACE_BLOCK.getDefaultState());
				}
				else
				{
					component = new AlchemicalFurnaceComponent(world, multiBlock, mutablePos.toImmutable());
				}
				components[index] = component;
			}
		}
		MultiBlockRegistry.INSTANCE.add(multiBlock);
		return multiBlock;
	}
}
