package net.watersfall.thuwumcraft.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.BlockSource;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.watersfall.thuwumcraft.hooks.Hooks;
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
	@Inject(method = "generate",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 3, shift = At.Shift.AFTER, by = 1),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void thuwumcraft$addLavaCrystals(FeatureContext<SingleStateFeatureConfig> context,
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
		Hooks.addLavaCrystals(context, pos, x, y, z, random);
	}

	@Inject(method = "generate",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;", ordinal = 7),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void thuwumcraft$addWaterCrystals(FeatureContext<SingleStateFeatureConfig> context,
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
		Hooks.addWaterCrystals(world, blockPos, x, z, random);
	}
}
