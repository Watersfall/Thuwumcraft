package net.watersfall.thuwumcraft.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.item.BeforeActions;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;

import java.util.ArrayList;

public class SpecialPickaxeItem extends PickaxeItem implements BeforeActions
{
	public static BlockPos getFurthestOre(World world, Block block, BlockPos pos)
	{
		int limit = 512;
		ArrayList<BlockPos> poses = new ArrayList<>(limit);
		BlockPos.Mutable current = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		poses.add(pos);
		boolean found = true;
		for(int i = 0; i < limit && found; i++)
		{
			found = false;
			for(int x = -1; x <= 1; x++)
			{
				for(int y = -1; y <= 1; y++)
				{
					for(int z = -1; z <= 1; z++)
					{
						mutable.set(current.getX() + x, current.getY() + y, current.getZ() + z);
						if(!current.equals(mutable) && !poses.contains(mutable) && world.getBlockState(mutable).getBlock() == block)
						{
							poses.add(mutable.toImmutable());
							current.set(mutable.getX(), mutable.getY(), mutable.getZ());
							found = true;
							break;
						}
					}
					if(found) break;
				}
				if(found) break;
			}
		}
		return current.toImmutable();
	}

	public SpecialPickaxeItem()
	{
		super(AlchemyToolMaterials.MAGIC, 1, -2.8F, (new Item.Settings()).group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP).fireproof());
	}

	@Override
	public boolean beforeMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner)
	{
		Block block = state.getBlock();
		if(block instanceof OreBlock)
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
		if(stack.getNbt() != null && stack.getNbt().contains("RepairCost"))
		{
			stack.getNbt().putInt("RepairCost", 0);
		}
	}
}
