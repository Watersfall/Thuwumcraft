package net.watersfall.alchemy.abilities.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.research.Research;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerResearchAbilityImpl implements PlayerResearchAbility
{
	private Set<Research> research;
	private Set<Identifier> advancements;

	public PlayerResearchAbilityImpl()
	{
		this.advancements = new HashSet<>();
		this.research = new HashSet<>();
	}

	public PlayerResearchAbilityImpl(Entity entity)
	{
		this();
		if(entity != null)
		{
			if(entity instanceof ServerPlayerEntity)
			{
				ServerPlayerEntity player = (ServerPlayerEntity)entity;
				player.getServerWorld().getServer().getAdvancementLoader().getAdvancements().forEach((advancement -> {
					if(player.getAdvancementTracker().getProgress(advancement).isDone())
					{
						this.advancements.add(advancement.getId());
					}
				}));
			}
		}
	}

	public PlayerResearchAbilityImpl(CompoundTag tag, Entity entity)
	{
		this(entity);
		this.fromNbt(tag, entity);
	}

	public PlayerResearchAbilityImpl(PacketByteBuf buf)
	{
		this();
		this.fromPacket(buf);
	}

	@Override
	public Set<Research> getResearch()
	{
		return research;
	}

	@Override
	public Set<Identifier> getAdvancements()
	{
		return this.advancements;
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

	@Override
	public void addAdvancement(Identifier id)
	{
		this.advancements.add(id);
	}

	@Override
	public boolean hasAdvancement(Identifier id)
	{
		return this.advancements.contains(id);
	}

	@Override
	public boolean hasResearch(Identifier id)
	{
		return hasResearch(Research.REGISTRY.get(id));
	}

	public boolean hasResearch(Research research)
	{
		return this.research.contains(research);
	}
}
