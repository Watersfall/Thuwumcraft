package net.watersfall.thuwumcraft.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.sound.AlchemySounds;
import net.watersfall.thuwumcraft.block.entity.CrucibleEntity;
import net.watersfall.thuwumcraft.recipe.AlchemyRecipes;
import net.watersfall.thuwumcraft.recipe.AspectIngredient;
import net.watersfall.thuwumcraft.recipe.CrucibleRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrucibleBlock extends AbstractCauldronBlock implements BlockEntityProvider
{
	public CrucibleBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ActionResult result = super.onUse(state, world, pos, player, hand, hit);
		if(result != ActionResult.PASS)
		{
			return result;
		}
		ItemStack stack = player.getStackInHand(hand);
		CrucibleEntity entity = (CrucibleEntity) world.getBlockEntity(pos);
		if(entity != null)
		{
			if(!stack.isEmpty())
			{
				entity.setCurrentInput(stack);
				Optional<CrucibleRecipe> crucibleOptional = world.getRecipeManager().getFirstMatch(AlchemyRecipes.CRUCIBLE_RECIPE, entity, world);
				if(crucibleOptional.isPresent())
				{
					CrucibleRecipe recipe = crucibleOptional.get();
					if(recipe.playerHasResearch(player))
					{
						if(!world.isClient)
						{
							stack.decrement(1);
							stack = recipe.craft(entity);
							if(!player.getInventory().insertStack(stack))
							{
								player.dropItem(stack, true);
							}
							entity.markDirty();
							entity.sync();
						}
						return ActionResult.success(world.isClient);
					}
				}
				Optional<AspectIngredient> aspectOptional = world.getRecipeManager().getFirstMatch(AlchemyRecipes.ASPECT_INGREDIENTS, entity, world);
				if(aspectOptional.isPresent())
				{
					if(!world.isClient)
					{
						AspectIngredient ingredient = aspectOptional.get();
						ingredient.craft(entity);
						entity.markDirty();
						entity.sync();
						world.playSound(null, pos, AlchemySounds.CAULDRON_ADD_INGREDIENT, SoundCategory.BLOCKS, 0.5F, 0.8F + ((float)Math.random() * 0.4F));
					}
					return ActionResult.success(world.isClient);
				}
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{

	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new CrucibleEntity(pos, state);
	}
}
