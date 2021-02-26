package net.watersfall.alchemy.abilities.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.AbilityClientSerializable;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.common.RunedShieldAbility;

public class RunedShieldAbilityEntity implements RunedShieldAbility<Entity>, AbilityClientSerializable<Entity>
{
	private int shieldAmount;
	private int maxShieldAmount;
	private int rechargeRate;
	public static final Identifier ID = AlchemyMod.getId("runed_shield_ability");

	public RunedShieldAbilityEntity(CompoundTag tag)
	{
		this.fromNbt(tag);
	}

	public RunedShieldAbilityEntity(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	public RunedShieldAbilityEntity(int shieldAmount, int maxShieldAmount, int rechargeRate)
	{
		this.shieldAmount = shieldAmount;
		this.maxShieldAmount = maxShieldAmount;
		this.rechargeRate = rechargeRate;
	}

	public RunedShieldAbilityEntity()
	{
		this(1, 1, 20);
	}

	@Override
	public Identifier getId()
	{
		return ID;
	}

	@Override
	public CompoundTag toNbt(CompoundTag tag)
	{
		tag.putInt("shield_amount", shieldAmount);
		tag.putInt("max_shield_amount", maxShieldAmount);
		tag.putInt("recharge_rate", rechargeRate);
		return tag;
	}

	@Override
	public void fromNbt(CompoundTag tag)
	{
		this.shieldAmount = tag.getInt("shield_amount");
		this.maxShieldAmount = tag.getInt("max_shield_amount") + 1;
		this.rechargeRate = tag.getInt("recharge_rate");
	}

	@Override
	public void tick(Entity entity)
	{
		if(!entity.getEntityWorld().isClient)
		{
			if(entity.getEntityWorld().getTime() % 100 == 0)
			{
				this.setShieldAmount(this.getShieldAmount() + 1);
				this.sync(entity);
			}
		}
	}

	@Override
	public int getShieldAmount()
	{
		return this.shieldAmount;
	}

	@Override
	public int getMaxAmount()
	{
		return this.maxShieldAmount;
	}

	@Override
	public int getRechargeRate()
	{
		return this.rechargeRate;
	}

	@Override
	public void setShieldAmount(int amount)
	{
		this.shieldAmount = amount;
	}

	@Override
	public void setMaxAmount(int amount)
	{
		this.maxShieldAmount = amount;
	}

	@Override
	public void setRechargeRate(int amount)
	{
		this.rechargeRate = amount;
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
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

	@Override
	public void sync(Entity entity)
	{
		PacketByteBuf buf = PacketByteBufs.create();
		AbilityProvider<Entity> provider = (AbilityProvider<Entity>)entity;
		provider.toPacket(buf);
		if(entity.getType() == EntityType.PLAYER)
		{
			ServerPlayNetworking.send((ServerPlayerEntity)entity, AlchemyMod.getId("abilities_packet"), buf);
		}
		for(ServerPlayerEntity player : PlayerLookup.tracking(entity))
		{
			ServerPlayNetworking.send(player, AlchemyMod.getId("abilities_packet"), buf);
		}
	}
}
