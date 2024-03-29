package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlock;
import net.watersfall.thuwumcraft.api.multiblock.MultiBlockComponent;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceComponent;
import net.watersfall.thuwumcraft.multiblock.multiblock.AlchemicalFurnaceMultiBlock;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;

public class AlchemicalFurnaceEntity extends ChildBlockEntity
{

	private MultiBlockComponent component;
	private MultiBlock<AlchemicalFurnaceComponent> multiBlock;

	public AlchemicalFurnaceEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.ALCHEMICAL_FURNACE, pos, state);
	}

	@Override
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		this.multiBlock = new AlchemicalFurnaceMultiBlock();
		multiBlock.read(tag);
	}

	@Override
	public void writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		if(this.component != null && this.component.getMultiBlock() != null)
		{
			this.component.getMultiBlock().write(tag);
		}
	}

	@Override
	public void fromClientTag(NbtCompound compoundTag)
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
	public NbtCompound toClientTag(NbtCompound compoundTag)
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
