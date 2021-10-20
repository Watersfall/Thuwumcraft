package net.watersfall.thuwumcraft.api.item.golem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.watersfall.thuwumcraft.entity.golem.GolemEntity;

public interface RendersGolemOutlines
{
	default boolean shouldRenderOutlines(ItemStack stack, PlayerEntity player)
	{
		return true;
	}

	default boolean shouldRenderOutline(ItemStack stack, PlayerEntity player, GolemEntity golem)
	{
		return true;
	}
}
