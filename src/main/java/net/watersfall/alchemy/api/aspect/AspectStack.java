package net.watersfall.alchemy.api.aspect;

public class AspectStack
{
	public static final AspectStack EMPTY = new AspectStack(null, 1);

	private final Aspect aspect;
	private int count;

	public AspectStack(Aspect aspect, int amount)
	{
		this.aspect = aspect;
		this.count = amount;
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}

	public int getCount()
	{
		return this.count;
	}

	public int setCount(int amount)
	{
		this.count = amount;
		return this.count;
	}

	public int increment(int amount)
	{
		return setCount(getCount() + amount);
	}

	public int decrement(int amount)
	{
		return increment(-amount);
	}

	public boolean isEmpty()
	{
		return this.aspect == null || this.count < 1;
	}
}