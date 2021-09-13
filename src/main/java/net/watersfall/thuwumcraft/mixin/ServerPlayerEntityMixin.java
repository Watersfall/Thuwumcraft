package net.watersfall.thuwumcraft.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.watersfall.thuwumcraft.hooks.AbilityHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin
{
	@Inject(method = "copyFrom", at = @At("TAIL"))
	public void thuwumcraft$copyAbilities(ServerPlayerEntity player, boolean alive, CallbackInfo info)
	{
		AbilityHooks.serverPlayerCopyAbilities(player, (ServerPlayerEntity)(Object)this, alive);
	}
}
