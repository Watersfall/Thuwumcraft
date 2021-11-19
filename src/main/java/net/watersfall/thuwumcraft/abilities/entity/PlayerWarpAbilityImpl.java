package net.watersfall.thuwumcraft.abilities.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.chunk.VisAbility;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerWarpAbility;
import net.watersfall.thuwumcraft.api.player.PlayerWarpEvent;
import net.watersfall.thuwumcraft.api.player.PlayerWarpEvents;
import net.watersfall.thuwumcraft.api.util.VisHelper;

public class PlayerWarpAbilityImpl implements PlayerWarpAbility
{
	private double tempWarp;
	private double normalWarp;
	private double permWarp;

	public PlayerWarpAbilityImpl()
	{
		tempWarp = 0;
		normalWarp = 0;
		permWarp = 0;
	}

	public PlayerWarpAbilityImpl(NbtCompound nbt, Entity entity)
	{
		this.fromNbt(nbt, entity);
	}

	public PlayerWarpAbilityImpl(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, Entity entity)
	{
		tag.putDouble("temp_warp", tempWarp);
		tag.putDouble("normal_warp", normalWarp);
		tag.putDouble("perm_warp", permWarp);
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, Entity entity)
	{
		tempWarp = tag.getDouble("temp_warp");
		normalWarp = tag.getDouble("normal_warp");
		permWarp = tag.getDouble("perm_warp");
		tempWarp = 100;
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeDouble(tempWarp);
		buf.writeDouble(normalWarp);
		buf.writeDouble(permWarp);
		return buf;
	}

	@Override
	public void tick(Entity entity)
	{
		if(!entity.world.isClient)
		{
			boolean shouldSync = false;
			if(entity.world.getTime() % 20 == 0)
			{
				VisAbility ability = VisHelper.getAbility(entity.world.getWorldChunk(entity.getBlockPos()));
				if(ability.getFlux() > ability.getMaxVis() * 0.75 && entity.world.random.nextFloat() <= 0.00005 * ability.getFlux())
				{
					setTemporaryWarp(getTemporaryWarp() + entity.world.random.nextFloat());
					shouldSync = true;
				}
			}
			if(entity.world.random.nextFloat() >= 0.999)
			{
				double remove = Math.min(entity.world.random.nextDouble(), tempWarp);
				setTemporaryWarp(getTemporaryWarp() - remove);
				shouldSync = true;
			}
			if(entity instanceof PlayerEntity player && entity.world.random.nextFloat() < (0.00005 * getTotalWarp()))
			{
				for(PlayerWarpEvent event : PlayerWarpEvents.getEvents().shuffle().stream().toList())
				{
					ActionResult result = event.run(player, this);
					if(result.isAccepted())
					{
						shouldSync = true;
						break;
					}
					else if(result == ActionResult.FAIL)
					{
						break;
					}
				}
			}
			if(shouldSync)
			{
				sync(entity);
			}
		}
	}

	@Override
	public void fromPacket(PacketByteBuf buf)
	{
		tempWarp = buf.readDouble();
		normalWarp = buf.readDouble();
		permWarp = buf.readDouble();
	}

	@Override
	public void sync(Entity entity)
	{
		if(entity instanceof ServerPlayerEntity player)
		{
			PacketByteBuf buf = PacketByteBufs.create();
			this.toPacket(buf);
			ServerPlayNetworking.send(player, Thuwumcraft.getId("warp"), buf);
		}
	}

	@Override
	public double getTemporaryWarp()
	{
		return tempWarp;
	}

	@Override
	public void setTemporaryWarp(double warp)
	{
		this.tempWarp = warp;
	}

	@Override
	public double getNormalWarp()
	{
		return normalWarp;
	}

	@Override
	public void setNormalWarp(double warp)
	{
		this.normalWarp = warp;
	}

	@Override
	public double getPermanentWarp()
	{
		return permWarp;
	}

	@Override
	public void setPermanentWarp(double warp)
	{
		this.permWarp = warp;
	}
}
