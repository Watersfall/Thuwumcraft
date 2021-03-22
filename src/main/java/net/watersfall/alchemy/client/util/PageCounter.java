package net.watersfall.alchemy.client.util;

import net.minecraft.util.math.MathHelper;

public class PageCounter
{
	private int value;
	private final int min;
	private final int max;

	public PageCounter(int value, int min, int max)
	{
		this.min = min;
		this.max = max;
		this.value = value;
	}

	public void increment()
	{
		value = MathHelper.clamp(value + 1, min, max);
	}

	public void decrement()
	{
		value = MathHelper.clamp(value - 1, min, max);
	}

	public int getValue()
	{
		return this.value;
	}

	public int getMin()
	{
		return this.min;
	}

	public int getMax()
	{
		return this.max;
	}
}
