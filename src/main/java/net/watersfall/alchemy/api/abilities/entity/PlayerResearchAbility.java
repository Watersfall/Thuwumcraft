package net.watersfall.alchemy.api.abilities.entity;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityClientSerializable;
import net.watersfall.alchemy.api.research.Research;

import java.util.List;

public interface PlayerResearchAbility extends Ability<Entity>, AbilityClientSerializable<Entity>
{
	public static final Identifier ID = new Identifier("waters_alchemy_mod", "player_research_ability");

	List<Research> getResearch();

	void addResearch(Identifier id);

	void addResearch(Research research);

	void grantCriterion(String id);

	boolean hasCriterion(String id);

	List<String> getCriteria();

	@Override
	default Identifier getId()
	{
		return ID;
	}

	@Override
	default CompoundTag toNbt(CompoundTag tag)
	{
		ListTag list = new ListTag();
		getResearch().forEach(research -> {
			list.add(StringTag.of(research.getId().toString()));
		});
		ListTag list2 = new ListTag();
		getCriteria().forEach(criterion -> {
			list2.add(StringTag.of(criterion));
		});
		tag.put("research_list", list);
		tag.put("criteria", list2);
		return tag;
	}

	@Override
	default void fromNbt(CompoundTag tag)
	{
		ListTag list = tag.getList("research_list", NbtType.STRING);
		list.forEach(research -> {
			addResearch(Identifier.tryParse(research.asString()));
		});
		list = tag.getList("criteria", NbtType.STRING);
		list.forEach(criterion -> {
			grantCriterion(criterion.asString());
		});
	}

	@Override
	default PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(getResearch().size());
		getResearch().forEach(research -> {
			buf.writeIdentifier(research.getId());
		});
		buf.writeInt(getCriteria().size());
		getCriteria().forEach(buf::writeString);
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
			this.grantCriterion(buf.readString());
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
