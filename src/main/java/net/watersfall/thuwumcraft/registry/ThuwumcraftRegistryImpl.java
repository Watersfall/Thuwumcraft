package net.watersfall.thuwumcraft.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;

import java.util.Set;

public class ThuwumcraftRegistryImpl<K, V> implements ThuwumcraftRegistry<K, V>
{
	private final BiMap<K, V> map = HashBiMap.create();

	@Override
	public V register(K id, V value)
	{
		V object = map.put(id, value);
		if(value != object && object != null)
		{
			Thuwumcraft.LOGGER.warn("Replacing registry value at: " + id.toString());
			Thuwumcraft.LOGGER.warn("Old Value: " + object);
			Thuwumcraft.LOGGER.warn("New Value: " + value);
		}
		return value;
	}

	@Override
	public V get(K id)
	{
		return map.get(id);
	}

	@Override
	public K getId(V value)
	{
		return map.inverse().get(value);
	}

	@Override
	public Set<V> values()
	{
		return map.values();
	}

	@Override
	public void clear()
	{
		map.clear();
	}
}
