package net.watersfall.alchemy.mixin;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.watersfall.alchemy.accessor.waters_PlayerAdvancementTrackerAccessor;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin implements waters_PlayerAdvancementTrackerAccessor
{
	@Shadow private ServerPlayerEntity owner;

	@Override
	public ServerPlayerEntity getOwner()
	{
		return this.owner;
	}

	@Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
	public void checkIsDone(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir)
	{
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(this.getOwner());
		provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).ifPresent((ability -> {
			ability.addAdvancement(advancement.getId());
		}));
	}
}
