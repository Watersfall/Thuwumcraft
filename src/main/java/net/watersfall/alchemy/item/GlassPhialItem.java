package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.client.item.GlassPhialTooltipData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class GlassPhialItem extends Item
{
	private final Aspect aspect;
	private static final int MAX_COUNT = 64;

	public GlassPhialItem(Aspect aspect)
	{
		super(new FabricItemSettings().group(AlchemyItems.ALCHEMY_MOD_ITEM_GROUP));
		this.aspect = aspect;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockEntity test = world.getBlockEntity(pos);
		ItemStack stack = context.getStack();
		if(test instanceof AspectInventory)
		{
			Direction dir = context.getSide();
			AspectInventory inventory = (AspectInventory)test;
			if(this.aspect == Aspect.EMPTY)
			{
				if(inventory.canExtract(dir))
				{
					if(!world.isClient)
					{
						AspectStack aspectStack = inventory.getAspects().values().stream().findFirst().get();
						int remove = Math.min(MAX_COUNT, aspectStack.getCount());
						ItemStack newStack = new ItemStack(Aspects.ASPECT_TO_PHIAL.get(aspectStack.getAspect()));
						aspectStack = inventory.removeAspect(aspectStack.getAspect(), remove);
						if(this.setAspectCount(newStack, aspectStack.getCount()))
						{
							stack.decrement(1);
							if(stack.isEmpty())
							{
								context.getPlayer().setStackInHand(context.getHand(), newStack);
							}
							else
							{
								if(!context.getPlayer().getInventory().insertStack(newStack))
								{
									context.getPlayer().dropItem(newStack, true);
								}
							}
						}
						if(test instanceof BlockEntityClientSerializable)
						{
							((BlockEntityClientSerializable) test).sync();
						}
					}
					return ActionResult.success(world.isClient);
				}
			}
			else
			{
				AspectStack aspectStack = new AspectStack(this.getAspect(), this.getAspectCount(stack));
				if(inventory.canInsert(aspectStack, dir))
				{
					if(!world.isClient)
					{
						inventory.addAspect(aspectStack);
						stack.decrement(1);
						if(stack.isEmpty())
						{
							context.getPlayer().setStackInHand(context.getHand(), new ItemStack(AlchemyItems.EMPTY_PHIAL_ITEM));
						}
						else
						{
							if(!context.getPlayer().getInventory().insertStack(new ItemStack(AlchemyItems.EMPTY_PHIAL_ITEM)))
							{
								context.getPlayer().dropItem(new ItemStack(AlchemyItems.EMPTY_PHIAL_ITEM), true);
							}
						}
						if(test instanceof BlockEntityClientSerializable)
						{
							((BlockEntityClientSerializable) test).sync();
						}
					}
					return ActionResult.success(world.isClient);
				}
			}
			return ActionResult.success(world.isClient);
		}
		return ActionResult.PASS;
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}

	public int getAspectCount(ItemStack stack)
	{
		return stack.getOrCreateTag().getInt("waters_aspect_count");
	}

	public boolean setAspectCount(ItemStack stack, int count)
	{
		if(count > MAX_COUNT)
		{
			count = MAX_COUNT;
		}
		stack.getOrCreateTag().putInt("waters_aspect_count", count);
		return count > 0;
	}

	@Override
	public String getTranslationKey()
	{
		if(this.aspect == Aspect.EMPTY)
		{
			return "item.waters_alchemy_mod.phial.empty";
		}
		else
		{
			return "item.waters_alchemy_mod.phial";
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack)
	{
		if(this.aspect != Aspect.EMPTY)
		{
			return Optional.of(new GlassPhialTooltipData(new AspectStack(this.aspect, this.getAspectCount(stack))));
		}
		return super.getTooltipData(stack);
	}
}
