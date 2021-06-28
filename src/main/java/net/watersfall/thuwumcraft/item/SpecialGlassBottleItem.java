package net.watersfall.thuwumcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.function.Supplier;

public class SpecialGlassBottleItem extends GlassBottleItem
{
	private final Supplier<ItemStack> filled;

	public SpecialGlassBottleItem(Settings settings, Supplier<ItemStack> filled)
	{
		super(settings);
		this.filled = filled;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack stack = user.getStackInHand(hand);
		BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
		if(hitResult.getType() == HitResult.Type.MISS)
		{
			return TypedActionResult.pass(stack);
		}
		else
		{
			if(hitResult.getType() == HitResult.Type.BLOCK)
			{
				BlockPos blockPos = hitResult.getBlockPos();
				if(!world.canPlayerModifyAt(user, blockPos))
				{
					return TypedActionResult.pass(stack);
				}
				if(world.getFluidState(blockPos).isIn(FluidTags.WATER))
				{
					world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
					return TypedActionResult.success(this.fill(stack, user, PotionUtil.setPotion(filled.get(), Potions.WATER)), world.isClient());
				}
			}

			return TypedActionResult.pass(stack);
		}
	}
}
