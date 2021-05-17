package net.watersfall.alchemy.item.wand;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public interface WandCapMaterial
{
	public static final Registry REGISTRY = new Registry();

	CapRechargeType getRechargeType();

	Identifier getId();

	int getColor();

	public static class Registry
	{
		private final Map<Identifier, WandCapMaterial> map = new HashMap<>();

		private Registry(){}

		public final WandCapMaterial register(Identifier id, WandCapMaterial cap)
		{
			map.put(id, cap);
			return cap;
		}

		public final WandCapMaterial get(Identifier id)
		{
			return map.get(id);
		}
	}
}
