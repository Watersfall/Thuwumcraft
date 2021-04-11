package net.watersfall.alchemy.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.lookup.AspectContainer;
import net.watersfall.alchemy.block.PipeBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class JarEntity extends BlockEntity implements AspectInventory, BlockEntityClientSerializable, AspectContainer
{
	private static final int MAX_COUNT = 256;
	private final HashMap<Aspect, AspectStack> aspects;
	private ItemStack input;
	private final List<BlockPos> visited = new ArrayList<>();

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

	private static AspectStack followPipe(World world, BlockPos pos, Direction from, Aspect aspect, JarEntity start)
	{
		start.visited.add(pos);
		AspectContainer container = AspectContainer.API.find(world, pos, from);
		if(container != null)
		{
			AspectStack stack;
			if(start.getSuction() > container.getSuction() && !(stack = container.extract(new AspectStack(aspect, 1))).isEmpty())
			{
				BlockEntity test = world.getBlockEntity(pos);
				if(test instanceof BlockEntityClientSerializable)
				{
					((BlockEntityClientSerializable)test).sync();
				}
				return stack;
			}
			else
			{
				BlockState state = world.getBlockState(pos);
				for(Direction direction : Direction.values())
				{
					if(direction != from && (state.getEntries().containsKey(PipeBlock.getPropertyFromDirection(direction)) && state.get(PipeBlock.getPropertyFromDirection(direction))) && !start.visited.contains(pos.offset(direction)))
					{
						stack = followPipe(world, pos.offset(direction), direction.getOpposite(), aspect, start);
						if(!stack.isEmpty())
						{
							return stack;
						}
					}
				}
			}
		}
		return AspectStack.EMPTY;
	}

	public static void tick(World world, BlockPos pos, BlockState state, JarEntity jar)
	{
		if(world.getTime() % 10 == 0)
		{
			Optional<AspectStack> optional = jar.aspects.values().stream().findFirst();
			AspectStack stack;
			if(optional.isPresent())
			{
				stack = followPipe(world, pos.up(), Direction.DOWN, optional.get().getAspect(), jar);
			}
			else
			{
				stack = followPipe(world, pos.up(), Direction.DOWN, Aspect.EMPTY, jar);
			}
			if(stack != null && !stack.isEmpty())
			{
				jar.addAspect(stack);
				jar.sync();
			}
			jar.visited.clear();
		}
	}

	@Override
	public AspectStack insert(AspectStack stack)
	{
		if(stack.isEmpty())
		{
			return stack;
		}
		Optional<AspectStack> optional = this.aspects.values().stream().findFirst();
		if(optional.isPresent())
		{
			if(optional.get().getAspect() == stack.getAspect())
			{
				optional.get().increment(stack.getCount());
				return AspectStack.EMPTY;
			}
		}
		else
		{
			this.addAspect(stack);
			return AspectStack.EMPTY;
		}
		return AspectStack.EMPTY;
	}

	@Override
	public AspectStack extract(AspectStack stack)
	{
		if(stack.isEmpty())
		{
			return stack;
		}
		Optional<AspectStack> optional = this.aspects.values().stream().findFirst();
		if(optional.isPresent())
		{
			if(optional.get().getAspect() == stack.getAspect())
			{
				int extract = Math.min(stack.getCount(), optional.get().getCount());
				optional.get().decrement(extract);
				return new AspectStack(stack.getAspect(), extract);
			}
		}
		return AspectStack.EMPTY;
	}

	@Override
	public int getSuction()
	{
		return 5;
	}
}
