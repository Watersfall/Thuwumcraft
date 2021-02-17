package net.watersfall.alchemy.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.multiblock.MultiBlock;
import net.watersfall.alchemy.api.multiblock.MultiBlockComponent;
import net.watersfall.alchemy.multiblock.component.AlchemicalFurnaceComponent;
import net.watersfall.alchemy.multiblock.multiblock.AlchemicalFurnaceMultiBlock;

public class AlchemicalFurnaceEntity extends ChildBlockEntity implements BlockEntityClientSerializable
{

	private MultiBlockComponent component;
	private MultiBlock<AlchemicalFurnaceComponent> multiBlock;

	public AlchemicalFurnaceEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.ALCHEMICAL_FURNACE_ENTITY, pos, state);
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		this.multiBlock = new AlchemicalFurnaceMultiBlock();
		multiBlock.read(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		if(this.component != null && this.component.getMultiBlock() != null)
		{
			this.component.getMultiBlock().write(tag);
		}
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		if(this.multiBlock == null)
		{
			this.multiBlock = new AlchemicalFurnaceMultiBlock();
			this.multiBlock.read(compoundTag);
			this.multiBlock.add(this.world, this.pos);
		}
		else
		{
			this.multiBlock.read(compoundTag);
		}
		for(int i = 0; i < this.multiBlock.getComponents().length; i++)
		{
			BlockEntity testEntity = this.world.getBlockEntity(this.multiBlock.getComponents()[i].getPos());
			if(testEntity instanceof ChildBlockEntity)
			{
				ChildBlockEntity entity = (ChildBlockEntity)testEntity;
				entity.setComponent(this.multiBlock.getComponents()[i]);
			}
		}
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		if(this.component != null)
		{
			return this.component.getMultiBlock().write(compoundTag);
		}
		else if(this.multiBlock != null)
		{
			return this.multiBlock.write(compoundTag);
		}
		else
		{
			return compoundTag;
		}
	}

	@Override
	public void setWorld(World world)
	{
		super.setWorld(world);
		if(this.multiBlock != null)
		{
			this.multiBlock.add(world, pos);
		}
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
