package net.watersfall.thuwumcraft.world;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CustomMobSpawnerLogic
{
	private int delay = 20;
	private int minDelay = 3600;
	private int maxDelay = 7200;
	private boolean requiresPlayer = true;
	private int playerRange = 32;
	private int minSpawnCount = 1;
	private int maxSpawnCount = 1;
	private int maxNearbyEntities = 2;
	private int spawnRange = 4;
	private MobSpawnerEntry entry = null;
	private final Random random = new Random();

	public void serverTick(World world, BlockPos pos)
	{
		if(entry != null)
		{
			if (!requiresPlayer || world.isPlayerInRange(pos.getX(), pos.getY(), pos.getZ(), this.playerRange))
			{
				if (this.delay == -1)
				{
					this.delay = this.delay + this.random.nextInt(this.maxDelay - this.minDelay);
				}
				if (this.delay > 0)
				{
					this.delay--;
				}
				else
				{
					int spawns = (maxSpawnCount > minSpawnCount) ? this.minSpawnCount + random.nextInt(maxSpawnCount - minSpawnCount) : minSpawnCount;
					for(int i = 0; i < spawns; ++i)
					{
						NbtCompound nbtCompound = entry.getEntityNbt();
						Optional<EntityType<?>> optional = EntityType.fromNbt(nbtCompound);
						if (!optional.isPresent())
						{
							this.entry = null;
							return;
						}
						double x = pos.getX() + (world.random.nextDouble() - world.random.nextDouble()) * (double)this.spawnRange + 0.5D;
						double y = pos.getY() + world.random.nextInt(3) - 1;
						double z = pos.getZ() + (world.random.nextDouble() - world.random.nextDouble()) * (double)this.spawnRange + 0.5D;
						if (world.isSpaceEmpty(optional.get().createSimpleBoundingBox(x, y, z)) && SpawnRestriction.canSpawn(optional.get(), (ServerWorldAccess) world, SpawnReason.SPAWNER, new BlockPos(x, y, z), world.getRandom()))
						{
							Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, (entityx) -> {
								entityx.refreshPositionAndAngles(x, y, z, entityx.getYaw(), entityx.getPitch());
								return entityx;
							});
							if (entity == null)
							{
								this.entry = null;
								return;
							}
							int k = world.getNonSpectatingEntities(entity.getClass(), (new Box(pos.getX(), pos.getY(), pos.getZ(), (double)(pos.getX() + 1), pos.getY() + 1, pos.getZ() + 1)).expand(this.spawnRange)).size();
							if (k >= this.maxNearbyEntities)
							{
								this.entry = null;
								return;
							}
							entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), world.random.nextFloat() * 360.0F, 0.0F);
							if (entity instanceof MobEntity)
							{
								MobEntity mobEntity = (MobEntity)entity;
								if (mobEntity.canSpawn(world, SpawnReason.SPAWNER) && mobEntity.canSpawn(world))
								{
									((MobEntity)entity).initialize((ServerWorldAccess) world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, null, null);
								}
							}

							world.syncWorldEvent(2004, pos, 0);
							if (entity instanceof MobEntity)
							{
								((MobEntity)entity).playSpawnEffects();
							}
							world.spawnEntity(entity);
							this.delay = -1;
						}
					}
				}
			}
		}
	}

	public void clientTick(World world, BlockPos pos)
	{
		if(entry != null)
		{
			double x = (double)pos.getX() + world.random.nextDouble();
			double y = (double)pos.getY() + world.random.nextDouble();
			double z = (double)pos.getZ() + world.random.nextDouble();
			world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}

	public void readNbt(World world, BlockPos pos, NbtCompound tag)
	{
		if(tag.contains("delay"))
			delay = tag.getInt("delay");
		if(tag.contains("min_delay"))
			minDelay = tag.getInt("min_delay");
		if(tag.contains("max_delay"))
			maxDelay = tag.getInt("max_delay");
		if(tag.contains("requires_player"))
			requiresPlayer = tag.getBoolean("requires_boolean");
		if(tag.contains("player_range"))
			playerRange = tag.getInt("player_range");
		if(tag.contains("min_spawn_count"))
			minSpawnCount = tag.getInt("min_spawn_count");
		if(tag.contains("max_spawn_count"))
			maxSpawnCount = tag.getInt("max_spawn_count");
		if(tag.contains("max_nearby_entities"))
			maxNearbyEntities = tag.getInt("max_nearby_entities");
		if(tag.contains("spawn_range"))
			spawnRange = tag.getInt("spawn_range");
		if(tag.contains("mob"))
			entry = new MobSpawnerEntry(tag.getCompound("mob"));
	}

	public NbtCompound writeNbt(World world, BlockPos pos, NbtCompound tag)
	{
		tag.putInt("delay", delay);
		tag.putInt("min_delay", minDelay);
		tag.putInt("max_delay", maxDelay);
		tag.putBoolean("requires_player", requiresPlayer);
		tag.putInt("player_range", playerRange);
		tag.putInt("min_spawn_count", minSpawnCount);
		tag.putInt("max_spawn_count", maxSpawnCount);
		tag.putInt("max_nearby_entites", maxNearbyEntities);
		tag.putInt("spawn_range", spawnRange);
		if(this.entry != null)
			tag.put("mob", entry.toNbt());
		return tag;
	}

	public static List<Text> toTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context)
	{
		NbtCompound tag = stack.getNbt();
		if(tag == null)
		{
			return tooltip;
		}
		else
		{
			if(tag.contains("mob"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".mob", tag.getCompound("mob").getCompound("Entity").getString("id")));
			else
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".mob", new TranslatableText(stack.getItem().getTranslationKey() + ".no_spawn")));
			if(tag.contains("delay"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".delay", tag.getInt("delay")));
			if(tag.contains("min_delay"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".min_delay", tag.getInt("min_delay")));
			if(tag.contains("max_delay"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".max_delay", tag.getInt("max_delay")));
			if(tag.contains("requires_player"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".requires_player", tag.getBoolean("requires_player")));
			if(tag.contains("player_range"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".player_range", tag.getInt("player_range")));
			if(tag.contains("min_spawn_count"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".min_spawn_count", tag.getInt("min_spawn_count")));
			if(tag.contains("max_spawn_count"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".max_spawn_count", tag.getInt("max_spawn_count")));
			if(tag.contains("max_nearby_entities"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".max_nearby_entities", tag.getInt("max_nearby_entities")));
			if(tag.contains("spawn_range"))
				tooltip.add(new TranslatableText(stack.getItem().getTranslationKey() + ".spawn_range", tag.getInt("spawn_range")));
		}
		return tooltip;
	}
}
