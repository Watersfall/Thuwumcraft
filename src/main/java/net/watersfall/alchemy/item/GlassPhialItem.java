package net.watersfall.alchemy.item;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.abilities.item.PhialStorageAbility;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.common.AspectStorageAbility;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectInventory;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.client.item.GlassPhialTooltipData;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.Option;
import java.security.Provider;
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
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		if(test instanceof AspectInventory)
		{
			Direction direction = context.getSide();
			AspectInventory inventory = (AspectInventory) test;
			if(stack.getItem() == AlchemyItems.EMPTY_PHIAL_ITEM)
			{
				if(inventory.canExtract(direction))
				{
					if(!world.isClient)
					{
						AspectStack invStack = inventory.getAspects().values().stream().findFirst().get();
						ItemStack newStack = new ItemStack(Aspects.ASPECT_TO_PHIAL.get(invStack.getAspect()));
						AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(newStack);
						PhialStorageAbility ability = provider.getAbility(AspectStorageAbility.ID, PhialStorageAbility.class).get();
						int remove = Math.min(invStack.getCount(), MAX_COUNT);
						invStack = inventory.removeAspect(invStack.getAspect(), remove);
						ability.setAspect(invStack);
						stack.decrement(1);
						if(stack.isEmpty())
						{
							player.setStackInHand(hand, newStack);
						}
						else
						{
							if(!player.getInventory().insertStack(newStack))
							{
								player.dropItem(newStack, true);
							}
						}
						((BlockEntityClientSerializable) test).sync();
					}
					return ActionResult.success(world.isClient);
				}
			}
			else
			{
				Optional<AspectStorageAbility> optional = AbilityProvider.getProvider(stack).getAbility(AspectStorageAbility.ID, AspectStorageAbility.class);
				if(optional.isPresent())
				{
					AspectStorageAbility<ItemStack> ability = (AspectStorageAbility<ItemStack>) optional.get();
					if(inventory.canInsert(ability.getAspect(this.aspect), direction))
					{
						if(!world.isClient)
						{
							inventory.addAspect(ability.getAspects().stream().findFirst().get());
							stack.decrement(1);
							ItemStack newStack = new ItemStack(AlchemyItems.EMPTY_PHIAL_ITEM);
							if(stack.isEmpty())
							{
								player.setStackInHand(hand, newStack);
							}
							else
							{
								if(!player.getInventory().insertStack(newStack))
								{
									player.dropItem(newStack, true);
								}
							}
							((BlockEntityClientSerializable) test).sync();
						}
						return ActionResult.success(world.isClient);
					}
				}
			}
		}
		return ActionResult.PASS;
	}

	public Aspect getAspect()
	{
		return this.aspect;
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
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
			return Optional.of(new GlassPhialTooltipData((PhialStorageAbility) provider.getAbility(AspectStorageAbility.ID, AspectStorageAbility.class).get()));
		}
		return super.getTooltipData(stack);
	}
}
