package com.watersfall.alchemy.blockentity;

import com.watersfall.alchemy.inventory.BrewingCauldronInventory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;

public class BrewingCauldronEntity extends BlockEntity implements BrewingCauldronInventory, BlockEntityClientSerializable
{
	public static final String WATER_LEVEL = "water_level";
	public static final String INGREDIENT_COUNT = "ingredient_count";
	public static final String NEEDS_COLOR_UPDATE = "needs_color_update";

	private final DefaultedList<ItemStack> contents = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private final DefaultedList<ItemStack> input = DefaultedList.ofSize(1, ItemStack.EMPTY);
	private short waterLevel;
	private byte ingredientCount;
	public float lastWaterLevel = 0;
	public boolean needsColorUpdate = true;
	public int color = 0;

	public BrewingCauldronEntity()
	{
		super(AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY);
	}

	@Override
	public DefaultedList<ItemStack> getContents()
	{
		return contents;
	}

	@Override
	public DefaultedList<ItemStack> getInput()
	{
		return input;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag)
	{
		super.fromTag(state, tag);
		this.waterLevel = tag.getShort(WATER_LEVEL);
		this.ingredientCount = tag.getByte(INGREDIENT_COUNT);
		this.lastWaterLevel = waterLevel + ((float) this.ingredientCount * (1F / 32F));
		Inventories.fromTag(tag, this.contents);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		tag.putShort(WATER_LEVEL, waterLevel);
		tag.putByte(INGREDIENT_COUNT, ingredientCount);
		Inventories.toTag(tag, this.contents);
		return tag;
	}

	public short getWaterLevel()
	{
		return waterLevel;
	}

	public void setWaterLevel(short waterLevel)
	{
		if(waterLevel < 5)
		{
			waterLevel = 0;
		}
		this.waterLevel = waterLevel;
		markDirty();
	}

	public byte getIngredientCount()
	{
		return ingredientCount;
	}

	public void setIngredientCount(byte ingredientCount)
	{
		this.ingredientCount = ingredientCount;
		markDirty();
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		this.waterLevel = compoundTag.getShort(WATER_LEVEL);
		this.ingredientCount = compoundTag.getByte(INGREDIENT_COUNT);
		this.needsColorUpdate = compoundTag.getBoolean(NEEDS_COLOR_UPDATE);
		Inventories.fromTag(compoundTag, contents);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		compoundTag.putShort(WATER_LEVEL, waterLevel);
		compoundTag.putByte(INGREDIENT_COUNT, ingredientCount);
		compoundTag.putBoolean(NEEDS_COLOR_UPDATE, needsColorUpdate);
		Inventories.toTag(compoundTag, contents);
		return compoundTag;
	}

	public void clear()
	{
		this.ingredientCount = 0;
		this.waterLevel = 0;
		this.needsColorUpdate = true;
		this.contents.clear();
		markDirty();
	}

	@Override
	public void sync()
	{
		BlockEntityClientSerializable.super.sync();
		this.needsColorUpdate = true;
	}

	@Environment(EnvType.CLIENT)
	public float getAnimationProgress(float tickDelta)
	{
		return MathHelper.lerp(tickDelta, this.lastWaterLevel, (float) this.waterLevel + ((float) this.ingredientCount * 62.5F));
	}
}
