package net.watersfall.alchemy.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.watersfall.alchemy.world.CustomMobSpawnerLogic;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomMobSpawnerItem extends BlockItem
{
	public CustomMobSpawnerItem(Block block, Settings settings)
	{
		super(block, settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		CustomMobSpawnerLogic.toTooltip(stack, world, tooltip, context);
	}
}
