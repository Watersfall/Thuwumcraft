package net.watersfall.thuwumcraft.mixin;

import net.minecraft.block.Block;
import net.watersfall.thuwumcraft.block.AbstractCauldronBlock;
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
	private static void thuwumcraft$cancelSpawnSmokeParticles(World world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo info)
	{
		Block block = world.getBlockState(pos.up()).getBlock();
		if(block instanceof AbstractCauldronBlock)
		{
			info.cancel();
		}
	}
}
