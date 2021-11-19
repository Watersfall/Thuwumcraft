package net.watersfall.thuwumcraft.entity.mind;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.network.MindMobSpawnS2CPacket;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

import java.util.UUID;

public class MindSpider extends SpiderEntity
{
	private UUID owner;

	public MindSpider(EntityType<? extends SpiderEntity> type, World world)
	{
		super(type, world);
		this.owner = UUID.randomUUID();
	}

	public MindSpider(World world, UUID owner)
	{
		this(ThuwumcraftEntities.MIND_SPIDER, world);
		this.owner = owner;
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		PacketByteBuf buf = PacketByteBufs.create();
		new MindMobSpawnS2CPacket(this).write(buf);
		return ServerPlayNetworking.createS2CPacket(Thuwumcraft.getId("mind_mob"), buf);
	}

	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}
}
