package net.watersfall.alchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.watersfall.alchemy.block.entity.PhialShelfEntity;
import net.watersfall.alchemy.item.GlassPhialItem;
import net.watersfall.alchemy.util.BlockUtils;
import org.jetbrains.annotations.Nullable;

public class PhialShelfBlock extends Block implements BlockEntityProvider
{
	public static final DirectionProperty DIRECTION = Properties.HORIZONTAL_FACING;
	protected static final VoxelShape OUTLINE_SHAPE_SOUTH = VoxelShapes.cuboid(0, 0, 0, 1, 1, 0.5);
	protected static final VoxelShape OUTLINE_SHAPE_NORTH = VoxelShapes.cuboid(0, 0, 0.5, 1, 1, 1);
	protected static final VoxelShape OUTLINE_SHAPE_WEST = VoxelShapes.cuboid(0.5, 0, 0, 1, 1, 1);
	protected static final VoxelShape OUTLINE_SHAPE_EAST = VoxelShapes.cuboid(0, 0, 0, 0.5, 1, 1);

	public PhialShelfBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState().with(DIRECTION, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(DIRECTION);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return this.getDefaultState().with(DIRECTION, ctx.getPlayerFacing().getOpposite());
	}

	public Direction getDirection(BlockState state)
	{
		return state.get(DIRECTION);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(hit.getSide() == this.getDirection(state))
		{
			ItemStack stack = player.getStackInHand(hand);
			PhialShelfEntity entity = (PhialShelfEntity)world.getBlockEntity(pos);
			int slot = getSlotFromPosition(BlockUtils.getCoordinatesFromHitResult(hit));
			ItemStack invStack = entity.getStack(slot);
			if(stack.isEmpty())
			{
				if(!world.isClient)
				{
					if(invStack.isEmpty())
					{
						return ActionResult.CONSUME;
					}
					else
					{
						invStack = entity.removeStack(slot);
						player.setStackInHand(hand, invStack);
						entity.sync();
					}
				}
				return ActionResult.success(world.isClient);
			}
			else
			{
				if(stack.getItem() instanceof GlassPhialItem)
				{
					if(!world.isClient)
					{
						if(invStack.isEmpty())
						{
							player.setStackInHand(hand, ItemStack.EMPTY);
							entity.setStack(slot, stack);
							entity.sync();
						}
						else
						{
							entity.setStack(slot, stack);
							player.setStackInHand(hand, invStack);
						}
					}
					return ActionResult.success(world.isClient);
				}
			}
		}
		return ActionResult.PASS;
	}

	private int getSlotFromPosition(Vec2f position)
	{
		if(position.y > 0.5)
		{
			if(position.x < 0.33)
			{
				return 0;
			}
			else if(position.x > 0.66)
			{
				return 2;
			}
			else
			{
				return 1;
			}
		}
		else
		{
			if(position.x < 0.33)
			{
				return 3;
			}
			else if(position.x > 0.66)
			{
				return 5;
			}
			else
			{
				return 4;
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new PhialShelfEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		switch(state.get(DIRECTION))
		{
			case NORTH:
				return OUTLINE_SHAPE_NORTH;
			case SOUTH:
				return OUTLINE_SHAPE_SOUTH;
			case EAST:
				return OUTLINE_SHAPE_EAST;
			default:
				return OUTLINE_SHAPE_WEST;
		}
	}
}
