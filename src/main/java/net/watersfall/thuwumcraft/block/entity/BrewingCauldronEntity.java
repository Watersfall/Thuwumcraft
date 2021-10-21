package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.watersfall.thuwumcraft.client.util.RenderHelper;
import net.watersfall.thuwumcraft.inventory.BrewingCauldronInventory;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;
import net.watersfall.thuwumcraft.recipe.CauldronIngredient;

import java.util.Optional;

public class BrewingCauldronEntity extends AbstractCauldronEntity implements BrewingCauldronInventory
{
	private final DefaultedList<ItemStack> contents = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private final DefaultedList<ItemStack> input = DefaultedList.ofSize(1, ItemStack.EMPTY);

	protected byte ingredientCount;

	public BrewingCauldronEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.BREWING_CAULDRON, pos, state);
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
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		this.ingredientCount = tag.getByte(INGREDIENT_COUNT);
		this.lastWaterLevel = (short)(waterLevel + ((float)this.ingredientCount * (1F / 32F)));
		Inventories.readNbt(tag, this.contents);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		tag.putByte(INGREDIENT_COUNT, ingredientCount);
		Inventories.writeNbt(tag, this.contents);
		return tag;
	}

	@Override
	public void fromClientTag(NbtCompound compoundTag)
	{
		this.contents.clear();
		super.fromClientTag(compoundTag);
		this.ingredientCount = compoundTag.getByte(INGREDIENT_COUNT);
		Inventories.readNbt(compoundTag, contents);
	}

	@Override
	public NbtCompound toClientTag(NbtCompound compoundTag)
	{
		super.toClientTag(compoundTag);
		compoundTag.putByte(INGREDIENT_COUNT, this.ingredientCount);
		Inventories.writeNbt(compoundTag, contents);
		return compoundTag;
	}

	public void clear()
	{
		this.ingredientCount = 0;
		this.contents.clear();
		this.input.clear();
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
				Optional<CauldronIngredient> ingredient = this.world.getRecipeManager().getFirstMatch(ThuwumcraftRecipes.CAULDRON_INGREDIENTS, this.withInput(i - 1), world);
				colors[i] = ingredient.isPresent() ? ingredient.get().getColor() : -1;
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
