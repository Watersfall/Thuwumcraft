package com.watersfall.alchemy.mixin;

import com.watersfall.alchemy.block.AlchemyModBlocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin
{
	@Inject(method = "spawnSmokeParticle", at = @At("HEAD"), cancellable = true)
	private static void waters_cancelSpawnSmokeParticles(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo info)
	{
		if(world.getBlockState(pos.up()).getBlock() == AlchemyModBlocks.BREWING_CAULDRON_BLOCK)
		{
			info.cancel();
		}
	}
}
