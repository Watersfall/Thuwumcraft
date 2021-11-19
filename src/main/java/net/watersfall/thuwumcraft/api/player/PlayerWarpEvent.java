package net.watersfall.thuwumcraft.api.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerWarpAbility;

@FunctionalInterface
public interface PlayerWarpEvent
{
	/**
	 * Attempts to run a warp event
	 * @param player The player
	 * @param warp The players warp values, for convenience
	 * @return SUCCESS if something happens and the ability should sync,
	 * FAIL if no syncing should happen and no other events should be attempted
	 * PASS if nothing occurs and other events should run
	 */
	ActionResult run(PlayerEntity player, PlayerWarpAbility warp);
}
