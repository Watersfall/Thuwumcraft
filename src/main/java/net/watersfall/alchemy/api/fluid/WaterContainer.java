package net.watersfall.alchemy.api.fluid;

public interface WaterContainer
{
	/**
	 * Gets the real, server-side water level
	 * @return The real water level
	 */
	short getWaterLevel();

	/**
	 * Sets the real, server-side water level
	 * @param level the new real water level
	 */
	void setWaterLevel(short level);

	/**
	 * Gets the last client-side displayed water level
	 * @return The last displayed water level
	 */
	float getLastWaterLevel();

	/**
	 * Sets the last client-side displayed water level
	 * @param level the new client-side displayed water level
	 */
	void setLastWaterLevel(float level);

	/**
	 * Gets the new client-side displayed water level based on the tickDelta
	 * @param tickDelta The tick delta
	 * @return the new display water level
	 */
	float getAnimationProgress(float tickDelta);

	/**
	 * Gets the max client-side displayable water level
	 * @return the max client-side water level
	 */
	float getMaxDisplayWaterLevel();
}
