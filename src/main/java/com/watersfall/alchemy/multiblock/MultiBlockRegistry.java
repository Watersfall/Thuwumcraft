package com.watersfall.alchemy.multiblock;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockRegistry
{
	public static final MultiBlockRegistry INSTANCE = new MultiBlockRegistry();

	private final List<MultiBlock> multiBlocks;
	private MultiBlockRegistry()
	{
		multiBlocks = new ArrayList<>();
	}

	public void add(MultiBlock multiBlock)
	{
		this.multiBlocks.add(multiBlock);
	}

	public void remove(MultiBlock multiBlock)
	{
		this.multiBlocks.remove(multiBlock);
	}

	public void tick()
	{
		this.multiBlocks.forEach((multiBlock -> multiBlock.tick()));
	}
}
