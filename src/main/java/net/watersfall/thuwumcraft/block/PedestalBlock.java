package net.watersfall.thuwumcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.entity.PedestalEntity;
import net.watersfall.thuwumcraft.block.entity.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.recipe.PedestalRecipe;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.util.BlockUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class PedestalBlock extends Block implements BlockEntityProvider
{
	protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(
			createCuboidShape(2D, 0D, 2D, 14D, 2D, 14D),
			createCuboidShape(4D, 2D, 4D, 12D, 10D, 12D),
			createCuboidShape(3D, 10D, 3D, 13D, 12D, 13D),
			createCuboidShape(6D, 12D, 6D, 10D, 15D, 10D)
	);

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
			if(!entity.isMain() && !entity.isCrafting())
			{
				if(playerStack.getItem() == ThuwumcraftItems.WITCHY_SPOON_ITEM)
				{
					Optional<PedestalRecipe> recipeOptional = world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.PEDESTAL_RECIPE, entity, world);
					if(recipeOptional.isPresent())
					{
						PedestalRecipe recipe = recipeOptional.get();
						if(recipe.playerHasResearch(player))
						{
							if(!world.isClient)
							{
								entity.beginCraft(recipe);
							}
							return ActionResult.success(world.isClient);
						}
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
							ItemStack newStack = playerStack.copy();
							newStack.setCount(1);
							entity.setStack(newStack);
							playerStack.decrement(1);
							if(!entityStack.isEmpty())
							{
								if(playerStack.isEmpty())
								{
									player.setStackInHand(hand, entityStack);
								}
								else if(!player.getInventory().insertStack(entityStack))
								{
									player.dropItem(entityStack, false, true);
								}
							}
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
							if(!player.getInventory().insertStack(entity.getStack().copy()))
							{
								player.dropItem(entity.getStack().copy(), false, true);
							}
							entity.setStack(ItemStack.EMPTY);
						}
						return ActionResult.success(world.isClient);
					}
					return ActionResult.FAIL;
				}
			}
		}
		return ActionResult.FAIL;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		double x = (double)pos.getX() + 0.5D;
		double y = (double)pos.getY() + 1.0625D;
		double z = (double)pos.getZ() + 0.5D;
		world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
		BlockEntity testEntity = world.getBlockEntity(pos);
		if(testEntity instanceof PedestalEntity)
		{
			PedestalEntity entity = (PedestalEntity)testEntity;
			if(entity.isMain())
			{
				for(int i = 0; i  < 4; i++)
				{
					world.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, random.nextDouble() - 0.5D, random.nextDouble() - 0.5D, random.nextDouble() - 0.5D);
				}
			}
			if(entity.isCrafting())
			{
				for(int i = 0; i  < 4; i++)
				{
					world.addParticle(ParticleTypes.WITCH, x, y, z, random.nextDouble() - 0.5D, random.nextDouble() - 0.5D, random.nextDouble() - 0.5D);
				}
			}
		}
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE_SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new PedestalEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return BlockUtils.checkType(type, ThuwumcraftBlockEntities.PEDESTAL_ENTITY, PedestalEntity::tick);
	}
}
