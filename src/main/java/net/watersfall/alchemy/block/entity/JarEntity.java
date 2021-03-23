package net.watersfall.alchemy.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.aspect.AspectStack;

import java.util.HashMap;

public class JarEntity extends BlockEntity implements AspectInventory, BlockEntityClientSerializable
{
	private static final int MAX_COUNT = 256;
	private final HashMap<Aspect, AspectStack> aspects;
	private ItemStack input;

	public JarEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.JAR_ENTITY, pos, state);
		this.aspects = new HashMap<>();
		input = ItemStack.EMPTY;
	}

	@Override
	public HashMap<Aspect, AspectStack> getAspects()
	{
		return this.aspects;
	}

	@Override
	public ItemStack getCurrentInput()
	{
		return this.input;
	}

	@Override
	public void setCurrentInput(ItemStack stack)
	{
		this.input = stack;
	}

	public boolean canAddAspect(Aspect aspect)
	{
		return this.aspects.size() == 0 || this.aspects.containsKey(aspect);
	}

	@Override
	public boolean canInsert(AspectStack stack, Direction direction)
	{
		return this.aspects.size() == 0 || (this.aspects.size() == 1 && this.aspects.containsKey(stack.getAspect()));
	}

	@Override
	public boolean canExtract(Direction direction)
	{
		return this.aspects.size() > 0;
	}

	@Override
	public void addAspect(AspectStack aspect)
	{
		if(this.aspects.size() == 0)
		{
			this.aspects.put(aspect.getAspect(), aspect);
		}
		else
		{
			if(this.aspects.containsKey(aspect.getAspect()))
			{
				this.aspects.compute(aspect.getAspect(), (type, stack) -> {
					stack.increment(aspect.getCount());
					aspect.decrement(aspect.getCount());
					return stack;
				});
			}
		}
	}

	@Override
	public boolean isEmpty()
	{
		return this.aspects.size() == 0;
	}

	public int getMaxAspectCount()
	{
		return MAX_COUNT;
	}

	@Override
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		fromInventoryTag(tag);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		toInventoryTag(tag);
		return tag;
	}

	@Override
	public void fromClientTag(NbtCompound compoundTag)
	{
		this.aspects.clear();
		fromInventoryTag(compoundTag);
	}

	@Override
	public NbtCompound toClientTag(NbtCompound compoundTag)
	{
		return toInventoryTag(compoundTag);
	}
}
