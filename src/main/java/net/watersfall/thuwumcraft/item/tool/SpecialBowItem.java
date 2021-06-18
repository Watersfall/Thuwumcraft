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
		if(stack.getTag() != null && stack.getTag().contains("RepairCost"))
		{
			stack.getTag().putInt("RepairCost", 0);
		}
	}
}
