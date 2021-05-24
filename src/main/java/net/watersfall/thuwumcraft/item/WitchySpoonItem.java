package net.watersfall.thuwumcraft.item;

import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockRegistry;
import net.watersfall.thuwumcraft.block.AlchemyBlocks;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockType;
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
		else
		{
			Pair<MultiBlockType, BlockPos[]> type = MultiBlockRegistry.TYPES.getMatch(context.getPlayer(), context.getWorld(), state, context.getBlockPos());
			if(type != null)
			{
				if(!context.getWorld().isClient)
				{
					type.getLeft().create(context.getPlayer(), context.getWorld(), context.getBlockPos(), type.getRight());
				}
				return ActionResult.success(context.getWorld().isClient);
			}
			return ActionResult.PASS;
		}
	}
}
