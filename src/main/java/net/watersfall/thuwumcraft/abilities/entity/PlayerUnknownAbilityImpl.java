package net.watersfall.thuwumcraft.abilities.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerUnknownAbility;

public class PlayerUnknownAbilityImpl implements PlayerUnknownAbility
{
	private int ticks;
	private boolean temporary;
	private boolean sound;

	public PlayerUnknownAbilityImpl()
	{
		this.ticks = 0;
		this.temporary = false;
		this.sound = false;
	}

	public PlayerUnknownAbilityImpl(NbtCompound tag, Entity entity)
	{
		this.fromNbt(tag, entity);
	}

	public PlayerUnknownAbilityImpl(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	@Override
	public int getTicksInUnknown()
	{
		return ticks;
	}

	@Override
	public void setTicksInUnknown(int ticks)
	{
		this.ticks = ticks;
	}

	@Override
	public boolean isTemporary()
	{
		return temporary;
	}

	@Override
	public void setTemporary(boolean temporary)
	{
		this.temporary = temporary;
	}

	@Override
	public boolean shouldPlaySound()
	{
		return sound;
	}

	@Override
	public void setShouldPlaySound(boolean sound)
	{
		this.sound = sound;
	}
}
