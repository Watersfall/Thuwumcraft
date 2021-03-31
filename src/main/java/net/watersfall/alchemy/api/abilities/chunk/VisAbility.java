package net.watersfall.alchemy.api.abilities.chunk;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityClientSerializable;

public class VisAbility implements Ability<Chunk>, AbilityClientSerializable<Chunk>
{
	public static final Identifier ID = new Identifier("waters_alchemy_mod", "vis_ability");

	public int vis;

	public VisAbility()
	{
		vis = (int)(Math.random() * 50) + 1;
	}

	public VisAbility(NbtCompound tag, Chunk chunk)
	{
		this.fromNbt(tag, chunk);
	}

	public VisAbility(PacketByteBuf buf)
	{
		this.fromPacket(buf);
	}

	@Override
	public Identifier getId()
	{
		return ID;
	}

	@Override
	public NbtCompound toNbt(NbtCompound tag, Chunk chunk)
	{
		tag.putInt("vis", vis);
		return tag;
	}

	@Override
	public void fromNbt(NbtCompound tag, Chunk chunk)
	{
		this.vis = tag.getInt("vis");
	}

	@Override
	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(this.vis);
		return buf;
	}

	@Override
	public void fromPacket(PacketByteBuf buf)
	{
		this.vis = buf.readInt();
	}

	@Override
	public void sync(Chunk chunk)
	{

	}
}
