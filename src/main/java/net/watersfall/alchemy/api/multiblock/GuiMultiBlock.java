package net.watersfall.alchemy.api.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface GuiMultiBlock<T extends MultiBlockComponent> extends MultiBlock<T>, NamedScreenHandlerFactory
{
	void openScreen(World world, BlockPos pos, PlayerEntity player);
}
