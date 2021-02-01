package net.watersfall.alchemy.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.alchemy.block.ChildBlock;
import net.watersfall.alchemy.multiblock.MultiBlock;
import net.watersfall.alchemy.multiblock.MultiBlockComponent;
import net.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceComponent;
import net.watersfall.alchemy.multiblock.impl.multiblock.AlchemicalFurnaceMultiBlock;

public class AlchemicalFurnaceEntity extends ChildBlockEntity implements BlockEntityClientSerializable
{

	private MultiBlockComponent component;
	private MultiBlock<AlchemicalFurnaceComponent> multiBlock;

	public AlchemicalFurnaceEntity()
	{
		super(AlchemyModBlockEntities.ALCHEMICAL_FURNACE_ENTITY);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag)
	{
		super.fromTag(state, tag);
		this.multiBlock = new AlchemicalFurnaceMultiBlock();
		multiBlock.read(state, tag);
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
			this.multiBlock.read(null, compoundTag);
			this.multiBlock.add(this.world, this.pos);
		}
		else
		{
			this.multiBlock.read(null, compoundTag);
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
	public void setLocation(World world, BlockPos pos)
	{
		super.setLocation(world, pos);
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
