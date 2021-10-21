package net.watersfall.thuwumcraft.gui;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.registry.ThuwumcraftScreenHandlers;
import net.watersfall.wet.api.abilities.AbilityProvider;

public class ResearchBookHandler extends ScreenHandler
{
	public ResearchBookHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf)
	{
		super(ThuwumcraftScreenHandlers.RESEARCH_BOOK_HANDLER, syncId);
		if(buf != null)
		{
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(playerInventory.player);
			provider.addAbility(AbilityProvider.ENTITY_REGISTRY.create(PlayerResearchAbility.ID, buf));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}
}
