package net.watersfall.alchemy.item;

import net.watersfall.alchemy.block.AlchemyModBlocks;
import net.watersfall.alchemy.multiblock.impl.type.AlchemicalFurnaceType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class WitchySpoonItem extends Item
{

	public WitchySpoonItem()
	{
		super(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(1));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		if(state.getBlock() == Blocks.CAULDRON)
		{
			context.getWorld().setBlockState(context.getBlockPos(), AlchemyModBlocks.BREWING_CAULDRON_BLOCK.getDefaultState());
			return ActionResult.success(context.getWorld().isClient);
		}
		else if(state.getBlock() == Blocks.FURNACE)
		{
			if(AlchemicalFurnaceType.INSTANCE.matches(context.getWorld(), context.getBlockPos()))
			{
				if(!context.getWorld().isClient)
				{
					AlchemicalFurnaceType.INSTANCE.create(context.getWorld(), context.getBlockPos());
				}
				return ActionResult.success(context.getWorld().isClient);
			}
			return ActionResult.FAIL;
		}
		else
		{
			return super.useOnBlock(context);
		}
	}
}
