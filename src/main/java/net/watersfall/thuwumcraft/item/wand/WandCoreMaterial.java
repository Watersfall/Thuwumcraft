package net.watersfall.thuwumcraft.item.wand;

import net.minecraft.util.Identifier;

public interface WandCoreMaterial extends WandComponent
{
	double getMaxVis();

	Identifier getId();

	int getColor();
}
