package net.watersfall.alchemy.api.abilities.entity;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityClientSerializable;
import net.watersfall.alchemy.api.research.Research;

import java.util.Set;

public interface PlayerResearchAbility extends Ability<Entity>, AbilityClientSerializable<Entity>
{
	public static final Identifier ID = new Identifier("waters_alchemy_mod", "player_research_ability");

	Set<Research> getResearch();

	Set<Identifier> getAdvancements();

	void addResearch(Identifier id);

	void addResearch(Research research);

	void addAdvancement(Identifier id);

	boolean hasAdvancement(Identifier id);

	boolean hasResearch(Identifier id);

	boolean hasResearch(Research research);

	@Override
	default Identifier getId()
	{
		return ID;
	}

	@Override
	default NbtCompound toNbt(NbtCompound tag, Entity t)
	{
		NbtList list = new NbtList();
		getResearch().forEach(research -> {
			list.add(NbtString.of(research.getId().toString()));
		});
		tag.put("research_list", list);
		return tag;
	}

	@Override
	default void fromNbt(NbtCompound tag, Entity t)
	{
		this.getResearch().clear();
		NbtList list = tag.getList("research_list", NbtType.STRING);
		list.forEach(research -> {
			Research check = Research.REGISTRY.get(Identifier.tryParse(research.asString()));
			if(check != null)
			{
				addResearch(check);
			}
		});
	}

	@Override
	default PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(getResearch().size());
		getResearch().forEach(research -> buf.writeIdentifier(research.getId()));
		buf.writeInt(getAdvancements().size());
		getAdvancements().forEach(buf::writeIdentifier);
		return buf;
	}

	@Override
	default void fromPacket(PacketByteBuf buf)
	{
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			this.addResearch(buf.readIdentifier());
		}
		size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			this.addAdvancement(buf.readIdentifier());
		}
	}

	@Override
	default boolean copyable()
	{
		return true;
	}

	@Override
	default void sync(Entity entity)
	{

	}
}
