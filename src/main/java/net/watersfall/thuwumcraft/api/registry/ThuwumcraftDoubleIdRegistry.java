package net.watersfall.thuwumcraft.api.registry;

import net.minecraft.util.Identifier;

public interface ThuwumcraftDoubleIdRegistry<O, V> extends ThuwumcraftRegistry<Identifier, V>
{
	V getByOtherId(O key);

	O getOtherId(V value);

	V register(Identifier id, O otherId, V value);

	@Override
	default V register(Identifier id, V value)
	{
		throw new UnsupportedOperationException("Cannot use single id register method on a double id registry");
	}
}
