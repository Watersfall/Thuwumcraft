package com.watersfall.poisonedweapons.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LadleItem extends Item
{
	public LadleItem()
	{
		super(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(1));

	}

	public LadleItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
		if(stack.getTag() != null && !stack.getTag().isEmpty())
		{
			if(stack.getTag().contains("effects"))
			{
				CompoundTag tag = stack.getTag().getCompound("effects");
				for(int i = 0; i < tag.getSize(); i++)
				{
					CompoundTag subtag = tag.getCompound("effect" + i);
					tooltip.add(new LiteralText("Effect: " + subtag.getString("id")));
					tooltip.add(new LiteralText("Duration: " + (subtag.getInt("duration") / 20) + " Seconds"));
				}
			}
		}

	}
}
