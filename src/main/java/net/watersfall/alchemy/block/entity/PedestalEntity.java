package net.watersfall.alchemy.block.entity;

import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.alchemy.block.AlchemyBlocks;
import net.watersfall.alchemy.inventory.PedestalInventory;
import net.watersfall.alchemy.recipe.PedestalRecipe;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class PedestalEntity extends BlockEntity implements BlockEntityClientSerializable, PedestalInventory
{
	private ItemStack stack;
	private boolean main;
	private boolean crafting;
	private boolean craftingFinished;
	private PedestalRecipe.StageTracker recipe;

	public PedestalEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.PEDESTAL_ENTITY, pos, state);
		stack = ItemStack.EMPTY;
		craftingFinished = false;
	}

	public void beginCraft(PedestalRecipe recipe)
	{
		this.setMain(true);
		this.recipe = new PedestalRecipe.StageTracker(this, world, pos, recipe);
		this.sync();
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		this.setStack(ItemStack.fromNbt(compoundTag.getCompound("pedestal_item")));
		this.main = compoundTag.getBoolean("main");
		this.crafting = compoundTag.getBoolean("crafting");
	}

	@Override
	public void readNbt(CompoundTag tag)
	{
		super.readNbt(tag);
		this.setStack(ItemStack.fromNbt(tag.getCompound("pedestal_item")));
		this.main = tag.getBoolean("main");
		this.crafting = tag.getBoolean("crafting");
		this.craftingFinished = tag.getBoolean("crafting_finished");
		if(tag.contains("recipe"))
		{
			CompoundTag recipeTag = tag.getCompound("recipe");
			this.recipe = new PedestalRecipe.StageTracker(this, this.world, this.pos, recipeTag);
		}
	}

	@Override
	public CompoundTag writeNbt(CompoundTag tag)
	{
		super.writeNbt(tag);
		tag.put("pedestal_item", this.stack.writeNbt(new CompoundTag()));
		tag.putBoolean("main", main);
		tag.putBoolean("crafting", crafting);
		tag.putBoolean("crafting_finished", craftingFinished);
		if(this.recipe != null)
		{
			CompoundTag recipeTag = new CompoundTag();
			this.recipe.toTag(recipeTag);
			tag.put("recipe", recipeTag);
		}
		return tag;
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		compoundTag.put("pedestal_item", this.stack.writeNbt(new CompoundTag()));
		compoundTag.putBoolean("main", main);
		compoundTag.putBoolean("crafting", crafting);
		return compoundTag;
	}

	@Override
	public void setWorld(World world)
	{
		super.setWorld(world);
		if(this.recipe != null)
		{
			this.recipe.setWorld(world);
			this.recipe.fromTag();
		}
	}

	@Override
	public void sync()
	{
		this.markDirty();
		BlockEntityClientSerializable.super.sync();
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public boolean isMain()
	{
		return main;
	}

	public boolean isCrafting()
	{
		return crafting;
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
		if(this.world != null && !this.world.isClient)
		{
			this.sync();
		}
	}

	public void setMain(boolean main)
	{
		this.main = main;
	}

	public void setCrafting(boolean crafting)
	{
		this.crafting = crafting;
	}

	public void setCraftingFinished(boolean finished)
	{
		this.craftingFinished = finished;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir)
	{
		if(slot != 0)
		{
			return false;
		}
		else if(dir == Direction.DOWN && this.craftingFinished)
		{
			this.craftingFinished = false;
			return true;
		}
		return false;
	}

	public static <T> void tick(World world, BlockPos pos, BlockState state, T blockEntity)
	{
		PedestalEntity entity = (PedestalEntity)blockEntity;
		if(entity.recipe != null)
		{
			if(entity.recipe.getStage() != PedestalRecipe.StageTracker.Stage.END)
			{
				entity.recipe.tick();
			}
			else
			{
				entity.recipe = null;
			}
		}
	}
}
