package net.watersfall.thuwumcraft.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import net.watersfall.thuwumcraft.block.entity.ArcaneSealBlockEntity;
import net.watersfall.thuwumcraft.item.ArcaneRuneItem;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.util.BlockUtils;
import org.jetbrains.annotations.Nullable;

public class ArcaneSealBlock extends FacingBlock implements BlockEntityProvider, BlockColorProvider
{
	public static final IntProperty RUNES = IntProperty.of("runes", 0, 3);
	public static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.createCuboidShape(4, 14, 4, 12, 16, 12),
			Block.createCuboidShape(4, 0, 4, 12, 2, 12),
			Block.createCuboidShape(4, 4, 14, 12, 12, 16),
			Block.createCuboidShape(4, 4, 0, 12, 12, 2),
			Block.createCuboidShape(14, 4, 4, 16, 12, 12),
			Block.createCuboidShape(0, 4, 4, 2, 12, 12)
	};

	public ArcaneSealBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState().with(RUNES, 0));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack)
	{
		super.onPlaced(world, pos, state, placer, itemStack);
		if(placer instanceof PlayerEntity player)
		{
			world.addBlockEntity(new ArcaneSealBlockEntity(pos, state, player));
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return this.getDefaultState().with(FACING, ctx.getSide());
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		Direction direction = state.get(FACING);
		return world.getBlockState(pos.offset(direction.getOpposite())).isSideSolidFullSquare(world, pos.offset(direction.getOpposite()), direction.getOpposite());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos)
	{
		if(direction == state.get(FACING).getOpposite())
		{
			world.breakBlock(pos, true);
		}
		return state;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(FACING, RUNES);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(world.getBlockEntity(pos) instanceof ArcaneSealBlockEntity entity)
		{
			ItemStack handStack = player.getStackInHand(hand);
			if(handStack.isEmpty())
			{
				if(!world.isClient)
				{
					entity.clear();
					world.setBlockState(pos, state.with(RUNES, 0));
					entity.sync();
				}
				world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F);
				return ActionResult.success(world.isClient);
			}
			if(handStack.getItem() instanceof ArcaneRuneItem)
			{
				if(entity.canAddRune(handStack))
				{
					entity.addRune(handStack);
					if(!world.isClient)
					{
						increaseRuneCount(world, pos, state);
						entity.sync();
					}
					world.playSound(player, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 0.75F, 1.0F);
					return ActionResult.success(world.isClient);
				}
			}
		}
		return ActionResult.PASS;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new ArcaneSealBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return BlockUtils.checkType(type, ThuwumcraftBlockEntities.ARCANE_SEAL, ArcaneSealBlockEntity::tick);
	}

	public int getRuneCount(BlockState state)
	{
		return state.get(RUNES);
	}

	public void increaseRuneCount(World world, BlockPos pos, BlockState state)
	{
		world.setBlockState(pos, state.with(RUNES, getRuneCount(state) + 1));
	}

	@Override
	public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex)
	{
		if(world != null && pos != null && getRuneCount(state) > 0)
		{
			if(world.getBlockEntity(pos) instanceof ArcaneSealBlockEntity entity)
			{
				return entity.getColor(tintIndex);
			}
		}
		return 0;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return SHAPES[state.get(FACING).getId()];
	}
}
