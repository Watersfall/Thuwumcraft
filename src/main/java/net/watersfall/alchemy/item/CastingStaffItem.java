package net.watersfall.alchemy.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.watersfall.alchemy.entity.IceProjectileEntity;

public class CastingStaffItem extends Item
{
	private final SpellAction action;
	private final int castingTime;
	private final int cooldown;

	public CastingStaffItem(Settings settings, SpellAction action, int castingTime, int cooldown)
	{
		super(settings);
		this.action = action;
		this.castingTime = castingTime;
		this.cooldown = cooldown;
	}

	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.BOW;
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
		if(user instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)user;
			action.use(stack, world, player);
			player.getItemCooldownManager().set(this, cooldown);
		}
		return stack;
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		return this.castingTime;
	}

	@FunctionalInterface
	public static interface SpellAction
	{
		public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity player);

		public static SpellAction SNOW = ((stack, world, player) -> {
			SnowballEntity snowball = new SnowballEntity(world, player);
			snowball.setVelocity(player.getRotationVector().multiply(1.5));
			world.playSoundFromEntity(player, player, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
			world.spawnEntity(snowball);
			return TypedActionResult.success(stack);
		});

		public static SpellAction ICE = ((stack, world, player) -> {
			IceProjectileEntity entity = new IceProjectileEntity(world, player);
			entity.setVelocity(player.getRotationVector().multiply(1));
			world.playSoundFromEntity(player, player, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
			world.spawnEntity(entity);
			return TypedActionResult.success(stack);
		});
	}
}
