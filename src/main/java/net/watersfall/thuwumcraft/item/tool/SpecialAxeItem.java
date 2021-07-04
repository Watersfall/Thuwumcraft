package net.watersfall.thuwumcraft.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.item.BeforeActions;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

public class SpecialAxeItem extends AxeItem implements BeforeActions
{
	public SpecialAxeItem()
	{
		super(AlchemyToolMaterials.MAGIC, 5.0F, -3.0F, (new Item.Settings()).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP).fireproof());
	}

	@Override
	public boolean beforeMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner)
	{
		if(state.isIn(BlockTags.LOGS))
		{
			BlockPos breakPos = SpecialPickaxeItem.getFurthestOre(world, state.getBlock(), pos);
			if(breakPos.equals(pos))
			{
				return false;
			}
			else
			{
				world.breakBlock(breakPos, false, miner);
				BlockEntity entity = world.getBlockEntity(pos);
				Block.dropStacks(state, world, pos, entity, miner, stack);
			}
		}
		return true;
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
