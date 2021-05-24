package net.watersfall.thuwumcraft.item.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.item.ThuwumcraftItems;

import java.util.function.Supplier;

public enum WandCoreMaterials implements WandCoreMaterial
{
	WOOD(Thuwumcraft.getId("wood"), 50, 0x54321A, () -> new ItemStack(ThuwumcraftItems.WOOD_CORE));

	private final double maxVis;
	private final Identifier id;
	private final int color;
	private final Supplier<ItemStack> stack;

	WandCoreMaterials(Identifier id, double maxVis, int color, Supplier<ItemStack> stack)
	{
		this.id = id;
		this.maxVis = maxVis;
		this.color = color;
		this.stack = stack;
		WandCoreMaterial.REGISTRY.register(id, this);
	}

	@Override
	public double getMaxVis()
	{
		return maxVis;
	}

	@Override
	public Identifier getId()
	{
		return id;
	}

	@Override
	public int getColor()
	{
		return color;
	}

	@Override
	public ItemStack getItemStack()
	{
		return stack.get().copy();
	}
}
