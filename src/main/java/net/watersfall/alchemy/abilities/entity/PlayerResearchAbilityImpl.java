package net.watersfall.alchemy.abilities.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.research.Research;

import java.util.ArrayList;
import java.util.List;

public class PlayerResearchAbilityImpl implements PlayerResearchAbility
{
	private List<Research> research;

	public PlayerResearchAbilityImpl()
	{
		this.research = new ArrayList<>();
	}

	public PlayerResearchAbilityImpl(CompoundTag tag)
	{
		this();
		this.fromNbt(tag);
	}

	public PlayerResearchAbilityImpl(PacketByteBuf buf)
	{
		this();
		this.fromPacket(buf);
	}

	@Override
	public List<Research> getResearch()
	{
		return research;
	}

	@Override
	public void addResearch(Identifier id)
	{
		this.research.add(Research.REGISTRY.get(id));
	}

	@Override
	public void addResearch(Research research)
	{
		this.research.add(research);
	}
}
