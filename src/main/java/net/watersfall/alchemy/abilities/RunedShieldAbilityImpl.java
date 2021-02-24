package net.watersfall.alchemy.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.AbilityClientSerializable;
import net.watersfall.alchemy.api.abilities.RunedShieldAbility;

public class RunedShieldAbilityImpl implements RunedShieldAbility, AbilityClientSerializable
{
	public RunedShieldAbilityImpl(CompoundTag tag)
	{
		this.fromNbt(tag);
	}


	public RunedShieldAbilityImpl(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	public RunedShieldAbilityImpl()
	{

	}

	@Override
	public Identifier getId()
	{
		return new Identifier("waters_alchemy_mod", "test");
	}

	@Override
	public CompoundTag toNbt(CompoundTag tag)
	{
		return tag;
	}

	@Override
	public void fromNbt(CompoundTag tag)
	{

	}

	@Override
	public void tick(Entity entity)
	{
		if(entity.getEntityWorld().isClient)
		{
			System.out.println("ticking uwu");
		}
	}

	@Override
	public int getShieldAmount()
	{
		return 5;
	}

	@Override
	public int getMaxAmount()
	{
		return 5;
	}

	@Override
	public int getRechargeRate()
	{
		return 5;
	}

	@Override
	public void setShieldAmount(int amount)
	{

	}

	@Override
	public void setMaxAmount(int amount)
	{

	}

	@Override
	public void setRechargeRate(int amount)
	{

	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeString(this.getId().toString());
		buf.writeInt(this.getShieldAmount());
		buf.writeInt(this.getMaxAmount());
		buf.writeInt(this.getRechargeRate());
		return buf;
	}

	@Override
	public void fromPacket(PacketByteBuf buf)
	{
		this.setShieldAmount(buf.readInt());
		this.setMaxAmount(buf.readInt());
		this.setRechargeRate(buf.readInt());
	}
}
