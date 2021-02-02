package net.watersfall.alchemy.multiblock.impl.multiblock;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.watersfall.alchemy.accessor.waters_AbstractCookingRecipeInputAccessor;
import net.watersfall.alchemy.block.AlchemyModBlocks;
import net.watersfall.alchemy.blockentity.AlchemicalFurnaceEntity;
import net.watersfall.alchemy.blockentity.ChildBlockEntity;
import net.watersfall.alchemy.inventory.handler.AlchemicalFurnaceHandler;
import net.watersfall.alchemy.multiblock.*;
import net.watersfall.alchemy.multiblock.component.ItemComponent;
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
import net.watersfall.alchemy.util.InventoryHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlchemicalFurnaceMultiBlock implements GuiMultiBlock<AlchemicalFurnaceComponent>
{
	private World world;
	private BlockPos pos;
	private AlchemicalFurnaceComponent[] components;
	private boolean isReady;
	private long ticks;

	public static final VoxelShape[] SHAPES = new VoxelShape[]{
			VoxelShapes.cuboid(0D, 0D, 0D, 0D, 0D, 0D),
			VoxelShapes.cuboid(-1D, -1D, 0D, 1D, 1D, 1D),
			VoxelShapes.cuboid(0D, 0D, 0D, 2D, 2D, 1D),
			VoxelShapes.cuboid(0D, -1D, 0D, 2D, 1D, 1D),
			VoxelShapes.cuboid(0D, 0D, 0D, 0D, 0D, 0D),
			VoxelShapes.cuboid(0D, -1D, 0D, 2D, 1D, 1D),
			VoxelShapes.cuboid(-1D, 0D, 0D, 1D, 2D, 1D),
			VoxelShapes.cuboid(-1D, -1D, 0D, 1D, 1D, 1D),
			VoxelShapes.cuboid(0D, 0D, 0D, 0D, 0D, 0D),
			VoxelShapes.cuboid(0D, -1D, 0D, 1D, 1D, 2D),
			VoxelShapes.cuboid(0D, 0D, -1D, 1D, 2D, 1D),
			VoxelShapes.cuboid(0D, -1D, -1D, 1D, 1D, 1D),
			VoxelShapes.cuboid(0D, 0D, 0D, 0D, 0D, 0D),
			VoxelShapes.cuboid(0D, -1D, -1D, 1D, 1D, 1D),
			VoxelShapes.cuboid(0D, 0D, 0D, 1D, 2D, 2D),
			VoxelShapes.cuboid(0D, -1D, 0D, 1D, 1D, 2D)
	};

	public static final int BOTTOM_LEFT = 0;
	public static final int INPUT = 1;
	public static final int OUTPUT = 2;
	public static final int TOP_RIGHT = 3;

	public AlchemicalFurnaceMultiBlock()
	{
		this.pos = null;
		this.world = null;
		this.components = null;
		this.isReady = false;
		this.ticks = 0;
	}

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
	public void add(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos.toImmutable();
		for(int i = 0; i < this.components.length; i++)
		{
			this.components[i].setWorld(world);
		}
		if(!world.isClient)
		{
			MultiBlockRegistry.SERVER.add(this);
		}

	}

	@Override
	public void remove()
	{
		ItemComponent input = (ItemComponent)this.components[INPUT];
		ItemComponent output = (ItemComponent)this.components[OUTPUT];
		ItemScatterer.spawn(world, this.components[INPUT].getPos(), input.getInventory());
		ItemScatterer.spawn(world, this.components[OUTPUT].getPos(), output.getInventory());
		for(int i = 0; i < this.components.length; i++)
		{
			Block block = this.world.getBlockState(this.components[i].getPos()).getBlock();
			if(block == AlchemyModBlocks.CHILD_BLOCK || block == AlchemyModBlocks.ALCHEMICAL_FURNACE_BLOCK)
			{
				this.world.setBlockState(this.components[i].getPos(), Blocks.FURNACE.getDefaultState());
			}
		}
		MultiBlockRegistry.SERVER.remove(this);
	}

	@Override
	public void onUse(World world, BlockPos pos, PlayerEntity player)
	{
		this.openScreen(world, pos, player);
	}

	@Override
	public void markInvalid()
	{
		this.remove();
	}

	@Override
	public void tick()
	{
		GuiMultiBlock.super.tick();
		if(!isReady)
		{
			if(this.world != null)
			{
				if(!world.isClient)
				{
					for(int i = 0; i < this.components.length; i++)
					{
						BlockEntity testEntity = world.getBlockEntity(this.components[i].getPos());
						if(testEntity instanceof ChildBlockEntity)
						{
							ChildBlockEntity entity = (ChildBlockEntity)testEntity;
							this.components[i].setWorld(world);
							entity.setComponent(this.components[i]);
							this.isReady = true;
							if(entity instanceof AlchemicalFurnaceEntity)
							{
								((AlchemicalFurnaceEntity) entity).sync();
							}
						}
					}
				}
			}
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
								List<SmeltingRecipe> recipeList = world.getRecipeManager().listAllOfType(RecipeType.SMELTING);
								for(int o = 0; o < recipeList.size(); o++)
								{
									waters_AbstractCookingRecipeInputAccessor accessor = (waters_AbstractCookingRecipeInputAccessor)recipeList.get(o);
									if(accessor.getInput().test(input.getInventory().getStack(i)))
									{
										ItemStack outputStack = recipeList.get(o).getOutput().copy();
										boolean fit = InventoryHelper.fit(output.getInventory(), outputStack);
										if(fit)
										{
											input.getInventory().removeStack(i, 1);
										}
										output.getInventory().markDirty();
										input.getInventory().markDirty();
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
	public void read(BlockState state, CompoundTag tag)
	{
		this.components = new AlchemicalFurnaceComponent[4];
		ListTag list = tag.getList("components", NbtType.COMPOUND);
		BlockPos input = new BlockPos(list.getCompound(INPUT).getInt("x"), list.getCompound(INPUT).getInt("y"), list.getCompound(INPUT).getInt("z"));
		BlockPos output = new BlockPos(list.getCompound(OUTPUT).getInt("x"), list.getCompound(OUTPUT).getInt("y"), list.getCompound(OUTPUT).getInt("z"));
		BlockPos left = new BlockPos(list.getCompound(BOTTOM_LEFT).getInt("x"), list.getCompound(BOTTOM_LEFT).getInt("y"), list.getCompound(BOTTOM_LEFT).getInt("z"));
		BlockPos right = new BlockPos(list.getCompound(TOP_RIGHT).getInt("x"), list.getCompound(TOP_RIGHT).getInt("y"), list.getCompound(TOP_RIGHT).getInt("z"));
		this.components[INPUT] = new AlchemicalFurnaceInputComponent(this.world, this, input);
		this.components[OUTPUT] = new AlchemicalFurnaceOutputComponent(this.world, this, output);
		this.components[BOTTOM_LEFT] = new AlchemicalFurnaceComponent(this.world, this, left);
		this.components[TOP_RIGHT] = new AlchemicalFurnaceComponent(this.world, this, right);
		for(int i = 0; i < this.components.length; i++)
		{
			this.components[i].read(state, tag);
		}
		this.isReady = false;
	}

	@Override
	public CompoundTag write(CompoundTag tag)
	{
		ListTag list = new ListTag();
		for(int i = 0; i < this.components.length; i++)
		{
			CompoundTag pos = new CompoundTag();
			pos.putInt("x", this.components[i].getPos().getX());
			pos.putInt("y", this.components[i].getPos().getY());
			pos.putInt("z", this.components[i].getPos().getZ());
			list.add(pos);
			this.components[i].write(tag);
		}
		tag.put("components", list);
		return tag;
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

	@Override
	public void openScreen(World world, BlockPos pos, PlayerEntity player)
	{
		player.openHandledScreen(this);
	}

	@Override
	public Text getDisplayName()
	{
		return new TranslatableText("waters_alchemy_mod.test");
	}

	private Inventory getInventory(int side)
	{
		return ((ItemComponent)this.getComponents()[side]).getInventory();
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
	{
		return new AlchemicalFurnaceHandler(syncId, inv, getInventory(INPUT), getInventory(OUTPUT));
	}
}
