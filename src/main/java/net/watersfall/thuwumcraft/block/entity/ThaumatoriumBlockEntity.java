package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.block.PipeBlock;
import net.watersfall.thuwumcraft.block.ThaumatoriumBlock;
import net.watersfall.thuwumcraft.inventory.BasicInventory;
import net.watersfall.thuwumcraft.inventory.BetterAspectInventory;
import net.watersfall.thuwumcraft.recipe.CrucibleRecipe;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;

import java.util.*;

public class ThaumatoriumBlockEntity extends BlockEntity implements AspectContainer, BasicInventory, BlockEntityClientSerializable
{
	private final List<BlockPos> visited = new ArrayList<>();

	private final DefaultedList<ItemStack> inventory;
	private CrucibleRecipe currentRecipe = null;
	private final Map<Aspect, AspectStack> requiredAspects;
	private Identifier recipeId;

	public ThaumatoriumBlockEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.THAUMATORIUM, pos, state);
		this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
		this.requiredAspects = new HashMap<>();
	}

	@Override
	public AspectStack insert(AspectStack stack)
	{
		return stack;
	}

	@Override
	public AspectStack extract(AspectStack stack)
	{
		//No extract >:(
		return stack;
	}

	@Override
	public int getSuction()
	{
		return 50;
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return this.inventory;
	}

	public static void tick(World world, BlockPos pos, BlockState state, ThaumatoriumBlockEntity entity)
	{
		if(entity.currentRecipe != null
				&& entity.currentRecipe.catalyst.test(entity.inventory.get(0))
				&& (entity.inventory.get(1).isEmpty() || ItemStack.canCombine(entity.inventory.get(1), entity.currentRecipe.getOutput()))
				&& entity.inventory.get(1).getCount() < entity.inventory.get(1).getMaxCount()
		)
		{
			if(entity.requiredAspects.isEmpty())
			{
				entity.inventory.get(0).decrement(1);
				if(entity.inventory.get(1).isEmpty())
				{
					entity.inventory.set(1, entity.currentRecipe.getOutput());
				}
				else
				{
					entity.inventory.get(1).increment(1);
				}
				if(entity.inventory.get(0).isEmpty())
				{
					entity.setCurrentRecipe(null);
				}
				else
				{
					entity.setCurrentRecipe(entity.currentRecipe.getId());
				}
			}
			else if(world.getTime() % 10 == 0)
			{
				Aspect aspect = entity.requiredAspects.keySet().stream().findFirst().get();
				AspectStack stack = AspectStack.EMPTY;
				Direction facing = state.get(ThaumatoriumBlock.FACING);
				for(int i = 0; i < 2; i++)
				{
					for(Direction direction : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST})
					{
						if(direction != facing)
						{
							stack = followPipe(world, pos.offset(direction), direction.getOpposite(), aspect, entity);
							entity.visited.clear();
							if(!stack.isEmpty())
							{
								break;
							}
						}
					}
					if(!stack.isEmpty())
					{
						break;
					}
					pos = pos.up();
				}

				if(!stack.isEmpty())
				{
					stack.decrement(1);
					entity.requiredAspects.get(aspect).decrement(1);
					if(entity.requiredAspects.get(aspect).isEmpty())
					{
						entity.requiredAspects.remove(aspect);
					}
					entity.sync();
				}
			}
		}
	}

	private static AspectStack followPipe(World world, BlockPos pos, Direction from, Aspect aspect, ThaumatoriumBlockEntity start)
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

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		BetterAspectInventory.read(requiredAspects, nbt);
		Inventories.readNbt(nbt, inventory);
		recipeId = Identifier.tryParse(nbt.getString("recipe"));
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		BetterAspectInventory.write(requiredAspects, nbt);
		Inventories.writeNbt(nbt, inventory);
		if(currentRecipe != null)
		{
			nbt.putString("recipe", currentRecipe.getId().toString());
		}
		return nbt;
	}

	@Override
	public void setWorld(World world)
	{
		super.setWorld(world);
		if(recipeId != null)
		{
			Optional<? extends Recipe<?>> optional = world.getRecipeManager().get(recipeId);
			if(optional.isPresent() && optional.get() instanceof CrucibleRecipe recipe)
			{
				this.currentRecipe = recipe;
			}
		}
	}

	public void setCurrentRecipe(Identifier id)
	{
		var optional = this.world.getRecipeManager().get(id);
		if(optional.isPresent() && optional.get() instanceof CrucibleRecipe recipe)
		{
			this.currentRecipe = recipe;
			this.requiredAspects.clear();
			recipe.aspects.forEach(stack -> {
				this.requiredAspects.put(stack.getAspect(), stack.copy());
			});
			this.sync();
			this.setStack(2, recipe.getOutput());
		}
		else
		{
			this.currentRecipe = null;
			this.requiredAspects.clear();
			this.sync();
			this.setStack(2, ItemStack.EMPTY);
		}
	}

	@Override
	public void fromClientTag(NbtCompound tag)
	{
		requiredAspects.clear();
		BetterAspectInventory.read(requiredAspects, tag);
	}

	@Override
	public NbtCompound toClientTag(NbtCompound tag)
	{
		BetterAspectInventory.write(requiredAspects, tag);
		return tag;
	}

	public Map<Aspect, AspectStack> getRequiredAspects()
	{
		return requiredAspects;
	}
}
