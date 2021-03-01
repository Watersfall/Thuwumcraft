package net.watersfall.alchemy.api.abilities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Clearable;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public interface AbilityProvider<T> extends Clearable
{
	public static <T> AbilityProvider<T> getProvider(T t)
	{
		return (AbilityProvider<T>)t;
	}

	public static final Registry<Entity> ENTITY_REGISTRY = new Registry<>();
	public static final Registry<ItemStack> ITEM_REGISTRY = new Registry<>();

	void addAbility(Ability<T> ability);

	default void removeAbility(Ability<T> ability)
	{
		removeAbility(ability.getId());
	}

	void removeAbility(Identifier id);

	void copy(T to);

	<R> Optional<R> getAbility(Identifier id, Class<R> clazz);

	CompoundTag toNbt(CompoundTag tag);

	void fromNbt(CompoundTag tag);

	PacketByteBuf toPacket(PacketByteBuf buf);

	@Environment(EnvType.CLIENT)
	void fromPacket(PacketByteBuf buf);

	void sync(T t);

	class Registry<T>
	{
		private final HashMap<Identifier, Ability.FactoryTag<T>> registry;
		private final HashMap<Identifier, Ability.FactoryPacket<T>> packetRegistry;

		private Registry()
		{
			registry = new HashMap<>();
			packetRegistry = new HashMap<>();
		}

		public void register(Identifier id, Ability.FactoryTag<T> factory)
		{
			registry.put(id, factory);
		}

		public void registerPacket(Identifier id, Ability.FactoryPacket<T> packet)
		{
			packetRegistry.put(id, packet);
		}

		public Ability<T> create(Identifier id, CompoundTag tag)
		{
			return registry.get(id).create(tag);
		}

		public Ability<T> create(Identifier id, PacketByteBuf buf)
		{
			return packetRegistry.get(id).create(buf);
		}

		public Set<Identifier> getIds()
		{
			return registry.keySet();
		}
	}
}
