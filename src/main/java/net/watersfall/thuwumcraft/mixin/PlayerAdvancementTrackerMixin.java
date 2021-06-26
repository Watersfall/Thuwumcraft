package net.watersfall.thuwumcraft.mixin;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin
{
	@Shadow private ServerPlayerEntity owner;

	@Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
	public void thuwumcraft$checkIsDone(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir)
	{
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(this.owner);
		provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).ifPresent((ability -> {
			ability.addAdvancement(advancement.getId());
		}));
	}
}
