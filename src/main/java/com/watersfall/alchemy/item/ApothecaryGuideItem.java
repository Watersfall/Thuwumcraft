package com.watersfall.alchemy.item;

import com.watersfall.alchemy.inventory.ApothecaryGuideInventory;
import com.watersfall.alchemy.inventory.handler.ApothecaryGuideHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ApothecaryGuideItem extends Item
{
	public ApothecaryGuideItem()
	{
		super(new FabricItemSettings().maxCount(1).group(ItemGroup.BREWING));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack stack = user.getStackInHand(hand);
		user.openHandledScreen(createScreenHandlerFactory(stack));
		return TypedActionResult.success(stack);
	}

	private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack)
	{
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
		{
			return new ApothecaryGuideHandler(syncId, inventory, new ApothecaryGuideInventory(stack));
		}, stack.getName());
	}
}
