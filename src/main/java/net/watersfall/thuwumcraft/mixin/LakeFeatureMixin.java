package net.watersfall.thuwumcraft.mixin;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

//Is this the wrong way to do this?
//Probably
//Do I care?
//Nope
@Mixin(LakeFeature.class)
public class LakeFeatureMixin
{
	private static BlockState[] fireCrystals = null;
	private static BlockState[] waterCrystals = null;

	@Inject(method = "generate",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 3, shift = At.Shift.AFTER, by = 1),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void addLavaCrystals(FeatureContext<SingleStateFeatureConfig> context,
							CallbackInfoReturnable<Boolean> info,
							BlockPos pos,
							StructureWorldAccess world,
							Random random,
							SingleStateFeatureConfig config,
							boolean bls[],
							BlockSource blockSource,
							int x,
							int z,
							int y,
							BlockState state,
							BlockPos pos4)
	{
		if((context.getWorld().getLunarTime() & 0x100) == 0)
		{
			for(Direction direction : Direction.values())
			{
				BlockPos currentPos = pos.add(x, y, z).offset(direction);
				if(world.isAir(currentPos) && !bordersFluid(world, currentPos, Fluids.LAVA) && random.nextInt(10) == 0)
				{
					world.setBlockState(currentPos, getFireCrystal(random, direction), 2);
				}
			}
		}

	}

	@Inject(method = "generate",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;", ordinal = 7),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void addWaterCrystals(FeatureContext<SingleStateFeatureConfig> context,
								 CallbackInfoReturnable<Boolean> info,
								 BlockPos blockPos,
								 StructureWorldAccess world,
								 Random random,
								 SingleStateFeatureConfig config,
								 int x,
								 int z,
								 int fakeY
	)
	{
		if((world.getLunarTime() & 0x10) == 0)
		{
			for(int y = 0; y < 4; y++)
			{
				BlockPos currentPos = blockPos.add(x, y, z);
				FluidState fluid = world.getFluidState(currentPos);
				if(fluid.getFluid() == Fluids.WATER)
				{
					for(Direction direction : Direction.values())
					{
						BlockState state = world.getBlockState(currentPos.offset(direction));
						if(random.nextInt(10) == 0 && state.getMaterial().isSolid() && state.getMaterial() != Material.AMETHYST)
						{
							world.setBlockState(currentPos, getWaterCrystal(random, direction.getOpposite()), 2);
						}
					}
				}
			}
		}
	}

	private boolean bordersFluid(StructureWorldAccess world, BlockPos pos, Fluid fluid)
	{
		return world.getFluidState(pos).getFluid() == fluid ||
				world.getFluidState(pos.up()).getFluid() == fluid ||
				world.getFluidState(pos.down()).getFluid() == fluid ||
				world.getFluidState(pos.north()).getFluid() == fluid ||
				world.getFluidState(pos.south()).getFluid() == fluid ||
				world.getFluidState(pos.east()).getFluid() == fluid ||
				world.getFluidState(pos.west()).getFluid() == fluid;
	}

	private BlockState getFireCrystal(Random random, Direction direction)
	{
		if(fireCrystals == null)
		{
			fireCrystals = new BlockState[]{
					Aspects.ASPECT_TO_CLUSTER.get(Aspects.FIRE).getDefaultState(),
					Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.FIRE).getDefaultState(),
					Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.FIRE).getDefaultState(),
					Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.FIRE).getDefaultState()
			};
		}
		return fireCrystals[random.nextInt(fireCrystals.length)].with(AmethystClusterBlock.FACING, direction);
	}

	private BlockState getWaterCrystal(Random random, Direction direction)
	{
		if(waterCrystals == null)
		{
			waterCrystals = new BlockState[]{
					Aspects.ASPECT_TO_CLUSTER.get(Aspects.WATER).getDefaultState(),
					Aspects.ASPECT_TO_LARGE_CLUSTER.get(Aspects.WATER).getDefaultState(),
					Aspects.ASPECT_TO_MEDIUM_CLUSTER.get(Aspects.WATER).getDefaultState(),
					Aspects.ASPECT_TO_SMALL_CLUSTER.get(Aspects.WATER).getDefaultState()
			};
		}
		return waterCrystals[random.nextInt(waterCrystals.length)].with(AmethystClusterBlock.FACING, direction).with(Properties.WATERLOGGED, true);
	}
}
