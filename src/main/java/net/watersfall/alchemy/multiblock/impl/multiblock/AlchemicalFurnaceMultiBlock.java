package net.watersfall.alchemy.multiblock.impl.multiblock;

import net.watersfall.alchemy.block.AlchemyModBlocks;
import net.watersfall.alchemy.blockentity.ChildBlockEntity;
import net.watersfall.alchemy.multiblock.MultiBlock;
import net.watersfall.alchemy.multiblock.MultiBlockComponent;
import net.watersfall.alchemy.multiblock.MultiBlockRegistry;
import net.watersfall.alchemy.multiblock.MultiBlockType;
import net.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceComponent;
import net.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceInputComponent;
import net.watersfall.alchemy.multiblock.impl.component.AlchemicalFurnaceOutputComponent;
import net.watersfall.alchemy.multiblock.impl.type.AlchemicalFurnaceType;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class AlchemicalFurnaceMultiBlock implements MultiBlock<AlchemicalFurnaceComponent>
{
	private World world;
	private BlockPos pos;
	private AlchemicalFurnaceComponent[] components;
	private boolean isReady;
	private long ticks;

	public static final int BOTTOM_LEFT = 0;
	public static final int INPUT = 1;
	public static final int OUTPUT = 2;
	public static final int TOP_RIGHT = 3;

	public AlchemicalFurnaceMultiBlock(World world, BlockPos pos, AlchemicalFurnaceComponent[] components)
	{
		this.world =world;
		this.pos = pos;
		this.components = components;
		this.isReady = false;
		ticks = world.getTime();
	}

	@Override
	public AlchemicalFurnaceComponent[] getComponents()
	{
		return components;
	}

	@Override
	public boolean isValid()
	{
		return true;
	}

	@Override
	public void add()
	{
		MultiBlockRegistry.INSTANCE.add(this);
	}

	@Override
	public void remove()
	{
		for(int i = 0; i < this.components.length; i++)
		{
			if(this.world.getBlockState(this.components[i].getPos()).getBlock() == AlchemyModBlocks.CHILD_BLOCK)
			{
				this.world.setBlockState(this.components[i].getPos(), Blocks.FURNACE.getDefaultState());
			}
		}
		MultiBlockRegistry.INSTANCE.remove(this);
	}

	@Override
	public void onUse()
	{
		System.out.println("test");
	}

	@Override
	public void markInvalid()
	{
		this.remove();
	}

	@Override
	public void tick()
	{
		if(!isReady)
		{
			for(int i = 0; i < this.components.length; i++)
			{
				BlockEntity testEntity = world.getBlockEntity(this.components[i].getPos());
				if(testEntity instanceof ChildBlockEntity)
				{
					ChildBlockEntity entity = (ChildBlockEntity)testEntity;
					entity.setComponent(this.components[i]);
				}
			}
			this.isReady = true;
		}
		else
		{
			if(!world.isClient)
			{
				if(ticks % 20 == 0)
				{
					AlchemicalFurnaceInputComponent input = (AlchemicalFurnaceInputComponent) this.components[INPUT];
					AlchemicalFurnaceOutputComponent output = (AlchemicalFurnaceOutputComponent) this.components[OUTPUT];
					if(!input.getInventory().isEmpty())
					{
						for(int i = 0; i < input.getInventory().size(); i++)
						{
							if(!input.getInventory().getStack(i).isEmpty())
							{
								Optional<SmeltingRecipe> recipeOptional = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, input.getInventory(), world);
								if(recipeOptional.isPresent())
								{
									SmeltingRecipe recipe = recipeOptional.get();
									ItemStack stack = input.getInventory().removeStack(i, 1);
									if(!stack.isEmpty())
									{
										output.getInventory().setStack(i, recipe.getOutput().copy());
									}
								}
							}
						}
					}
				}
			}
		}
		ticks++;
	}

	@Override
	public MultiBlockType<? extends MultiBlock<? extends MultiBlockComponent>> getType()
	{
		return AlchemicalFurnaceType.INSTANCE;
	}

	@Override
	public BlockPos getPos()
	{
		return pos;
	}

	@Override
	public World getWorld()
	{
		return this.world;
	}
}
