package com.watersfall.alchemy.block;

import com.watersfall.alchemy.blockentity.PedestalEntity;
import com.watersfall.alchemy.item.AlchemyModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PedestalBlock extends Block implements BlockEntityProvider
{
	private static final byte HORIZONTAL_RANGE = 5;
	private static final byte VERTICAL_RANGE = 1;

	public PedestalBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ItemStack playerStack = player.getStackInHand(hand);
		BlockEntity entityCheck = world.getBlockEntity(pos);
		if(entityCheck instanceof PedestalEntity)
		{
			PedestalEntity entity = (PedestalEntity)entityCheck;
			if(playerStack.getItem() == AlchemyModItems.WITCHY_SPOON_ITEM)
			{
				if(entity.getStack().getItem() == Items.IRON_INGOT)
				{
					if(!world.isClient)
					{
						BlockPos.Mutable mutablePos = new BlockPos.Mutable();
						BlockState loopState = null;
						List<PedestalEntity> entities = new ArrayList<>();
						for(int x = pos.getX() - HORIZONTAL_RANGE; x < pos.getX() + HORIZONTAL_RANGE; x++)
						{
							for(int y = pos.getY() - VERTICAL_RANGE; y < pos.getY() + VERTICAL_RANGE; y++)
							{
								for(int z = pos.getZ() - HORIZONTAL_RANGE; z < pos.getZ() + HORIZONTAL_RANGE; z++)
								{
									mutablePos.set(x, y, z);
									if(!mutablePos.equals(pos))
									{
										loopState = world.getBlockState(mutablePos);
										if(loopState.getBlock() == AlchemyModBlocks.PEDESTAL_BLOCK)
										{
											entities.add((PedestalEntity)world.getBlockEntity(mutablePos));
										}
									}
								}
							}
						}
						entities.removeIf((test) -> test.getStack().getItem() != Items.IRON_INGOT);
						if(entities.size() >= 3)
						{
							entities.forEach((remove) -> {
								remove.setStack(ItemStack.EMPTY);
								remove.sync();
							});
							entity.setStack(new ItemStack(Items.GOLD_BLOCK));
							entity.sync();
						}
					}
				}
				return ActionResult.success(world.isClient);
			}
			else if(!playerStack.isEmpty())
			{
				ItemStack entityStack = entity.getStack();
				if(!entityStack.isItemEqual(playerStack))
				{
					if(!world.isClient)
					{
						entity.setStack(new ItemStack(playerStack.getItem()));
						playerStack.decrement(1);
						if(!entityStack.isEmpty())
						{
							if(playerStack.isEmpty())
							{
								player.setStackInHand(hand, entityStack);
							}
							else if(!player.inventory.insertStack(entityStack))
							{
								player.dropItem(entityStack, false, true);
							}
						}
						entity.sync();
					}
					return ActionResult.success(world.isClient);
				}
				return ActionResult.FAIL;
			}
			else
			{
				if(!entity.getStack().isEmpty())
				{
					if(!world.isClient)
					{
						player.setStackInHand(hand, entity.getStack());
						entity.setStack(ItemStack.EMPTY);
						entity.sync();
					}
					return ActionResult.success(world.isClient);
				}
				return ActionResult.FAIL;
			}
		}
		return ActionResult.FAIL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new PedestalEntity();
	}
}
