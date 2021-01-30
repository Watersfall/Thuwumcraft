package com.watersfall.alchemy.blockentity;

import com.watersfall.alchemy.block.AlchemyModBlocks;
import com.watersfall.alchemy.block.PedestalBlock;
import com.watersfall.alchemy.inventory.PedestalInventory;
import com.watersfall.alchemy.recipe.PedestalRecipe;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PedestalEntity extends BlockEntity implements BlockEntityClientSerializable, PedestalInventory
{
	private ItemStack stack;
	private boolean main;
	private boolean crafting;
	private PedestalRecipe recipe;

	public PedestalEntity()
	{
		super(AlchemyModBlockEntities.PEDESTAL_ENTITY);
		stack = ItemStack.EMPTY;
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
			this.sync();
			world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT, 10000.0F, 1.5F);
		}
	}

	public void helpCraft()
	{
		this.setCrafting(false);
		this.setStack(ItemStack.EMPTY);
		this.sync();
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
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		tag.put("pedestal_item", this.stack.toTag(new CompoundTag()));
		tag.putBoolean("main", main);
		tag.putBoolean("crafting", crafting);
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
	}

	public void setMain(boolean main)
	{
		this.main = main;
	}

	public void setCrafting(boolean crafting)
	{
		this.crafting = crafting;
	}
}
