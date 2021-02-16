package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.watersfall.alchemy.block.AlchemyBlocks;

public class MagicDustItem extends Item
{
	public MagicDustItem()
	{
		super(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP).rarity(Rarity.UNCOMMON));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		BlockState state = context.getWorld().getBlockState(context.getBlockPos());
		if(state.getBlock() == Blocks.CAULDRON)
		{
			if(!context.getWorld().isClient)
			{
				context.getWorld().setBlockState(context.getBlockPos(), AlchemyBlocks.CRUCIBLE_BLOCK.getDefaultState());
			}
			return ActionResult.success(context.getWorld().isClient);
		}
		return ActionResult.PASS;
	}
}
