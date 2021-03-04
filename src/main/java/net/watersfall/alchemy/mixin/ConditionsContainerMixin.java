package net.watersfall.alchemy.mixin;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.entity.Entity;
import net.watersfall.alchemy.accessor.waters_PlayerAdvancementTrackerAccessor;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Criterion.ConditionsContainer.class)
public class ConditionsContainerMixin
{
	@Shadow @Final private String id;

	@Inject(method = "grant", at = @At("TAIL"))
	public void grantResearchCriterion(PlayerAdvancementTracker tracker, CallbackInfo info)
	{
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(((waters_PlayerAdvancementTrackerAccessor)tracker).getOwner());
		provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).ifPresent(ability -> {
			ability.grantCriterion(this.id);
			ability.sync(((waters_PlayerAdvancementTrackerAccessor)tracker).getOwner());
		});
	}
}
