package net.watersfall.alchemy.api.fluid;

public interface ColoredWaterContainer extends WaterContainer
{
	/**
	 * Returns true if this container needs to recalculate it's water color,
	 * or false if it does not
	 * @return Whether a color update is needed
	 */
	boolean needsColorUpdate();

	/**
	 * Get the 0xAARRGGBB color value
	 * @return the color
	 */
	int getColor();

	/**
	 * Sets the 0xAARRGGBB color value
	 * @param color the new color
	 */
	void setColor(int color);
}
