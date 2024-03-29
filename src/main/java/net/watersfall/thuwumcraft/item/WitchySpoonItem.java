package net.watersfall.thuwumcraft.item;

import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockRegistry;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

public class WitchySpoonItem extends Item
{

	public WitchySpoonItem()
	{
		super(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP).maxCount(1));
	}

	//Todo: Behavior map like vanilla cauldron
	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		if(state.getBlock() == Blocks.CAULDRON)
		{
			context.getWorld().setBlockState(context.getBlockPos(), ThuwumcraftBlocks.BREWING_CAULDRON.getDefaultState());
			return ActionResult.success(context.getWorld().isClient);
		}
		else if(state.getBlock() == ThuwumcraftBlocks.BREWING_CAULDRON)
		{
			context.getWorld().setBlockState(context.getBlockPos(), ThuwumcraftBlocks.CRUCIBLE.getDefaultState());
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
