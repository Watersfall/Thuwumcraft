package net.watersfall.thuwumcraft.entity.mind;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.network.s2c.MindMobSpawnS2CPacket;
import net.watersfall.thuwumcraft.registry.ThuwumcraftEntities;

import java.util.UUID;

/**
 * TODO: Mind Spider Deep Lore:
 *
 * Mind spiders appear to players who have awakened their magical potential
 * by traveling to the unknown. The spiders believe the player to be an
 * ancient being originally from the unknown and attempt to attach to the
 * player the same as they did the ancient beings, by lunging at their heads
 * and attempting to merge. With low warp, this just resembles an attack, but
 * with a sufficiently warped, or rather, sufficiently similar to the ancient
 * beings player, the spider will be able to attempt to merge, but when this fails,
 * instead high damage will be dealt to the player, and then the spider will fade
 * away while fleeing.
 */
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
