package net.watersfall.thuwumcraft.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.spell.Spell;

public class ContinuousCastingStaffItem extends CastingStaffItem
{
	public ContinuousCastingStaffItem(Settings settings, Spell<?> spell, int castingTime, int cooldown)
	{
		super(settings, spell, castingTime, cooldown);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		user.setCurrentHand(hand);
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user)
	{
		return stack;
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks)
	{
		super.usageTick(world, user, stack, remainingUseTicks);
		int currentTick = getMaxUseTime(stack) - remainingUseTicks;
		if(currentTick >= castingTime && currentTick % cooldown == 0)
		{
			if(user instanceof PlayerEntity)
			{
				this.spell.cast(stack, world, (PlayerEntity)user);
			}
		}
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		return 72000;
	}
}
