package net.watersfall.alchemy.abilities.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.research.Research;

import java.util.ArrayList;
import java.util.List;

public class PlayerResearchAbilityImpl implements PlayerResearchAbility
{
	private List<Research> research;
	private List<Identifier> advancements;

	public PlayerResearchAbilityImpl()
	{
		this.advancements = new ArrayList<>();
		this.research = new ArrayList<>();
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
	public List<Research> getResearch()
	{
		return research;
	}

	@Override
	public List<Identifier> getAdvancements()
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
}
