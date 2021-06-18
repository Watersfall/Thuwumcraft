package net.watersfall.thuwumcraft.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.item.ThuwumcraftItems;

public class SpecialHoeItem extends OpenHoeItem
{
	public SpecialHoeItem()
	{
		super(AlchemyToolMaterials.MAGIC, 2, -3.0F, (new Item.Settings()).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP).fireproof());
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		ActionResult result = super.useOnBlock(context);
		if(result.isAccepted())
		{
			return result;
		}
		else
		{
			boolean fertilized = BoneMealItem.useOnFertilizable(new ItemStack(Items.BONE_MEAL), context.getWorld(), context.getBlockPos());
			if(!fertilized)
			{
				BlockState state = context.getWorld().getBlockState(context.getBlockPos());
				if(state.getBlock() instanceof CropBlock)
				{
					CropBlock block = (CropBlock)state.getBlock();
					if(block.isMature(state))
					{
						context.getWorld().setBlockState(context.getBlockPos(), state.with(CropBlock.AGE, 0));
						Block.dropStacks(state, context.getWorld(), context.getBlockPos(), null, context.getPlayer(), context.getPlayer().getStackInHand(context.getHand()));
						context.getStack().damage(1, context.getPlayer(), player -> {
							player.sendToolBreakStatus(context.getHand());
						});
						return ActionResult.success(context.getWorld().isClient);
					}
				}
			}
			else
			{
				context.getStack().damage(1, context.getPlayer(), player -> {
					player.sendToolBreakStatus(context.getHand());
				});
				return ActionResult.success(context.getWorld().isClient);
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player)
	{
		if(stack.getTag() != null && stack.getTag().contains("RepairCost"))
		{
			stack.getTag().putInt("RepairCost", 0);
		}
	}
}
