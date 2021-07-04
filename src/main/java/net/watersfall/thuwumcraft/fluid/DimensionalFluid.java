package net.watersfall.thuwumcraft.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.registry.ThuwumcraftFluids;

public abstract class DimensionalFluid extends FlowableFluid
{
	@Override
	public Fluid getFlowing()
	{
		return ThuwumcraftFluids.DIMENSIONAL_FLOWING;
	}

	@Override
	public Fluid getStill()
	{
		return ThuwumcraftFluids.DIMENSIONAL_STILL;
	}

	@Override
	public Item getBucketItem()
	{
		return ThuwumcraftItems.DIMENSIONAL_FLUID_BUCKET;
	}

	public BlockState toBlockState(FluidState state)
	{
		return ThuwumcraftBlocks.DIMENSIONAL_FLUID_BLOCK.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
	}

	public boolean matchesType(Fluid fluid)
	{
		return fluid == getFlowing() || fluid == getStill();
	}

	@Override
	protected boolean isInfinite()
	{
		return false;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state)
	{
		BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}

	@Override
	protected int getFlowSpeed(WorldView world)
	{
		return 10;
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView world)
	{
		return 2;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction)
	{
		return false;
	}

	@Override
	public int getTickRate(WorldView world)
	{
		return 5;
	}

	@Override
	protected float getBlastResistance()
	{
		return 500F;
	}

	public static class Flowing extends DimensionalFluid
	{
		@Override
		public boolean isStill(FluidState state)
		{
			return false;
		}

		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder)
		{
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		public int getLevel(FluidState state)
		{
			return state.get(LEVEL);
		}
	}

	public static class Still extends DimensionalFluid
	{
		public int getLevel(FluidState state)
		{
			return 8;
		}

		public boolean isStill(FluidState state)
		{
			return true;
		}
	}
}
