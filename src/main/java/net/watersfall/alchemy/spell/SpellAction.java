package net.watersfall.alchemy.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.entity.FireEntity;
import net.watersfall.alchemy.entity.IceProjectileEntity;
import net.watersfall.alchemy.entity.SandEntity;
import net.watersfall.alchemy.entity.WaterEntity;

import java.util.HashMap;

@FunctionalInterface
public interface SpellAction
{
	public static final Registry REGISTRY = new Registry();

	public TypedActionResult<ItemStack> use(ItemStack stack, World world, PlayerEntity player);

	public static SpellAction SNOW = REGISTRY.register(AlchemyMod.getId("snow"), (stack, world, player) -> {
		SnowballEntity snowball = new SnowballEntity(world, player);
		snowball.setVelocity(player.getRotationVector().multiply(1.5));
		world.playSoundFromEntity(player, player, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(snowball);
		return TypedActionResult.success(stack);
	});

	public static SpellAction ICE = REGISTRY.register(AlchemyMod.getId("ice"), (stack, world, player) -> {
		IceProjectileEntity entity = new IceProjectileEntity(world, player);
		entity.setVelocity(player.getRotationVector().multiply(1));
		world.playSoundFromEntity(player, player, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	});

	public static SpellAction WATER = REGISTRY.register(AlchemyMod.getId("water"), (stack, world, player) -> {
		WaterEntity entity = new WaterEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(2));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector().multiply(0.5));
		world.playSoundFromEntity(player, player, SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	});

	public static SpellAction FIRE = REGISTRY.register(AlchemyMod.getId("fire"), (stack, world, player) -> {
		FireEntity entity = new FireEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(2));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector().multiply(0.5));
		world.playSoundFromEntity(player, player, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	});

	public static SpellAction SAND = REGISTRY.register(AlchemyMod.getId("sand"), (stack, world, player) -> {
		SandEntity entity = new SandEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(1.5));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector());
		world.playSoundFromEntity(player, player, AlchemySounds.POCKET_SAND, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	});

	public static class Registry
	{
		private final HashMap<Identifier, SpellAction> map = new HashMap<>();
		private final HashMap<SpellAction, Identifier> map2 = new HashMap<>();

		private Registry(){}

		public final SpellAction register(Identifier id, SpellAction action)
		{
			map.put(id, action);
			map2.put(action, id);
			return action;
		}

		public final SpellAction get(Identifier id)
		{
			return map.get(id);
		}

		public final Identifier getId(SpellAction action)
		{
			return map2.get(action);
		}
	}
}
