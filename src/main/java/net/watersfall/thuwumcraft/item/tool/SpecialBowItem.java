package net.watersfall.thuwumcraft.item.tool;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpecialBowItem extends BowItem
{
	public SpecialBowItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player)
	{
		if(stack.getNbt() != null && stack.getNbt().contains("RepairCost"))
		{
			stack.getNbt().putInt("RepairCost", 0);
		}
	}
}
