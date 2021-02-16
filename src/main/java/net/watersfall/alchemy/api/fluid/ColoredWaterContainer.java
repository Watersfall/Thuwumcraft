package net.watersfall.alchemy.api.fluid;

public interface ColoredWaterContainer extends WaterContainer
{
	boolean needsColorUpdate();

	int getColor();

	void setColor(int color);
}
