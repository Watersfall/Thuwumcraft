package net.watersfall.thuwumcraft.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.abilities.item.PhialStorageAbility;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.common.AspectStorageAbility;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.client.item.GlassPhialTooltipData;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class GlassPhialItem extends Item
{
	private static final int MAX_COUNT = 10;

	public GlassPhialItem()
	{
		super(new FabricItemSettings().group(ThuwumcraftItems.ALCHEMY_MOD_ITEM_GROUP));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockEntity test = world.getBlockEntity(pos);
		ItemStack stack = context.getStack();
		PlayerEntity player = context.getPlayer();
		if(test instanceof AspectContainer container)
		{
			if(world.isClient)
			{
				return ActionResult.SUCCESS;
			}
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
			Optional<PhialStorageAbility> ability = provider.getAbility(PhialStorageAbility.ID, PhialStorageAbility.class);
			ability.ifPresent(phial -> {
				AspectStack phialStack = phial.getAspects().get(0);
				AspectStack original = phialStack.copy();
				if((phialStack = container.insert(phialStack)).isEmpty())
				{
					stack.decrement(1);
					ItemStack newStack = new ItemStack(ThuwumcraftItems.EMPTY_PHIAL_ITEM, 1);
					if(!player.getInventory().insertStack(newStack))
					{
						player.dropItem(newStack, true);
					}
					if(ability.get().isEmpty())
					{
						provider.removeAbility(ability.get());
					}
				}
				else if(original.getCount() != phialStack.getCount())
				{
					stack.decrement(1);
					ItemStack newStack = this.getDefaultStack();
					AbilityProvider<ItemStack> provider2 = AbilityProvider.getProvider(newStack);
					PhialStorageAbility ability2 = provider2.getAbility(PhialStorageAbility.ID, PhialStorageAbility.class).get();
					ability2.setAspect(phialStack);
					if(!player.getInventory().insertStack(newStack))
					{
						player.dropItem(newStack, true);
					}
				}
			});
			if(ability.isEmpty())
			{
				Aspects.ASPECTS.values().forEach((aspect -> {
					AspectStack check = new AspectStack(aspect, MAX_COUNT);
					if(!(check = container.extract(check)).isEmpty())
					{
						stack.decrement(1);
						ItemStack newStack = this.getDefaultStack();
						AbilityProvider<ItemStack> provider2 = AbilityProvider.getProvider(newStack);
						PhialStorageAbility ability2 = new PhialStorageAbility(check);
						provider2.addAbility(ability2);
						if(!player.getInventory().insertStack(newStack))
						{
							player.dropItem(newStack, true);
						}
					}
				}));
			}
			return ActionResult.CONSUME;
		}
		return ActionResult.PASS;
	}

	@Override
	public Text getName(ItemStack stack)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<PhialStorageAbility> optional = provider.getAbility(PhialStorageAbility.ID, PhialStorageAbility.class);
		if(optional.isPresent())
		{
			AspectStack aspect = optional.get().getAspects().get(0);
			if(aspect.isEmpty())
			{
				return new TranslatableText("item.thuwumcraft.phial.empty");
			}
			else
			{
				return new TranslatableText("item.thuwumcraft.phial.filled", new TranslatableText(aspect.getAspect().getTranslationKey()));
			}
		}
		return super.getName(stack);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<AspectStorageAbility> optional = provider.getAbility(AspectStorageAbility.ID, AspectStorageAbility.class);
		if(optional.isPresent())
		{
			return Optional.of(new GlassPhialTooltipData(optional.get()));
		}
		return super.getTooltipData(stack);
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks)
	{
		super.appendStacks(group, stacks);
		if(this.isIn(group))
		{
			Aspects.ASPECTS.values().forEach(aspect -> {
				ItemStack stack = new ItemStack(this, 1);
				AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
				provider.addAbility(new PhialStorageAbility(aspect, MAX_COUNT));
				stacks.add(stack);
			});

		}
	}
}
