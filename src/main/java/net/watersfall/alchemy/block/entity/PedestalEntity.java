package net.watersfall.alchemy.block.entity;

import net.watersfall.alchemy.block.AlchemyModBlocks;
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
	private PedestalRecipe recipe;

	public PedestalEntity()
	{
		super(AlchemyModBlockEntities.PEDESTAL_ENTITY);
		stack = ItemStack.EMPTY;
		craftingFinished = false;
	}

	public void beginCraft(PedestalRecipe recipe)
	{
		this.setMain(true);
		this.recipe = recipe;
		List<PedestalEntity> entities = PedestalRecipe.getNearbyPedestals(this.getPos(), this.getWorld());
		List<PedestalEntity> validEntities = new ArrayList<>(recipe.getInputs().size());
		entities.removeIf(entity -> entity.isMain() || entity.isCrafting());
		for(int i = 0; i < recipe.getInputs().size(); i++)
		{
			for(int o = 0; o < entities.size(); o++)
			{
				if(recipe.getInputs().get(i).test(entities.get(o).getStack()))
				{
					validEntities.add(entities.get(o));
					entities.remove(o--);
					break;
				}
			}
		}
		validEntities.forEach((entity) -> {
			entity.setCrafting(true);
			this.world.getBlockTickScheduler().schedule(entity.getPos(), AlchemyModBlocks.PEDESTAL_BLOCK, 40 + this.world.getRandom().nextInt(20));
			entity.sync();
		});
		this.world.getBlockTickScheduler().schedule(this.getPos(), AlchemyModBlocks.PEDESTAL_BLOCK, 80);
		this.sync();
	}

	public void finishCraft()
	{
		if(this.recipe != null)
		{
			this.setMain(false);
			this.setStack(this.recipe.getOutput());
			world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT, 10000.0F, 1.5F);
			this.craftingFinished = true;
		}
	}

	public void helpCraft()
	{
		this.setCrafting(false);
		this.setStack(ItemStack.EMPTY);
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		this.setStack(ItemStack.fromTag(compoundTag.getCompound("pedestal_item")));
		this.main = compoundTag.getBoolean("main");
		this.crafting = compoundTag.getBoolean("crafting");
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag)
	{
		super.fromTag(state, tag);
		this.setStack(ItemStack.fromTag(tag.getCompound("pedestal_item")));
		this.main = tag.getBoolean("main");
		this.crafting = tag.getBoolean("crafting");
		this.craftingFinished = tag.getBoolean("crafting_finished");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		tag.put("pedestal_item", this.stack.toTag(new CompoundTag()));
		tag.putBoolean("main", main);
		tag.putBoolean("crafting", crafting);
		tag.putBoolean("crafting_finished", craftingFinished);
		return tag;
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		compoundTag.put("pedestal_item", this.stack.toTag(new CompoundTag()));
		compoundTag.putBoolean("main", main);
		compoundTag.putBoolean("crafting", crafting);
		return compoundTag;
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
}
