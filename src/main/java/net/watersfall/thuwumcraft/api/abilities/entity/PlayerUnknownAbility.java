package net.watersfall.thuwumcraft.api.abilities.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSounds;
import net.watersfall.thuwumcraft.registry.tag.ThuwumcraftFluidTags;
import net.watersfall.thuwumcraft.registry.ThuwumcraftItems;
import net.watersfall.thuwumcraft.world.ThuwumcraftWorlds;
import net.watersfall.wet.api.abilities.Ability;
import net.watersfall.wet.api.abilities.AbilityClientSerializable;

/**
 * TODO: Entering the Unknown
 *
 * 		A thaumaturges first arrival in the unknown will be accidental
 * and temporary. After hopping into newly revealed unknown fluid, they
 * will wake up in this new strange place, with quickly deteriorating
 * vision and hearing, before waking back up at home.
 *
 * 		Future visits will not come from the same methods, or lead to
 * the same results. The same structure where unknown fluid is
 * typically found can be activated to form a stable way back and
 * forth.
 *
 * TODO: Design structure and activation method
 */
public interface PlayerUnknownAbility extends Ability<Entity>, AbilityClientSerializable<Entity>
{
	public static final Identifier ID = Thuwumcraft.getId("player_unknown_ability");

	int getTicksInUnknown();

	void setTicksInUnknown(int ticks);

	boolean isTemporary();

	void setTemporary(boolean temporary);

	boolean shouldPlaySound();

	void setShouldPlaySound(boolean sound);

	@Override
	default Identifier getId()
	{
		return ID;
	}

	@Override
	default NbtCompound toNbt(NbtCompound tag, Entity entity)
	{
		tag.putInt("ticks", getTicksInUnknown());
		tag.putBoolean("temporary", isTemporary());
		tag.putBoolean("sound", shouldPlaySound());
		return tag;
	}

	@Override
	default void fromNbt(NbtCompound tag, Entity entity)
	{
		this.setTicksInUnknown(tag.getInt("ticks"));
		this.setTemporary(tag.getBoolean("temporary"));
		this.setShouldPlaySound(tag.getBoolean("sound"));
	}

	@Override
	default void tick(Entity entity)
	{
		if(entity.getEntityWorld().getRegistryKey() == World.OVERWORLD)
		{
			if(entity.getEntityWorld().getFluidState(entity.getBlockPos()).getRegistryEntry().isIn(ThuwumcraftFluidTags.DIMENSIONAL_FLUID))
			{
				if(!entity.getEntityWorld().isClient && this.getTicksInUnknown() == 0)
				{
					ServerPlayerEntity player = (ServerPlayerEntity)entity;
					ServerWorld serverWorld = entity.world.getServer().getWorld(ThuwumcraftWorlds.THE_UNKNOWN);
					player.teleport(serverWorld, entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
					this.setShouldPlaySound(true);
					this.setTemporary(true);
					this.sync(entity);
				}
			}
			this.setTicksInUnknown(MathHelper.clamp(getTicksInUnknown() - 1, 0, Integer.MAX_VALUE));
		}
		else if(entity.getEntityWorld().getRegistryKey() == ThuwumcraftWorlds.THE_UNKNOWN)
		{
			if(!entity.getEntityWorld().isClient && this.isTemporary() && this.getTicksInUnknown() > 200)
			{
				ServerPlayerEntity player = (ServerPlayerEntity)entity;
				ServerWorld world = player.getServer().getWorld(World.OVERWORLD);
				BlockPos pos;
				if(player.getSpawnPointPosition() != null)
				{
					pos = player.getSpawnPointPosition();
				}
				else
				{
					pos = world.getSpawnPos();
				}
				player.teleport(world, pos.getX(), pos.getY(), pos.getZ(), player.getSpawnAngle(), 0);
				player.dropItem(ThuwumcraftItems.EYE_OF_THE_UNKNOWN);
				this.setTemporary(false);
				this.sync(entity);
			}
			this.setTicksInUnknown(this.getTicksInUnknown() + 1);
			if(shouldPlaySound() && entity.world.getRegistryKey() == ThuwumcraftWorlds.THE_UNKNOWN)
			{
				if(entity.world.isClient)
				{
					entity.world.playSoundFromEntity((PlayerEntity)entity, entity, ThuwumcraftSounds.RINGING_SOUND, SoundCategory.PLAYERS, 1F, 1F);
					entity.world.playSoundFromEntity((PlayerEntity)entity, entity, ThuwumcraftSounds.STATIC_SOUND, SoundCategory.PLAYERS, 1F, 1F);
				}
				this.setShouldPlaySound(false);
				this.sync(entity);
			}
		}
	}

	@Override
	default boolean copyable()
	{
		return true;
	}

	@Override
	default PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeBoolean(this.isTemporary());
		buf.writeBoolean(shouldPlaySound());
		return buf;
	}

	@Override
	default void fromPacket(PacketByteBuf buf)
	{
		this.setTemporary(buf.readBoolean());
		this.setShouldPlaySound(buf.readBoolean());
	}

	@Override
	default void sync(Entity entity)
	{
		if(entity instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity)entity;
			PacketByteBuf buf = this.toPacket(PacketByteBufs.create());
			ServerPlayNetworking.send(player, Thuwumcraft.getId("unknown_ability_packet"), buf);
		}
	}
}
