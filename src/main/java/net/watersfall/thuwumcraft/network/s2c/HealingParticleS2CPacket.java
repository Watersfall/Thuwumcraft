package net.watersfall.thuwumcraft.network.s2c;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.client.network.ThuwumcraftClientNetworking;

public class HealingParticleS2CPacket implements Packet<ClientPlayPacketListener>
{
	public static final Identifier ID = Thuwumcraft.getId("healing_particle");

	private final int id;
	private final int amount;

	public HealingParticleS2CPacket(PlayerEntity entity, int amount)
	{
		this.id = entity.getId();
		this.amount = amount;
	}

	public HealingParticleS2CPacket(PacketByteBuf buf)
	{
		this.id = buf.readInt();
		this.amount = buf.readInt();
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(id);
		buf.writeInt(amount);
	}

	@Override
	public void apply(ClientPlayPacketListener listener)
	{
		ThuwumcraftClientNetworking.spawnHealingParticles(this);
	}

	public int getId()
	{
		return id;
	}

	public int getAmount()
	{
		return amount;
	}
}
