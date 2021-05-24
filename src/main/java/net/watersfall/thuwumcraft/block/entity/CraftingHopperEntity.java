package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.block.CraftingHopper;

import java.util.Optional;

public class CraftingHopperEntity extends BlockEntity
{
	public static final int[][] PATTERNS = new int[][]{
			{0,1,2,3,4,5,6,7,8},
			{0,1,2,3,5,6,7,8},
			{0,1,3,4,6,7},
			{0,1,2,3,4,5},
			{0,3,6},
			{0,1,2},
			{0,1,3,4},
			{0,3},
			{0,1},
			{0}
	};
	private final CraftingInventory internalInventory = new CraftingInventory(null, 3, 3);
	private int size = 9;

	public CraftingHopperEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.CRAFTING_HOPPER, pos, state);
	}

	private Inventory getExtractInventory(World world, BlockPos pos, Direction direction)
	{
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof InventoryProvider)
		{
			return ((InventoryProvider)state.getBlock()).getInventory(state, world, pos);
		}
		BlockEntity entity = world.getBlockEntity(pos);
		if(entity instanceof Inventory)
		{
			return (Inventory)entity;
		}
		return null;
	}

	private Inventory getInsertInventory(World world, BlockPos pos, Direction direction)
	{
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof InventoryProvider)
		{
			return ((InventoryProvider)state.getBlock()).getInventory(state, world, pos);
		}
		BlockEntity entity = world.getBlockEntity(pos);
		if(entity instanceof Inventory)
		{
			return (Inventory)entity;
		}
		return null;
	}

	private ItemStack attemptExtract(Inventory inventory, World world, BlockPos pos, Direction direction, int[] pattern)
	{
		for(int i = 0; i < inventory.size(); i++)
		{
			if(inventory.getStack(i).getCount() >= size)
			{
				if(!(inventory instanceof SidedInventory) || ((SidedInventory)inventory).canExtract(i, inventory.getStack(i), direction.getOpposite()))
				{
					for(int o = 0; o < pattern.length; o++)
					{
						internalInventory.stacks.set(pattern[o], inventory.getStack(i));
					}
					Optional<CraftingRecipe> optional = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, internalInventory, world);
					if(optional.isPresent())
					{
						return optional.get().craft(internalInventory);
					}
					else
					{
						internalInventory.clear();
					}
				}
			}
			else if(containsEnough(inventory, inventory.getStack(i), size, direction))
			{
				for(int o = 0; o < size; o++)
				{
					internalInventory.stacks.set(o, inventory.getStack(i).copy());
				}
				Optional<CraftingRecipe> optional = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, internalInventory, world);
				if(optional.isPresent())
				{
					return optional.get().craft(internalInventory);
				}
				else
				{
					internalInventory.clear();
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public boolean containsEnough(Inventory inventory, ItemStack stack, int needed, Direction direction)
	{
		int count = 0;
		for(int i = 0; i < inventory.size(); i++)
		{
			if(inventory instanceof SidedInventory)
			{
				if(!((SidedInventory)inventory).canExtract(i, inventory.getStack(i), direction))
				{
					continue;
				}
			}
			if(ItemStack.canCombine(stack, inventory.getStack(i)))
			{
				count += inventory.getStack(i).getCount();
				if(count >= needed)
				{
					return true;
				}
			}
		}
		return false;
	}

	private void attemptInsert(Inventory extracted, World world, BlockPos pos, Direction direction, ItemStack stack, int[] pattern)
	{
		Inventory inventory = getInsertInventory(world, pos, direction);
		if(inventory != null)
		{
			for(int i = 0; i < inventory.size() && !stack.isEmpty(); i++)
			{
				if(!(inventory instanceof SidedInventory) || ((SidedInventory)inventory).canInsert(i, stack, direction.getOpposite()))
				{
					if(inventory.getStack(i).isEmpty())
					{
						inventory.setStack(i, stack);
						consumeStacks(extracted, internalInventory.getStack(pattern[0]), size, direction.getOpposite());
						return;
					}
					else if(ItemStack.canCombine(inventory.getStack(i), stack) && inventory.getStack(i).getCount() < inventory.getStack(i).getMaxCount())
					{
						int amount = Math.min(stack.getCount(), inventory.getStack(i).getMaxCount() - inventory.getStack(i).getCount());
						stack.decrement(amount);
						inventory.getStack(i).increment(amount);
					}
				}
			}
			consumeStacks(extracted, internalInventory.getStack(pattern[0]), size, direction.getOpposite());
		}
	}

	private void consumeStacks(Inventory inventory, ItemStack stack, int size, Direction direction)
	{
		for(int i = 0; i < inventory.size() && size > 0; i++)
		{
			if(inventory instanceof SidedInventory)
			{
				if(!((SidedInventory)inventory).canExtract(i, inventory.getStack(i), direction))
				{
					continue;
				}
			}
			if(ItemStack.canCombine(stack, inventory.getStack(i)))
			{
				int remove = Math.min(size, inventory.getStack(i).getCount());
				inventory.getStack(i).decrement(remove);
				size -= remove;
			}
		}
		this.internalInventory.clear();
	}

	public static void tick(World world, BlockPos pos, BlockState state, CraftingHopperEntity entity)
	{
		if(world.getTime() % 20 == 0)
		{
			int[] pattern = PATTERNS[state.get(CraftingHopper.PATTERN)];
			entity.size = pattern.length;
			Inventory inventory = entity.getExtractInventory(world, pos.offset(Direction.UP), Direction.UP);
			if(inventory != null)
			{
				ItemStack stack = entity.attemptExtract(inventory, world, pos.up(), Direction.UP, pattern);
				if(!stack.isEmpty())
				{
					entity.attemptInsert(inventory, world, pos.offset(Direction.DOWN), Direction.DOWN, stack, pattern);
				}
			}
		}
	}
}
