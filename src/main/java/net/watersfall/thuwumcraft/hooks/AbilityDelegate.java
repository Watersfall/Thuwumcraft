package net.watersfall.thuwumcraft.hooks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.abilities.Ability;
import net.watersfall.thuwumcraft.api.abilities.AbilityClientSerializable;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AbilityDelegate<T>
{
	private final Map<Identifier, Ability<T>> abilities = new HashMap<>();
	private final Map<Identifier, Ability<T>> tempAbilities = new HashMap<>();

	public void addAbility(Ability<T> ability)
	{
		abilities.put(ability.getId(), ability);
	}

	public void removeAbility(Identifier id)
	{
		abilities.remove(id);
	}

	public <R> Optional<R> getAbility(Identifier id, Class<R> clazz)
	{
		if(this.abilities.containsKey(id))
		{
			Ability<T> ability = this.abilities.get(id);
			if(clazz.isInstance(ability))
			{
				return Optional.of(clazz.cast(ability));
			}

		}
		return Optional.empty();
	}

	public void tick(T t)
	{
		this.abilities.values().forEach(ability -> ability.tick(t));
	}

	public void copy(T to, boolean alive)
	{
		AbilityProvider<T> provider = AbilityProvider.getProvider(to);
		this.abilities.values().forEach(ability -> {
			if(ability.copyable() || alive)
			{
				provider.addAbility(ability);
			}
		});
	}

	public NbtCompound toNbt(NbtCompound tag, T t)
	{
		this.abilities.values().forEach(value -> tag.put(value.getId().toString(), value.toNbt(new NbtCompound(), t)));
		return tag;
	}

	public void fromNbt(NbtCompound tag, T t, AbilityProvider<T> provider)
	{
		AbilityProvider.Registry<T> registry = provider.getRegistry(t);
		if(registry != null && tag != null)
		{
			NbtCompound abilitiesTag = tag.getCompound("thuwumcraft:abilities");
			abilitiesTag.getKeys().forEach(key -> {
				Ability<T> ability = registry.create(Identifier.tryParse(key), abilitiesTag.getCompound(key), t);
				this.abilities.put(ability.getId(), ability);
			});
		}
	}

	public PacketByteBuf toPacket(PacketByteBuf buf)
	{
		buf.writeInt(this.abilities.size());
		abilities.values().forEach((value) -> {
			if(value instanceof AbilityClientSerializable)
			{
				String id = value.getId().toString();
				buf.writeString(id);
				((AbilityClientSerializable) value).toPacket(buf);
			}
		});
		return buf;
	}

	public void fromPacket(PacketByteBuf buf, AbilityProvider<T> provider, T t)
	{
		tempAbilities.clear();
		AbilityProvider.Registry<T> registry = provider.getRegistry(t);
		if(registry != null)
		{
			int size = buf.readInt();
			for(int i = 0; i < size; i++)
			{
				Identifier id = Identifier.tryParse(buf.readString());
				this.tempAbilities.put(id, registry.create(id, buf));
			}
			MinecraftClient.getInstance().execute(() -> {
				this.abilities.clear();
				this.abilities.putAll(tempAbilities);
				this.tempAbilities.clear();
			});
		}
	}

	public void clear()
	{
		this.abilities.clear();
		this.tempAbilities.clear();
	}
}
