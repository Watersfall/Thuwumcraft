package net.watersfall.thuwumcraft.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.sound.ThuwumcraftSounds;
import net.watersfall.thuwumcraft.block.entity.CrucibleEntity;
import net.watersfall.thuwumcraft.recipe.AspectIngredient;
import net.watersfall.thuwumcraft.recipe.CrucibleRecipe;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.util.InventoryHelper;
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
				Optional<CrucibleRecipe> crucibleOptional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.CRUCIBLE, entity, world);
				if(crucibleOptional.isPresent())
				{
					CrucibleRecipe recipe = crucibleOptional.get();
					if(recipe.playerHasResearch(player))
					{
						if(!world.isClient)
						{
							InventoryHelper.useItem(stack, player, hand, 1, recipe.craft(entity));
							entity.markDirty();
							entity.sync();
						}
						return ActionResult.success(world.isClient);
					}
				}
				Optional<AspectIngredient> aspectOptional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.ASPECT_INGREDIENTS, entity, world);
				if(aspectOptional.isPresent())
				{
					if(!world.isClient)
					{
						AspectIngredient ingredient = aspectOptional.get();
						ingredient.craft(entity);
						entity.markDirty();
						entity.sync();
						world.playSound(null, pos, ThuwumcraftSounds.CAULDRON_ADD_INGREDIENT, SoundCategory.BLOCKS, 0.5F, 0.8F + ((float)Math.random() * 0.4F));
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
		super.onEntityCollision(state, world, pos, entity);
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof CrucibleEntity cauldron)
		{
			if(entity instanceof ItemEntity itemEntity)
			{
				if(this.isPowered(state))
				{
					if(!world.isClient)
					{
						cauldron.setCurrentInput(itemEntity.getStack());
						Optional<AspectIngredient> aspectOptional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.ASPECT_INGREDIENTS, cauldron, world);
						aspectOptional.ifPresent(recipe -> {
							recipe.craft(cauldron);
							ItemStack stack = itemEntity.getStack();
							stack.decrement(1);
							itemEntity.setStack(stack);
							cauldron.sync();
						});
					}
				}
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new CrucibleEntity(pos, state);
	}
}
