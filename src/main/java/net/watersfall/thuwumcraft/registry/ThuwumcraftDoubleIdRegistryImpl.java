package net.watersfall.thuwumcraft.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftDoubleIdRegistry;

import java.util.Set;

public class ThuwumcraftDoubleIdRegistryImpl<O, V> implements ThuwumcraftDoubleIdRegistry<O, V>
{
	private final BiMap<Identifier, V> idMap = HashBiMap.create();
	private final BiMap<O, V> otherMap = HashBiMap.create();

	@Override
	public V getByOtherId(O key)
	{
		return otherMap.get(key);
	}

	@Override
	public O getOtherId(V value)
	{
		return otherMap.inverse().get(value);
	}

	@Override
	public V register(Identifier id, O otherId, V value)
	{
		V object = idMap.put(id, value);
		otherMap.put(otherId, value);
		if(value != object && object != null)
		{
			Thuwumcraft.LOGGER.warn("Replacing registry value at: " + id.toString());
			Thuwumcraft.LOGGER.warn("Old Value: " + object);
			Thuwumcraft.LOGGER.warn("New Value: " + value);
		}
		return value;
	}

	@Override
	public V get(Identifier id)
	{
		return idMap.get(id);
	}

	@Override
	public Identifier getId(V value)
	{
		return idMap.inverse().get(value);
	}

	@Override
	public Set<V> values()
	{
		return idMap.values();
	}

	@Override
	public void clear()
	{
		idMap.clear();
		otherMap.clear();
	}
}
