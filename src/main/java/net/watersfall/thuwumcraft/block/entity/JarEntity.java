package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.AspectInventory;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.api.client.render.AspectRenderer;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.block.PipeBlock;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;

import java.util.*;


@EnvironmentInterface(value = EnvType.CLIENT, itf = AspectRenderer.class)
public class JarEntity extends SyncableBlockEntity implements AspectInventory, AspectContainer, AspectRenderer
{
	private static final int MAX_COUNT = 256;
	private final HashMap<Aspect, AspectStack> aspects;
	private ItemStack input;
	private final List<BlockPos> visited = new ArrayList<>();

	public JarEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.JAR, pos, state);
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
	public void writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		toInventoryTag(tag);
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

	private static AspectStack followPipe(World world, BlockPos pos, Direction from, Aspect aspect, JarEntity start, int suction)
	{
		start.visited.add(pos);
		if(world.isChunkLoaded(pos) && suction > 0)
		{
			AspectContainer container = AspectContainer.API.find(world, pos, from);
			AspectContainer insert = AspectContainer.API.find(world, pos.offset(from), from.getOpposite());
			if(container != null && insert != null)
			{
				AspectStack stack;
				if(suction > container.getSuction() && !(stack = container.extract(new AspectStack(aspect, 1), true)).isEmpty())
				{
					AspectStack newStack = insert.insert(stack.copy(), false);
					if(!newStack.equals(stack))
					{
						container.extract(new AspectStack(aspect, 1), false);
					}
					return AspectStack.EMPTY;
				}
				else
				{
					BlockState state = world.getBlockState(pos);
					for(Direction direction : Direction.values())
					{
						if(direction != from && (state.getEntries().containsKey(PipeBlock.getPropertyFromDirection(direction)) && state.get(PipeBlock.getPropertyFromDirection(direction))) && !start.visited.contains(pos.offset(direction)))
						{
							stack = followPipe(world, pos.offset(direction), direction.getOpposite(), aspect, start, suction - 1);
							if(!stack.isEmpty())
							{
								return stack;
							}
						}
					}
				}
			}
		}
		return AspectStack.EMPTY;
	}

	public static void tick(World world, BlockPos pos, BlockState state, JarEntity jar)
	{
		//if(world.getTime() % 10 == 0)
		{
			Optional<AspectStack> optional = jar.aspects.values().stream().findFirst();
			AspectStack stack;
			if(optional.isPresent() && optional.get().getCount() < MAX_COUNT)
			{
				stack = followPipe(world, pos.up(), Direction.DOWN, optional.get().getAspect(), jar, jar.getSuction());
			}
			else
			{
				stack = followPipe(world, pos.up(), Direction.DOWN, Aspect.EMPTY, jar, jar.getSuction());
			}
			jar.visited.clear();
		}
	}

	@Override
	public AspectStack insert(AspectStack stack, boolean simulate)
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
				int increment = Math.min(MAX_COUNT - optional.get().getCount(), stack.getCount());
				if(!simulate)
				{
					optional.get().increment(increment);
					this.sync();
				}
				stack.decrement(increment);
				return stack;
			}
		}
		else
		{
			if(!simulate)
			{
				this.addAspect(stack);
				this.sync();
			}
			return AspectStack.EMPTY;
		}
		return AspectStack.EMPTY;
	}

	@Override
	public AspectStack extract(AspectStack stack, boolean simulate)
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
				if(!simulate)
				{
					optional.get().decrement(extract);
					if(optional.get().isEmpty())
					{
						this.removeAspect(optional.get().getAspect());
					}
					this.sync();
				}
				return new AspectStack(stack.getAspect(), extract);
			}
		}
		return AspectStack.EMPTY;
	}

	@Override
	public int getSuction()
	{
		return 10;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void setup(MatrixStack matrices, BlockHitResult hit)
	{
		AspectRenderer.super.setup(matrices, hit);
		BlockPos up = pos.up();
		if(world.getBlockState(up).getMaterial().isSolid() && hit.getBlockPos().equals(this.getPos()))
		{
			Direction direction = hit.getSide();
			if(direction.getAxis() == Direction.Axis.Y)
			{
				matrices.translate(0, direction == Direction.UP ? -0.25 : -1.75, 0);
			}
			else
			{
				matrices.translate(direction.getOffsetX() / 1.75D, -1, direction.getOffsetZ() / 1.75D);
			}
		}
		else
		{
			matrices.translate(0, -0.25, 0);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public Collection<AspectStack> getStacksForRender()
	{
		return aspects.values();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public boolean shouldRenderInEvent()
	{
		return true;
	}
}
