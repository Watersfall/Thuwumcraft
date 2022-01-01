package net.watersfall.thuwumcraft.abilities.chunk;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.chunk.VisAbility;
import net.watersfall.thuwumcraft.api.tag.ThuwumcraftBiomeTags;
import net.watersfall.wet.api.abilities.AbilityProvider;

public class VisAbilityImpl implements VisAbility
{
	private double vis;
	private double maxVis;
	private double flux;

	public VisAbilityImpl(World world, ChunkPos pos, AbilityProvider<Chunk> provider)
	{
		if(!world.isClient)
		{
			Chunk chunk = (Chunk)provider;
			Biome biome = chunk.getBiomeForNoiseGen(pos.getStartX(), 0, pos.getStartZ());
			if(ThuwumcraftBiomeTags.MAGICAL_FORESTS.contains(biome))
			{
				this.vis = Math.random() * 200 + 100;
			}
			else
			{
				this.vis = Math.random() * 50 + 50;
			}
		}
		this.maxVis = vis;
		this.flux = 0;
	}

	public VisAbilityImpl(NbtCompound tag, Chunk chunk)
	{
		this.fromNbt(tag, chunk);
	}

	public VisAbilityImpl(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	@Override
	public Identifier getId()
	{
		return VisAbility.ID;
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, Chunk chunk)
	{
		tag.putDouble("vis", this.vis);
		if(this.maxVis == 0)
		{
			this.maxVis = vis;
		}
		tag.putDouble("max_vis", this.maxVis);
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, Chunk chunk)
	{
		this.vis = tag.getDouble("vis");
		this.maxVis = tag.getDouble("max_vis");
		if(this.maxVis == 0)
		{
			this.maxVis = vis;
		}
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeDouble(vis);
		buf.writeDouble(maxVis);
		return buf;
	}

	@Override
	public void fromPacket(PacketByteBuf buf)
	{
		this.vis = buf.readDouble();
		this.maxVis = buf.readDouble();
	}

	@Override
	public void sync(Chunk chunk)
	{
		if(chunk instanceof WorldChunk)
		{
			ServerWorld world = (ServerWorld)((WorldChunk)chunk).getWorld();
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(chunk.getPos().x);
			buf.writeInt(chunk.getPos().z);
			this.toPacket(buf);
			for(ServerPlayerEntity entity : PlayerLookup.tracking(world, chunk.getPos().getStartPos()))
			{
				ServerPlayNetworking.send(entity, Thuwumcraft.getId("vis_packet"), buf);
			}
		}
	}

	@Override
	public double getVis()
	{
		return this.vis;
	}

	@Override
	public double getMaxVis()
	{
		return this.maxVis;
	}

	@Override
	public double getFlux()
	{
		return flux;
	}

	@Override
	public void setVis(double vis)
	{
		this.vis = vis;
	}

	@Override
	public void setMaxVis(double maxVis)
	{
		this.maxVis = maxVis;
	}

	@Override
	public void setFlux(double flux)
	{
		this.flux = flux;
	}

	@Override
	public void tick(Chunk chunk)
	{
		if(chunk instanceof WorldChunk)
		{
			World world = ((WorldChunk)chunk).getWorld();
			if(!world.isClient && world.getTime() % 20 == 0)
			{
				if(vis < maxVis)
				{
					int increase = Math.min(1, world.random.nextInt((int)maxVis / 20 + 1) + 1);
					vis = MathHelper.clamp(vis + increase, 0, maxVis);
					this.sync(chunk);
				}
			}
		}
	}
}
