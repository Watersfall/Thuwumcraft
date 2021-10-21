package net.watersfall.thuwumcraft.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.sound.ThuwumcraftSounds;
import net.watersfall.thuwumcraft.block.entity.BrewingCauldronEntity;
import net.watersfall.thuwumcraft.recipe.CauldronIngredient;
import net.watersfall.thuwumcraft.recipe.CauldronIngredientRecipe;
import net.watersfall.thuwumcraft.recipe.CauldronItemRecipe;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.util.InventoryHelper;

import java.util.Optional;

public class BrewingCauldronBlock extends AbstractCauldronBlock implements BlockEntityProvider
{
	public BrewingCauldronBlock(Settings settings)
	{
		super(settings);
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
	{
		super.onEntityCollision(state, world, pos, entity);
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof BrewingCauldronEntity cauldron)
		{
			if(entity instanceof ItemEntity)
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
		BlockEntity test = world.getBlockEntity(pos);
		if(test instanceof BrewingCauldronEntity entity)
		{
			if(this.isPowered(state) && entity.getWaterLevel() > 0)
			{
				ItemStack inputStack = new ItemStack(item);
				entity.setInput(inputStack);
				Optional<CauldronIngredientRecipe> typeOptional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.CAULDRON_INGREDIENT, entity, world);
				if(typeOptional.isPresent())
				{
					CauldronIngredientRecipe typeRecipe = typeOptional.get();
					if(typeRecipe.playerHasResearch(player))
					{
						if(!world.isClient)
						{
							Optional<CauldronIngredient> optional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.CAULDRON_INGREDIENTS, entity.withInput(0), world);
							if(!optional.isPresent())
							{
								player.sendMessage(new TranslatableText("block.thuwumcraft.cauldron.invalid_recipe").formatted(Formatting.GRAY, Formatting.ITALIC), true);
								return ActionResult.FAIL;
							}
							entity.setInput(inputStack);
							ItemStack stack = optional.get().craft(entity, typeRecipe, world);
							if(inputStack == stack)
							{
								player.sendMessage(new TranslatableText("block.thuwumcraft.cauldron.invalid_recipe").formatted(Formatting.GRAY, Formatting.ITALIC), true);
								return ActionResult.FAIL;
							}
							else
							{
								InventoryHelper.useItem(itemStack, player, hand, 1, stack);
								entity.setInput(ItemStack.EMPTY);
								entity.setWaterLevel((short) (entity.getWaterLevel() - typeRecipe.getWaterUse()));
								entity.sync();
							}
						}
						return ActionResult.success(world.isClient);
					}
				}
				Optional<CauldronItemRecipe> itemOptional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.CAULDRON_ITEM, entity, world);
				if(itemOptional.isPresent())
				{
					CauldronItemRecipe recipe = itemOptional.get();
					if(recipe.playerHasResearch(player))
					{
						if(!world.isClient)
						{
							ItemStack stack = recipe.craft(entity);
							InventoryHelper.useItem(itemStack, player, hand, 1, stack);
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
							world.playSound(null, pos, ThuwumcraftSounds.CAULDRON_ADD_INGREDIENT, SoundCategory.BLOCKS, 0.5F, 0.8F + ((float)Math.random() * 0.4F));
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
						world.playSound(null, pos, ThuwumcraftSounds.CAULDRON_ADD_INGREDIENT, SoundCategory.BLOCKS, 0.5F, 0.8F + ((float)Math.random() * 0.4F));
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
		}
		return ActionResult.PASS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new BrewingCauldronEntity(pos, state);
	}
}
