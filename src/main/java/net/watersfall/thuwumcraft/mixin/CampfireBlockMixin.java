package net.watersfall.thuwumcraft.mixin;

import net.minecraft.block.CampfireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.hooks.Hooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
{
	@Inject(method = "spawnSmokeParticle", at = @At("HEAD"), cancellable = true)
	private static void thuwumcraft$cancelSpawnSmokeParticles(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo info)
	{
		Hooks.cancelCampfireSmokeParticles(world, pos, isSignal, lotsOfSmoke, info);
	}
}
