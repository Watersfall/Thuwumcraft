package net.watersfall.alchemy.block.entity;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.watersfall.alchemy.api.fluid.ColoredWaterContainer;
import net.watersfall.alchemy.api.fluid.WaterContainer;
import net.watersfall.alchemy.block.BrewingCauldronBlock;
import net.watersfall.alchemy.client.util.RenderHelper;
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
import net.watersfall.alchemy.recipe.CauldronIngredient;

public class BrewingCauldronEntity extends AbstractCauldronEntity implements BrewingCauldronInventory
{
	private final DefaultedList<ItemStack> contents = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private final DefaultedList<ItemStack> input = DefaultedList.ofSize(1, ItemStack.EMPTY);

	protected byte ingredientCount;

	public BrewingCauldronEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.BREWING_CAULDRON_ENTITY, pos, state);
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
		this.ingredientCount = tag.getByte(INGREDIENT_COUNT);
		this.lastWaterLevel = (short)(waterLevel + ((float)this.ingredientCount * (1F / 32F)));
		Inventories.fromTag(tag, this.contents);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		super.toTag(tag);
		tag.putByte(INGREDIENT_COUNT, ingredientCount);
		Inventories.toTag(tag, this.contents);
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag)
	{
		super.fromClientTag(compoundTag);
		this.ingredientCount = compoundTag.getByte(INGREDIENT_COUNT);
		Inventories.fromTag(compoundTag, contents);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag)
	{
		super.toClientTag(compoundTag);
		compoundTag.putByte(INGREDIENT_COUNT, this.ingredientCount);
		Inventories.toTag(compoundTag, contents);
		return compoundTag;
	}

	public void clear()
	{
		this.ingredientCount = 0;
		this.contents.clear();
		super.clear();
	}

	@Override
	public void sync()
	{
		super.sync();
		this.needsColorUpdate = true;
	}

	@Override
	public int getColor()
	{
		if(this.needsColorUpdate())
		{
			int[] colors = new int[1 + this.getIngredientCount()];
			colors[0] = BiomeColors.getWaterColor(this.world, this.getPos());
			for(int i = 1; i <= this.getIngredientCount(); i++)
			{
				CauldronIngredient ingredient = BrewingCauldronBlock.getIngredient(this.getStack(i - 1).getItem(), this.world.getRecipeManager());
				colors[i] = ingredient == null ? -1 : ingredient.getColor();
			}
			this.setColor(RenderHelper.getColor(colors));
			this.setNeedsColorUpdate(false);
		}
		return this.color;
	}

	public byte getIngredientCount()
	{
		return ingredientCount;
	}

	public float getMaxDisplayWaterLevel()
	{
		return this.waterLevel + this.ingredientCount * 62.5F;
	}

	public void setIngredientCount(byte ingredientCount)
	{
		this.ingredientCount = ingredientCount;
		markDirty();
	}
}
