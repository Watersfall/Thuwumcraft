package net.watersfall.alchemy.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin
{
	@Inject(method = "copyFrom", at = @At("TAIL"))
	public void copyAbilities(ServerPlayerEntity player, boolean alive, CallbackInfo info)
	{
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		provider.copy((Entity)(Object)this);
	}
}
