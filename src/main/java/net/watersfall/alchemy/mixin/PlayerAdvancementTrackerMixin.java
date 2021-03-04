package net.watersfall.alchemy.mixin;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.watersfall.alchemy.accessor.waters_PlayerAdvancementTrackerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin implements waters_PlayerAdvancementTrackerAccessor
{
	@Shadow private ServerPlayerEntity owner;

	@Override
	public ServerPlayerEntity getOwner()
	{
		return this.owner;
	}
}
