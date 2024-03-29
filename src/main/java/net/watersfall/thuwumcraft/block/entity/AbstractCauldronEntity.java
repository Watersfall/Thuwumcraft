package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.watersfall.thuwumcraft.api.fluid.ColoredWaterContainer;

public abstract class AbstractCauldronEntity extends SyncableBlockEntity implements ColoredWaterContainer, Clearable
{
	public static final String WATER_LEVEL = "water_level";
	public static final String INGREDIENT_COUNT = "ingredient_count";
	public static final String NEEDS_COLOR_UPDATE = "needs_color_update";

	protected short waterLevel;
	protected short lastWaterLevel = 0;
	protected boolean needsColorUpdate = true;
	protected int color = 0;

	public AbstractCauldronEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	@Override
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		this.waterLevel = tag.getShort(WATER_LEVEL);
	}

	@Override
	public void writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		tag.putShort(WATER_LEVEL, waterLevel);
	}

	@Override
	public void fromClientTag(NbtCompound compoundTag)
	{
		this.waterLevel = compoundTag.getShort(WATER_LEVEL);
		this.needsColorUpdate = compoundTag.getBoolean(NEEDS_COLOR_UPDATE);
	}

	@Override
	public NbtCompound toClientTag(NbtCompound compoundTag)
	{
		compoundTag.putShort(WATER_LEVEL, waterLevel);
		compoundTag.putBoolean(NEEDS_COLOR_UPDATE, needsColorUpdate);
		return compoundTag;
	}

	public float getAnimationProgress(float tickDelta)
	{
		return MathHelper.lerp(tickDelta, this.lastWaterLevel, this.getMaxDisplayWaterLevel());
	}

	public short getWaterLevel()
	{
		return waterLevel;
	}


	public int getColor()
	{
		if(this.needsColorUpdate)
		{
			this.color = this.world.getColor(this.pos, BiomeColors.WATER_COLOR);
		}
		return this.color;
	}

	public float getLastWaterLevel()
	{
		return this.lastWaterLevel;
	}

	public abstract float getMaxDisplayWaterLevel();

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

	public void setColor(int color)
	{
		this.color = color;
	}

	public void setLastWaterLevel(float lastWaterLevel)
	{
		this.lastWaterLevel = (short)lastWaterLevel;
	}

	public void setNeedsColorUpdate(boolean needsColorUpdate)
	{
		this.needsColorUpdate = needsColorUpdate;
	}

	@Override
	public void clear()
	{
		this.waterLevel = 0;
		this.needsColorUpdate = true;
		markDirty();
	}
}
