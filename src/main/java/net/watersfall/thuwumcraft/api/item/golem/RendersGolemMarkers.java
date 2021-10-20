package net.watersfall.thuwumcraft.api.item.golem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.watersfall.thuwumcraft.api.golem.GolemMarker;

public interface RendersGolemMarkers
{
	default boolean shouldRenderMarkers(ItemStack stack, PlayerEntity player)
	{
		return true;
	}

	default boolean shouldRenderMarker(ItemStack stack, PlayerEntity player, GolemMarker marker)
	{
		return true;
	}
}
