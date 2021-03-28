package net.watersfall.alchemy.block;

import net.minecraft.recipe.Recipe;
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.block.entity.BrewingCauldronEntity;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.CauldronIngredient;
import net.watersfall.alchemy.recipe.CauldronIngredientRecipe;
import net.watersfall.alchemy.recipe.CauldronItemRecipe;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Optional;

public class BrewingCauldronBlock extends AbstractCauldronBlock implements BlockEntityProvider
{
	public BrewingCauldronBlock(Settings settings)
	{
		super(settings);
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof BrewingCauldronEntity)
		{
			BrewingCauldronEntity cauldron = (BrewingCauldronEntity)test;
			if(entity instanceof LivingEntity)
			{
				if(!world.isClient && entity.isOnFire() && cauldron.getWaterLevel() > 333)
				{
					entity.extinguish();
					cauldron.setWaterLevel((short) (cauldron.getWaterLevel() - 333));
					cauldron.sync();
				}
			}
			else if(entity instanceof ItemEntity)
			{
				if(this.isPowered(state))
				{
					if(!world.isClient)
					{
						if(cauldron.getIngredientCount() < 3)
						{
							ItemEntity itemEntity = (ItemEntity)entity;
							Item item = itemEntity.getStack().getItem();
							if(cauldron.count(item) <= 0)
							{
								itemEntity.getStack().decrement(1);
								cauldron.addStack(new ItemStack(item));
								cauldron.setIngredientCount((byte) (cauldron.getIngredientCount() + 1));
								cauldron.sync();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ActionResult result = super.onUse(state, world, pos, player, hand, hit);
		if(result != ActionResult.PASS)
		{
			return result;
		}
		ItemStack itemStack = player.getStackInHand(hand);
		Item item = itemStack.getItem();
		BrewingCauldronEntity entity = (BrewingCauldronEntity) world.getBlockEntity(pos);
		if(this.isPowered(state) && entity.getWaterLevel() > 0)
		{
			ItemStack inputStack = new ItemStack(item);
			entity.setInput(inputStack);
			Optional<CauldronIngredientRecipe> typeOptional = world.getRecipeManager().getFirstMatch(AlchemyRecipes.CAULDRON_INGREDIENT_RECIPE, entity, world);
			if(typeOptional.isPresent())
			{
				CauldronIngredientRecipe typeRecipe = typeOptional.get();
				if(typeRecipe.playerHasResearch(player))
				{
					if(!world.isClient)
					{
						Optional<CauldronIngredient> optional = world.getRecipeManager().getFirstMatch(AlchemyRecipes.CAULDRON_INGREDIENTS, entity.withInput(0), world);
						if(!optional.isPresent())
						{
							player.sendMessage(new TranslatableText("block.waters_alchemy_mod.cauldron.invalid_recipe").formatted(Formatting.GRAY, Formatting.ITALIC), true);
							return ActionResult.FAIL;
						}
						ItemStack stack = optional.get().craft(entity, typeRecipe, world);
						if(inputStack == stack)
						{
							player.sendMessage(new TranslatableText("block.waters_alchemy_mod.cauldron.invalid_recipe").formatted(Formatting.GRAY, Formatting.ITALIC), true);
							return ActionResult.FAIL;
						}
						else
						{
							itemStack.decrement(1);
							if(itemStack.isEmpty())
							{
								player.setStackInHand(hand, stack);
							}
							else if(!player.getInventory().insertStack(stack))
							{
								player.dropItem(stack, true);
							}
							entity.setInput(ItemStack.EMPTY);
							entity.setWaterLevel((short) (entity.getWaterLevel() - typeRecipe.getWaterUse()));
							entity.sync();
						}
					}
					return ActionResult.success(world.isClient);
				}
			}
			Optional<CauldronItemRecipe> itemOptional = world.getRecipeManager().getFirstMatch(AlchemyRecipes.CAULDRON_ITEM_RECIPE, entity, world);
			if(itemOptional.isPresent())
			{
				CauldronItemRecipe recipe = itemOptional.get();
				if(recipe.playerHasResearch(player))
				{
					if(!world.isClient)
					{
						ItemStack stack = recipe.craft(entity);
						itemStack.decrement(1);
						if(itemStack.isEmpty())
						{
							player.setStackInHand(hand, stack);
						}
						else if(!player.getInventory().insertStack(stack))
						{
							player.dropItem(stack, true);
						}
						entity.setInput(ItemStack.EMPTY);
						entity.setWaterLevel((short) (entity.getWaterLevel() - recipe.getWaterUse()));
						entity.sync();
					}
					return ActionResult.success(world.isClient);
				}
			}
			entity.setInput(ItemStack.EMPTY);
			if(entity.count(item) > 0)
			{
				if(!world.isClient)
				{
					if(entity.addItem(item))
					{
						if(!player.getAbilities().creativeMode)
						{
							itemStack.decrement(1);
						}
						world.playSound(null, pos, AlchemySounds.CAULDRON_ADD_INGREDIENT, SoundCategory.BLOCKS, 0.5F, 0.8F + ((float)Math.random() * 0.4F));
						entity.sync();
					}
				}
				return ActionResult.success(world.isClient);
			}
			else if(entity.getIngredientCount() < 3)
			{
				if(!world.isClient)
				{
					entity.addStack(new ItemStack(item));
					if(!player.getAbilities().creativeMode)
					{
						itemStack.decrement(1);
					}
					entity.setIngredientCount((byte) (entity.getIngredientCount() + 1));
					world.playSound(null, pos, AlchemySounds.CAULDRON_ADD_INGREDIENT, SoundCategory.BLOCKS, 0.5F, 0.8F + ((float)Math.random() * 0.4F));
					entity.sync();
				}
				return ActionResult.success(world.isClient);
			}
			return ActionResult.CONSUME;
		}
		if(entity.getWaterLevel() <= 5)
		{
			entity.setWaterLevel((short) 0);
		}
		return ActionResult.PASS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new BrewingCauldronEntity(pos, state);
	}
}
