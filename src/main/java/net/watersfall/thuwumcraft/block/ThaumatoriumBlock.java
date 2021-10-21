package net.watersfall.thuwumcraft.block;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.watersfall.thuwumcraft.block.entity.ThaumatoriumBlockEntity;
import net.watersfall.thuwumcraft.gui.ThaumatoriumHandler;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.util.BlockUtils;
import org.jetbrains.annotations.Nullable;

public class ThaumatoriumBlock extends HorizontalFacingBlock implements BlockEntityProvider
{
	public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
	public static final BooleanProperty POWERED = Properties.POWERED;

	public ThaumatoriumBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER).with(POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(FACING, HALF, POWERED);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		return world.getBlockState(pos.up()).getMaterial().isReplaceable();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack)
	{
		super.onPlaced(world, pos, state, placer, itemStack);
		if(state.get(HALF) == DoubleBlockHalf.LOWER)
		{
			world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER));
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return state.get(HALF) == DoubleBlockHalf.LOWER ? new ThaumatoriumBlockEntity(pos, state) : null;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		super.onBreak(world, pos, state, player);
		if(state.get(HALF) == DoubleBlockHalf.LOWER)
		{
			world.breakBlock(pos.up(), false);
		}
		else
		{
			world.breakBlock(pos.down(), false);
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		BlockState state = super.getPlacementState(ctx);
		if(ctx.getPlayer() != null)
		{
			state = state.with(FACING, ctx.getPlayer().getHorizontalFacing().getOpposite());
		}
		if(ctx.getWorld().getBlockState(ctx.getBlockPos().down()).isOf(ThuwumcraftBlocks.BREWING_CAULDRON))
		{
			state = state.with(POWERED, true);
		}
		return state;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(!world.isClient)
		{
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		}
		return ActionResult.CONSUME;
	}

	@Nullable
	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos)
	{
		return new ExtendedScreenHandlerFactory()
		{
			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf)
			{
				if(state.get(HALF) == DoubleBlockHalf.UPPER)
				{
					buf.writeBlockPos(pos.down());
				}
				else
				{
					buf.writeBlockPos(pos);
				}
			}

			@Override
			public Text getDisplayName()
			{
				return Text.of("test");
			}

			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
			{
				BlockEntity test;
				if(state.get(HALF) == DoubleBlockHalf.UPPER)
				{
					test = world.getBlockEntity(pos.down());
				}
				else
				{
					test = world.getBlockEntity(pos);
				}
				if(test instanceof ThaumatoriumBlockEntity entity)
				{
					return new ThaumatoriumHandler(syncId, inv, entity);
				}
				return null;
			}
		};
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
	{
		return BlockUtils.checkType(type, ThuwumcraftBlockEntities.THAUMATORIUM, world.isClient ? null : ThaumatoriumBlockEntity::tick);
	}
}
