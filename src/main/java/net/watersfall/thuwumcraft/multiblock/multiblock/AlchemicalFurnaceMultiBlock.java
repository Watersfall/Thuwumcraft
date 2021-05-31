package net.watersfall.thuwumcraft.multiblock.multiblock;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.watersfall.thuwumcraft.api.multiblock.*;
import net.watersfall.thuwumcraft.block.AlchemicalFurnaceBlock;
import net.watersfall.thuwumcraft.block.ThuwumcraftBlocks;
import net.watersfall.thuwumcraft.block.entity.AlchemicalFurnaceEntity;
import net.watersfall.thuwumcraft.block.entity.ChildBlockEntity;
import net.watersfall.thuwumcraft.item.MagicalCoalItem;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceFuelComponent;
import net.watersfall.thuwumcraft.gui.AlchemicalFurnaceHandler;
import net.watersfall.thuwumcraft.api.multiblock.component.ItemComponent;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceComponent;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceInputComponent;
import net.watersfall.thuwumcraft.multiblock.component.AlchemicalFurnaceOutputComponent;
import net.watersfall.thuwumcraft.multiblock.type.AlchemicalFurnaceType;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.util.InventoryHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AlchemicalFurnaceMultiBlock implements GuiMultiBlock<AlchemicalFurnaceComponent>
{
	private World world;
	private BlockPos pos;
	private AlchemicalFurnaceComponent[] components;
	private boolean isReady;
	private int runningTicks;
	private int maxRunningTicks;
	private int fuelAmount;
	public static final int MAX_FUEL = 2400;
	private PropertyDelegate propertyDelegate = new PropertyDelegate()
	{
		@Override
		public int get(int index)
		{
			switch(index)
			{
				case 0:
					return runningTicks;
				case 1:
					return maxRunningTicks;
				case 2:
					return fuelAmount;
				default:
					return 0;
			}
		}

		@Override
		public void set(int index, int value)
		{
			switch(index)
			{
				case 0:
					runningTicks = value;
					break;
				case 1:
					maxRunningTicks = value;
					break;
				case 2:
					fuelAmount = value;
					break;
			}
		}

		@Override
		public int size()
		{
			return 3;
		}
	};

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
		runningTicks = 0;
	}

	public AlchemicalFurnaceMultiBlock(World world, BlockPos pos, AlchemicalFurnaceComponent[] components)
	{
		this.world =world;
		this.pos = pos;
		this.components = components;
		this.isReady = false;
		runningTicks = 0;
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
			MultiBlockRegistry.SERVER_TICKER.add(this);
		}
	}

	@Override
	public void remove()
	{
		ItemComponent input = (ItemComponent)this.components[INPUT];
		ItemComponent output = (ItemComponent)this.components[OUTPUT];
		ItemComponent fuel = (ItemComponent)this.components[BOTTOM_LEFT];
		ItemScatterer.spawn(world, this.components[INPUT].getPos(), input.getInventory());
		ItemScatterer.spawn(world, this.components[OUTPUT].getPos(), output.getInventory());
		ItemScatterer.spawn(world, this.components[BOTTOM_LEFT].getPos(), fuel.getInventory());
		Direction direction = this.world.getBlockState(pos).get(AlchemicalFurnaceBlock.DIRECTION);
		for(int i = 0; i < this.components.length; i++)
		{
			Block block = this.world.getBlockState(this.components[i].getPos()).getBlock();
			if(block == ThuwumcraftBlocks.CHILD_BLOCK || block == ThuwumcraftBlocks.ALCHEMICAL_FURNACE_BLOCK)
			{
				this.world.setBlockState(this.components[i].getPos(), Blocks.FURNACE.getDefaultState().with(FurnaceBlock.FACING, direction));
			}
		}
		MultiBlockRegistry.SERVER_TICKER.remove(this);
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
				AlchemicalFurnaceInputComponent input = (AlchemicalFurnaceInputComponent) this.components[INPUT];
				AlchemicalFurnaceOutputComponent output = (AlchemicalFurnaceOutputComponent) this.components[OUTPUT];
				AlchemicalFurnaceFuelComponent fuel = (AlchemicalFurnaceFuelComponent) this.components[BOTTOM_LEFT];
				if(this.fuelAmount < MAX_FUEL)
				{
					if(!fuel.getInventory().isEmpty())
					{
						ItemStack stack = fuel.getInventory().getStack(0);
						if(stack.getItem() instanceof MagicalCoalItem)
						{
							MagicalCoalItem item = (MagicalCoalItem) stack.getItem();
							if(item.getBurnTime() + this.fuelAmount <= MAX_FUEL)
							{
								stack.decrement(1);
								this.fuelAmount += item.getBurnTime();
							}
						}
					}
				}
				if(!input.getInventory().isEmpty())
				{
					if(true /*TODO: special recipe type*/)
					{
						if(fuelAmount > 0)
						{
							maxRunningTicks = 100;
							if(runningTicks >= 100)
							{
								for(int i = 0; i < input.getInventory().size(); i++)
								{
									if(!input.getInventory().getStack(i).isEmpty())
									{
										Optional<SmeltingRecipe> recipeOptional = world.getRecipeManager().getFirstMatch(
												RecipeType.SMELTING,
												new SimpleInventory(input.getInventory().getStack(i)),
												this.world
										);
										if(recipeOptional.isPresent())
										{
											SmeltingRecipe recipe = recipeOptional.get();
											ItemStack outputStack = recipe.getOutput().copy();
											boolean fit = InventoryHelper.fit(output.getInventory(), outputStack);
											if(fit)
											{
												input.getInventory().removeStack(i, 1);
											}
										}
									}
								}
								output.getInventory().markDirty();
								input.getInventory().markDirty();
								this.markDirty();
								runningTicks = 0;
							}
							else
							{
								fuelAmount--;
								runningTicks++;
							}
						}
					}
					else
					{
						runningTicks = 0;
					}
				}
				else
				{
					runningTicks = 0;
				}
			}
		}
	}

	@Override
	public void read(NbtCompound tag)
	{
		this.components = new AlchemicalFurnaceComponent[4];
		NbtList list = tag.getList("components", NbtType.COMPOUND);
		BlockPos input = new BlockPos(list.getCompound(INPUT).getInt("x"), list.getCompound(INPUT).getInt("y"), list.getCompound(INPUT).getInt("z"));
		BlockPos output = new BlockPos(list.getCompound(OUTPUT).getInt("x"), list.getCompound(OUTPUT).getInt("y"), list.getCompound(OUTPUT).getInt("z"));
		BlockPos left = new BlockPos(list.getCompound(BOTTOM_LEFT).getInt("x"), list.getCompound(BOTTOM_LEFT).getInt("y"), list.getCompound(BOTTOM_LEFT).getInt("z"));
		BlockPos right = new BlockPos(list.getCompound(TOP_RIGHT).getInt("x"), list.getCompound(TOP_RIGHT).getInt("y"), list.getCompound(TOP_RIGHT).getInt("z"));
		this.components[INPUT] = new AlchemicalFurnaceInputComponent(this.world, this, input);
		this.components[OUTPUT] = new AlchemicalFurnaceOutputComponent(this.world, this, output);
		this.components[BOTTOM_LEFT] = new AlchemicalFurnaceFuelComponent(this.world, this, left);
		this.components[TOP_RIGHT] = new AlchemicalFurnaceComponent(this.world, this, right);
		for(int i = 0; i < this.components.length; i++)
		{
			this.components[i].read(tag);
		}
		this.fuelAmount = tag.getInt("fuel_amount");
		this.runningTicks = tag.getInt("running_ticks");
		this.maxRunningTicks = tag.getInt("max_running_ticks");
		this.isReady = false;
	}

	@Override
	public NbtCompound write(NbtCompound tag)
	{
		NbtList list = new NbtList();
		for(int i = 0; i < this.components.length; i++)
		{
			NbtCompound pos = new NbtCompound();
			pos.putInt("x", this.components[i].getPos().getX());
			pos.putInt("y", this.components[i].getPos().getY());
			pos.putInt("z", this.components[i].getPos().getZ());
			list.add(pos);
			this.components[i].write(tag);
		}
		tag.put("components", list);
		tag.putInt("fuel_amount", fuelAmount);
		tag.putInt("running_ticks", runningTicks);
		tag.putInt("max_running_ticks", maxRunningTicks);
		return tag;
	}

	@Override
	public void markDirty()
	{
		BlockEntity test = this.world.getBlockEntity(pos);
		if(test != null)
		{
			test.markDirty();
		}
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
		return new TranslatableText("container.thuwumcraft.alchemical_furnace");
	}

	private Inventory getInventory(int side)
	{
		return ((ItemComponent)this.getComponents()[side]).getInventory();
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
	{
		return new AlchemicalFurnaceHandler(syncId, inv, getInventory(INPUT), getInventory(OUTPUT), getInventory(BOTTOM_LEFT), propertyDelegate);
	}
}
