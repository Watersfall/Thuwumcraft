package net.watersfall.thuwumcraft.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.thuwumcraft.hooks.AbilityHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin
{
	@Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 1))
	public void thuwumcraft$tickAbilities(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci)
	{
		AbilityHooks.tickChuckAbilities(chunk);
	}
}
