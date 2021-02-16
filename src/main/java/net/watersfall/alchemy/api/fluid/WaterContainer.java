package net.watersfall.alchemy.api.fluid;

public interface WaterContainer
{
	short getWaterLevel();

	void setWaterLevel(short level);

	float getLastWaterLevel();

	void setLastWaterLevel(float level);

	float getAnimationProgress(float tickDelta);

	float getMaxDisplayWaterLevel();
}
