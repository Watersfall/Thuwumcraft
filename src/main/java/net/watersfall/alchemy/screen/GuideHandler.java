package net.watersfall.alchemy.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.watersfall.alchemy.AlchemyMod;

public class GuideHandler extends ScreenHandler
{
	public GuideHandler(int syncId, PlayerInventory playerInventory)
	{
		super(AlchemyMod.GUIDE_HANDLER, syncId);
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return true;
	}
}
