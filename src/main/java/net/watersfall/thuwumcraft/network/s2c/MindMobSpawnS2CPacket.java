package net.watersfall.thuwumcraft.network.s2c;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.watersfall.thuwumcraft.client.network.ThuwumcraftClientNetworking;
import net.watersfall.thuwumcraft.entity.mind.MindSpider;

import java.util.UUID;

public class MindMobSpawnS2CPacket extends MobSpawnS2CPacket
{
	private final UUID owner;

	public MindMobSpawnS2CPacket(MindSpider entity)
	{
		super(entity);
		this.owner = entity.getOwner();
	}

	public MindMobSpawnS2CPacket(PacketByteBuf buf)
	{
		super(buf);
		this.owner = buf.readUuid();
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		super.write(buf);
		buf.writeUuid(owner);
	}

	@Override
	public void apply(ClientPlayPacketListener clientPlayPacketListener)
	{
		ThuwumcraftClientNetworking.spawnMindMob(this, clientPlayPacketListener);
	}

	public UUID getOwner()
	{
		return this.owner;
	}
}
