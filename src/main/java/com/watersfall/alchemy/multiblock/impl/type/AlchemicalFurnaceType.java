package com.watersfall.alchemy.multiblock.impl.type;

import com.watersfall.alchemy.block.AlchemyModBlocks;
import com.watersfall.alchemy.multiblock.MultiBlockRegistry;
import com.watersfall.alchemy.multiblock.MultiBlockType;
import com.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceComponent;
import com.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceInputComponent;
import com.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceOutputComponent;
import com.watersfall.alchemy.multiblock.impl.multiblock.AlchemicalFurnaceMultiBlock;
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
