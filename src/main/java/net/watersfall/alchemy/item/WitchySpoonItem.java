package net.watersfall.alchemy.item;

import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.multiblock.MultiBlockType;
import net.watersfall.alchemy.multiblock.impl.type.AlchemicalFurnaceType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class WitchySpoonItem extends Item
{

	public WitchySpoonItem()
	{
		super(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP).maxCount(1));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		if(state.getBlock() == Blocks.CAULDRON)
		{
			context.getWorld().setBlockState(context.getBlockPos(), AlchemyBlocks.BREWING_CAULDRON_BLOCK.getDefaultState());
			return ActionResult.success(context.getWorld().isClient);
		}
		else if(state.getBlock() == AlchemyBlocks.BREWING_CAULDRON_BLOCK)
		{
			context.getWorld().setBlockState(context.getBlockPos(), AlchemyBlocks.CRUCIBLE_BLOCK.getDefaultState());
			return ActionResult.success(context.getWorld().isClient);
		}
		else if(state.getBlock() == Blocks.FURNACE)
		{
			BlockPos[] states = AlchemicalFurnaceType.INSTANCE.matches(context.getPlayer(), context.getWorld(), context.getBlockPos());
			if(states != MultiBlockType.MISSING)
			{
				if(!context.getWorld().isClient)
				{
					AlchemicalFurnaceType.INSTANCE.create(context.getPlayer(), context.getWorld(), context.getBlockPos(), states);
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
