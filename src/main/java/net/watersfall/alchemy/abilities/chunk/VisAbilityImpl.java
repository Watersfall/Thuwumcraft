package net.watersfall.alchemy.abilities.chunk;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.chunk.VisAbility;

public class VisAbilityImpl implements VisAbility
{
	private int vis;
	private int maxVis;

	public VisAbilityImpl()
	{
		this.vis = (int)(Math.random() * 51);
		this.maxVis = vis;
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
		tag.putInt("vis", this.vis);
		if(this.maxVis == 0)
		{
			this.maxVis = vis;
		}
		tag.putInt("max_vis", this.maxVis);
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, Chunk chunk)
	{
		this.vis = tag.getInt("vis");
		this.maxVis = tag.getInt("max_vis");
		if(this.maxVis == 0)
		{
			this.maxVis = vis;
		}
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(vis);
		buf.writeInt(maxVis);
		return buf;
	}

	@Override
	public void fromPacket(PacketByteBuf buf)
	{
		this.vis = buf.readInt();
		this.maxVis = buf.readInt();
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
				ServerPlayNetworking.send(entity, AlchemyMod.getId("vis_packet"), buf);
			}
		}
	}

	@Override
	public int getVis()
	{
		return this.vis;
	}

	@Override
	public int getMaxVis()
	{
		return this.maxVis;
	}

	@Override
	public void setVis(int vis)
	{
		this.vis = vis;
	}

	@Override
	public void setMaxVis(int maxVis)
	{
		this.maxVis = maxVis;
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
					int increase = Math.min(1, world.random.nextInt(maxVis / 20 + 1) + 1);
					vis = MathHelper.clamp(vis + increase, 0, maxVis);
					this.sync(chunk);
				}
			}
		}
	}
}
