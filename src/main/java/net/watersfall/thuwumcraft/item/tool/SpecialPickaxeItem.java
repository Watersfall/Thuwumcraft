package net.watersfall.thuwumcraft.item.tool;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.item.ThuwumcraftItems;

import java.util.ArrayList;

public class SpecialPickaxeItem extends PickaxeItem
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
	public void onCraft(ItemStack stack, World world, PlayerEntity player)
	{
		if(stack.getTag() != null && stack.getTag().contains("RepairCost"))
		{
			stack.getTag().putInt("RepairCost", 0);
		}
	}
}
