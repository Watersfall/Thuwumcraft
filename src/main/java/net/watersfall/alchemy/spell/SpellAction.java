package net.watersfall.alchemy.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.entity.FireEntity;
import net.watersfall.alchemy.entity.IceProjectileEntity;
import net.watersfall.alchemy.entity.SandEntity;
import net.watersfall.alchemy.entity.WaterEntity;

@FunctionalInterface
public interface SpellAction
{
	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity player);

	public static SpellAction SNOW = (stack, world, player) -> {
		SnowballEntity snowball = new SnowballEntity(world, player);
		snowball.setVelocity(player.getRotationVector().multiply(1.5));
		world.playSoundFromEntity(player, player, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(snowball);
		return TypedActionResult.success(stack);
	};

	public static SpellAction ICE = (stack, world, player) -> {
		IceProjectileEntity entity = new IceProjectileEntity(world, player);
		entity.setVelocity(player.getRotationVector().multiply(1));
		world.playSoundFromEntity(player, player, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	};

	public static SpellAction WATER = (stack, world, player) -> {
		WaterEntity entity = new WaterEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(2));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector().multiply(0.5));
		world.playSoundFromEntity(player, player, SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	};

	public static SpellAction FIRE = (stack, world, player) -> {
		FireEntity entity = new FireEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(2));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector().multiply(0.5));
		world.playSoundFromEntity(player, player, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	};

	public static SpellAction SAND = (stack, world, player) -> {
		SandEntity entity = new SandEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(1.5));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector());
		world.playSoundFromEntity(player, player, AlchemySounds.POCKET_SAND, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	};
}
