package net.watersfall.alchemy.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public class ResearchBookHandler extends ScreenHandler
{
	public ResearchBookHandler(int syncId, PlayerInventory playerInventory)
	{
		super(AlchemyScreenHandlers.RESEARCH_BOOK_HANDLER, syncId);
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}
}
