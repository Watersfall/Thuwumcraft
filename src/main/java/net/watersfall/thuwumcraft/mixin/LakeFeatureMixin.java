package net.watersfall.thuwumcraft.mixin;

import net.minecraft.world.gen.feature.LakeFeature;
import org.spongepowered.asm.mixin.Mixin;

//Is this the wrong way to do this?
//Probably
//Do I care?
//Nope
@Mixin(LakeFeature.class)
public class LakeFeatureMixin
{
//	@Inject(method = "generate",
//			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1, shift = At.Shift.AFTER, by = 1),
//			locals = LocalCapture.CAPTURE_FAILHARD
//	)
//	public void thuwumcraft$addLavaCrystals(FeatureContext<SingleStateFeatureConfig> context,
//							CallbackInfoReturnable<Boolean> info,
//							BlockPos pos,
//							StructureWorldAccess world,
//							Random random,
//							SingleStateFeatureConfig config,
//							boolean bls[],
//							BlockSource blockSource,
//							int x,
//							int z,
//							int y,
//							BlockState state,
//							BlockPos pos4)
//	{
//		Hooks.addLavaCrystals(context, pos, x, y, z, random);
//	}
//
//	@Inject(method = "generate",
//			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;", ordinal = 5),
//			locals = LocalCapture.CAPTURE_FAILHARD
//	)
//	public void thuwumcraft$addWaterCrystals(FeatureContext<SingleStateFeatureConfig> context,
//								 CallbackInfoReturnable<Boolean> info,
//								 BlockPos blockPos,
//								 StructureWorldAccess world,
//								 Random random,
//								 SingleStateFeatureConfig config,
//								 int x,
//								 int z,
//								 int fakeY
//	)
//	{
//		Hooks.addWaterCrystals(world, blockPos, x, z, random);
//	}
}
