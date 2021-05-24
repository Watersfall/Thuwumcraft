package net.watersfall.thuwumcraft.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.entity.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.block.entity.CraftingHopperEntity;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CraftingHopper extends HorizontalFacingBlock implements BlockEntityProvider
{
	public static final VoxelShape OUTLINE_SHAPE_NORTH;
	public static final VoxelShape OUTLINE_SHAPE_SOUTH;
	public static final VoxelShape OUTLINE_SHAPE_EAST;
	public static final VoxelShape OUTLINE_SHAPE_WEST;
	public static final IntProperty PATTERN = IntProperty.of("pattern", 0, 9);

	static
	{
		OUTLINE_SHAPE_NORTH = Stream.of(
				Block.createCuboidShape(0, 10, 1, 16, 11, 16),
				Block.createCuboidShape(0, 11, 0, 2, 16, 16),
				Block.createCuboidShape(14, 11, 0, 16, 16, 16),
				Block.createCuboidShape(2, 11, 1, 3.5, 16, 2),
				Block.createCuboidShape(12.5, 11, 1, 14, 16, 2),
				Block.createCuboidShape(2, 11, 14, 14, 16, 16),
				Block.createCuboidShape(4, 0, 4, 12, 10, 12),
				Block.createCuboidShape(3.5, 8, 0, 12.5, 16, 2),
				Block.createCuboidShape(0, 10, 0, 3.5, 11, 1),
				Block.createCuboidShape(12.5, 10, 0, 16, 11, 1),
				Block.createCuboidShape(6, 1, 2, 10, 5, 4)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		OUTLINE_SHAPE_SOUTH = Stream.of(
				Block.createCuboidShape(0, 10, 0, 16, 11, 15),
				Block.createCuboidShape(14, 11, 0, 16, 16, 16),
				Block.createCuboidShape(0, 11, 0, 2, 16, 16),
				Block.createCuboidShape(12.5, 11, 14, 14, 16, 15),
				Block.createCuboidShape(2, 11, 14, 3.5, 16, 15),
				Block.createCuboidShape(2, 11, 0, 14, 16, 2),
				Block.createCuboidShape(4, 0, 4, 12, 10, 12),
				Block.createCuboidShape(3.5, 8, 14, 12.5, 16, 16),
				Block.createCuboidShape(12.5, 10, 15, 16, 11, 16),
				Block.createCuboidShape(0, 10, 15, 3.5, 11, 16),
				Block.createCuboidShape(6, 1, 12, 10, 5, 14)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		OUTLINE_SHAPE_EAST = Stream.of(
				Block.createCuboidShape(0, 10, 0, 15, 11, 16),
				Block.createCuboidShape(0, 11, 0, 16, 16, 2),
				Block.createCuboidShape(0, 11, 14, 16, 16, 16),
				Block.createCuboidShape(14, 11, 2, 15, 16, 3.5),
				Block.createCuboidShape(14, 11, 12.5, 15, 16, 14),
				Block.createCuboidShape(0, 11, 2, 2, 16, 14),
				Block.createCuboidShape(4, 0, 4, 12, 10, 12),
				Block.createCuboidShape(14, 8, 3.5, 16, 16, 12.5),
				Block.createCuboidShape(15, 10, 0, 16, 11, 3.5),
				Block.createCuboidShape(15, 10, 12.5, 16, 11, 16),
				Block.createCuboidShape(12, 1, 6, 14, 5, 10)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
		OUTLINE_SHAPE_WEST = Stream.of(
				Block.createCuboidShape(1, 10, 0, 16, 11, 16),
				Block.createCuboidShape(0, 11, 14, 16, 16, 16),
				Block.createCuboidShape(0, 11, 0, 16, 16, 2),
				Block.createCuboidShape(1, 11, 12.5, 2, 16, 14),
				Block.createCuboidShape(1, 11, 2, 2, 16, 3.5),
				Block.createCuboidShape(14, 11, 2, 16, 16, 14),
				Block.createCuboidShape(4, 0, 4, 12, 10, 12),
				Block.createCuboidShape(0, 8, 3.5, 2, 16, 12.5),
				Block.createCuboidShape(0, 10, 12.5, 1, 11, 16),
				Block.createCuboidShape(0, 10, 0, 1, 11, 3.5),
				Block.createCuboidShape(2, 1, 6, 4, 5, 10)
		).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
	}

	public CraftingHopper(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState().with(PATTERN, 0).with(FACING, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(FACING, PATTERN);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return super.getPlacementState(ctx).with(FACING, ctx.getPlayer().getHorizontalFacing().getOpposite());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(!world.isClient)
		{
			this.cyclePattern(world, pos, state);
		}
		return ActionResult.success(world.isClient);
	}

	public void cyclePattern(World world, BlockPos pos, BlockState state)
	{
		int current = state.get(PATTERN) + 1;
		if(current > 9)
		{
			current = 0;
		}
		world.setBlockState(pos, state.with(PATTERN, current));
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new CraftingHopperEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		switch(state.get(FACING))
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

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return world.isClient ? null : checkType(type, ThuwumcraftBlockEntities.CRAFTING_HOPPER, CraftingHopperEntity::tick);
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker)
	{
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}
}
