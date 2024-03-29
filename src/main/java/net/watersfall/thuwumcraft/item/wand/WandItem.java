package net.watersfall.thuwumcraft.item.wand;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.api.abilities.chunk.VisAbility;
import net.watersfall.thuwumcraft.api.abilities.item.WandAbility;
import net.watersfall.thuwumcraft.api.spell.CastingType;
import net.watersfall.wet.api.abilities.AbilityProvider;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class WandItem extends Item
{
	public WandItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<WandAbility> optional = provider.getAbility(WandAbility.ID, WandAbility.class);
		if(optional.isPresent())
		{
			WandAbility ability = optional.get();
			if(ability.canCast())
			{
				if(ability.getSpell().getCastingType() == CastingType.SINGLE)
				{
					return ability.getSpell().getCastingTime();
				}
				else
				{
					return 72000;
				}
			}
		}
		return 0;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack stack = user.getStackInHand(hand);
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<WandAbility> ability = provider.getAbility(WandAbility.ID, WandAbility.class);
		if(ability.isPresent() && ability.get().canCast())
		{
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		return TypedActionResult.pass(stack);
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<WandAbility> optional = provider.getAbility(WandAbility.ID, WandAbility.class);
		if(optional.isPresent())
		{
			WandAbility ability = optional.get();
			int currentTick = getMaxUseTime(stack) - remainingUseTicks;
			if(ability.canCast())
			{
				if(ability.getSpell().getCastingType() == CastingType.CONTINUOUS)
				{
					int castingTime = ability.getSpell().getCastingTime();
					int cooldown = ability.getSpell().getCooldown();
					if(currentTick >= castingTime && currentTick % cooldown == 0)
					{
						if(user instanceof PlayerEntity)
						{
							ability.getSpell().cast(stack, world, (PlayerEntity)user);
							if(!world.isClient)
							{
								ability.setVis(ability.getVis() - ability.getSpell().getCost());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<WandAbility> optional = provider.getAbility(WandAbility.ID, WandAbility.class);
		if(optional.isPresent())
		{
			WandAbility ability = optional.get();
			if(ability.canCast())
			{
				if(ability.getSpell().getCastingType() == CastingType.SINGLE)
				{
					ability.getSpell().cast(stack, world, (PlayerEntity)user);
					if(!world.isClient)
					{
						ability.setVis(ability.getVis() - ability.getSpell().getCost());
					}
				}
			}
			((PlayerEntity)user).getItemCooldownManager().set(this, ability.getSpell().getCooldown());
		}
		return stack;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks)
	{
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		Optional<WandAbility> optional = provider.getAbility(WandAbility.ID, WandAbility.class);
		if(optional.isPresent())
		{
			WandAbility ability = optional.get();
			int currentTick = getMaxUseTime(stack) - remainingUseTicks;
			if(ability.canCast())
			{
				if(ability.getSpell().getCastingType() == CastingType.CONTINUOUS)
				{
					int castingTime = ability.getSpell().getCastingTime();
					if(currentTick >= castingTime)
					{
						if(user instanceof PlayerEntity)
						{
							((PlayerEntity)user).getItemCooldownManager().set(this, ability.getSpell().getCastingTime());
						}
					}
				}
			}
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		provider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(ability -> {
			if(ability.getWandCore() != null)
			{
				String vis = new DecimalFormat("#.##").format(ability.getVis());
				tooltip.add(new LiteralText("Vis: " + vis + "/" + ability.getWandCore().getMaxVis()));
			}
			if(ability.getWandCore() != null)
			{
				tooltip.add(new TranslatableText("item.thuwumcraft.wand.core").append(": ").append(ability.getWandCore().getName()));
			}
			else
			{
				tooltip.add(new TranslatableText("item.thuwumcraft.wand.core").append(": ").append(new TranslatableText("item.thuwumcraft.wand.core.none")));
			}
			if(ability.getWandCap() != null)
			{
				tooltip.add(new TranslatableText("item.thuwumcraft.wand.cap").append(": ").append(ability.getWandCap().getName()));
			}
			else
			{
				tooltip.add(new TranslatableText("item.thuwumcraft.wand.cap").append(": ").append(new TranslatableText("item.thuwumcraft.wand.cap.none")));
			}
			if(ability.getSpell() == null)
			{
				tooltip.add(new TranslatableText("item.thuwumcraft.wand.spell").append(": ").append(new TranslatableText("item.thuwumcraft.wand.spell.none")));
			}
			else
			{
				tooltip.add(new TranslatableText("item.thuwumcraft.wand.spell").append(": ").append(ability.getSpell().getName()));
			}
		});
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		super.inventoryTick(stack, world, entity, slot, selected);
		if(!world.isClient && world.getTime() % 100 == 0)
		{
			AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
			provider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(wand -> {
				if(wand.getWandCore() != null && wand.canCharge(CapRechargeType.ENVIRONMENTAL))
				{
					Chunk chunk = entity.getEntityWorld().getChunk(entity.getBlockPos());
					AbilityProvider<Chunk> chunkProvider = AbilityProvider.getProvider(chunk);
					chunkProvider.getAbility(VisAbility.ID, VisAbility.class).ifPresent(chunkVis -> {
						if(chunkVis.getVis() > 0)
						{
							chunkVis.setVis(chunkVis.getVis() - 1);
							chunkVis.sync(chunk);
							wand.setVis(wand.getVis() + 1);
						}
					});
				}
			});
		}
	}
}
