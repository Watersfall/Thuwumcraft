package net.watersfall.alchemy.blockentity;

import net.watersfall.alchemy.multiblock.MultiBlockComponent;
import net.minecraft.block.entity.BlockEntity;

public class ChildBlockEntity extends BlockEntity
{
	private MultiBlockComponent component;

	public ChildBlockEntity()
	{
		super(AlchemyModBlockEntities.CHILD_BLOCK_ENTITY);
	}

	public MultiBlockComponent getComponent()
	{
		return component;
	}

	public void setComponent(MultiBlockComponent component)
	{
		this.component = component;
	}
}
