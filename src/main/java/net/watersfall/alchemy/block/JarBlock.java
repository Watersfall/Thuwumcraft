package net.watersfall.alchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.block.entity.JarEntity;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.AspectIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JarBlock extends Block implements BlockEntityProvider
{
	protected static final VoxelShape OUTLINE_SHAPE;

	static
	{
		OUTLINE_SHAPE = VoxelShapes.union(
				createCuboidShape(3, 0, 3, 13, 12, 13),
				createCuboidShape(5, 12, 5, 11, 14, 11)
		);
	}

	public JarBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ItemStack stack = player.getStackInHand(hand);
		if(!stack.isEmpty())
		{
			JarEntity entity = (JarEntity) world.getBlockEntity(pos);
			if(entity != null)
			{
				entity.setCurrentInput(stack);
				Optional<AspectIngredient> ingredientOptional = world.getRecipeManager().getFirstMatch(AlchemyRecipes.ASPECT_INGREDIENTS, entity, world);
				if(ingredientOptional.isPresent())
				{
					if(!world.isClient)
					{
						AspectIngredient ingredient = ingredientOptional.get();
						ingredient.craft(entity);
						stack.decrement(1);
						entity.markDirty();
						entity.sync();
					}
					return ActionResult.success(world.isClient);
				}
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos)
	{
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE_SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new JarEntity(pos, state);
	}
}
