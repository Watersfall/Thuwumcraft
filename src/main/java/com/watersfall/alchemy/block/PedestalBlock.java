package com.watersfall.alchemy.block;

import com.watersfall.alchemy.AlchemyMod;
import com.watersfall.alchemy.blockentity.PedestalEntity;
import com.watersfall.alchemy.item.AlchemyModItems;
import com.watersfall.alchemy.recipe.PedestalRecipe;
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
import java.util.Optional;

public class PedestalBlock extends Block implements BlockEntityProvider
{
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
				Optional<PedestalRecipe> recipeOptional = world.getRecipeManager().getFirstMatch(AlchemyMod.PEDESTAL_RECIPE, entity, world);
				if(recipeOptional.isPresent())
				{
					if(!world.isClient)
					{
						PedestalRecipe recipe = recipeOptional.get();
						recipe.craft(entity);
					}
					return ActionResult.success(world.isClient);
				}
				return ActionResult.FAIL;
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
