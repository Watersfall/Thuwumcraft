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
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.alchemy.abilities.item.PhialStorageAbility;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.common.AspectStorageAbility;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectStack;
import net.watersfall.alchemy.api.aspect.Aspects;
import net.watersfall.alchemy.api.lookup.AspectContainer;
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
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		if(test instanceof AspectContainer)
		{
			if(world.isClient)
			{
				return ActionResult.SUCCESS;
			}
			AspectContainer container = (AspectContainer)test;
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
			Optional<PhialStorageAbility> ability = provider.getAbility(PhialStorageAbility.ID, PhialStorageAbility.class);
			ability.ifPresent(phial -> {
				AspectStack phialStack = phial.getAspects().get(0);
				AspectStack original = phialStack.copy();
				if((phialStack = container.insert(phialStack)).isEmpty())
				{
					stack.decrement(1);
					ItemStack newStack = new ItemStack(AlchemyItems.EMPTY_PHIAL_ITEM, 1);
					if(!player.getInventory().insertStack(newStack))
					{
						player.dropItem(newStack, true);
					}
				}
				else if(original.getCount() != phialStack.getCount())
				{
					stack.decrement(1);
					ItemStack newStack = Aspects.ASPECT_TO_PHIAL.get(phialStack.getAspect()).getDefaultStack();
					AbilityProvider<ItemStack> provider2 = AbilityProvider.getProvider(newStack);
					PhialStorageAbility ability2 = provider2.getAbility(PhialStorageAbility.ID, PhialStorageAbility.class).get();
					ability2.setAspect(phialStack);
					if(!player.getInventory().insertStack(newStack))
					{
						player.dropItem(newStack, true);
					}
				}
			});
			if(!ability.isPresent())
			{
				Aspects.ASPECTS.values().forEach((aspect -> {
					AspectStack check = new AspectStack(aspect, 64);
					if(!(check = container.extract(check)).isEmpty())
					{
						stack.decrement(1);
						ItemStack newStack = new ItemStack(Aspects.ASPECT_TO_PHIAL.get(check.getAspect()), 1);
						AbilityProvider<ItemStack> provider2 = AbilityProvider.getProvider(newStack);
						PhialStorageAbility ability2 = provider2.getAbility(PhialStorageAbility.ID, PhialStorageAbility.class).get();
						ability2.setAspect(check);
						if(!player.getInventory().insertStack(newStack))
						{
							player.dropItem(newStack, true);
						}
					}
				}));
			}
			if(test instanceof BlockEntityClientSerializable)
			{
				((BlockEntityClientSerializable)test).sync();
			}
			return ActionResult.CONSUME;
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
