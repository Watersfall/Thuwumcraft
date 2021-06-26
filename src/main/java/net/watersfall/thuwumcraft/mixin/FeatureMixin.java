package net.watersfall.thuwumcraft.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.Feature;
import net.watersfall.thuwumcraft.block.ThuwumcraftBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Feature.class)
public class FeatureMixin
{
	@Inject(method = "isSoil(Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
	private static void thuwumcraft$isDeepslateGrass(BlockState state, CallbackInfoReturnable<Boolean> cir)
	{
		if(state.isOf(ThuwumcraftBlocks.DEEPSLATE_GRASS))
		{
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}
