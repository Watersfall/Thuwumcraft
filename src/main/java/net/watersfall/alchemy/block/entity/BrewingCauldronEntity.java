package net.watersfall.alchemy.block.entity;

import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.inventory.BrewingCauldronInventory;
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
	private short lastWaterLevel = 0;
	private boolean needsColorUpdate = true;
	private int color = 0;

	public BrewingCauldronEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyModBlockEntities.BREWING_CAULDRON_ENTITY, pos, state);
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
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		this.waterLevel = tag.getShort(WATER_LEVEL);
		this.ingredientCount = tag.getByte(INGREDIENT_COUNT);
		this.lastWaterLevel = (short)(waterLevel + ((float)this.ingredientCount * (1F / 32F)));
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

	public short getWaterLevel()
	{
		return waterLevel;
	}

	public byte getIngredientCount()
	{
		return ingredientCount;
	}

	public int getColor()
	{
		return this.color;
	}

	public float getLastWaterLevel()
	{
		return this.lastWaterLevel;
	}

	public int getDisplayWaterLevel()
	{
		return this.lastWaterLevel;
	}

	public boolean needsColorUpdate()
	{
		return this.needsColorUpdate;
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

	public void setIngredientCount(byte ingredientCount)
	{
		this.ingredientCount = ingredientCount;
		markDirty();
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public void setLastWaterLevel(short lastWaterLevel)
	{
		this.lastWaterLevel = lastWaterLevel;
	}

	public void setNeedsColorUpdate(boolean needsColorUpdate)
	{
		this.needsColorUpdate = needsColorUpdate;
	}
}
