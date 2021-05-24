package net.watersfall.alchemy.item.wand;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public interface WandCoreMaterial extends WandComponent
{
	public static final Registry REGISTRY = new Registry();

	double getMaxVis();

	Identifier getId();

	int getColor();

	public static class Registry
	{
		private final Map<Identifier, WandCoreMaterial> map = new HashMap<>();

		private Registry(){}

		public final WandCoreMaterial register(Identifier id, WandCoreMaterial core)
		{
			map.put(id, core);
			return core;
		}

		public final WandCoreMaterial get(Identifier id)
		{
			return map.get(id);
		}

		public final WandCoreMaterial getByItem(Item item)
		{
			for(WandCoreMaterial material : map.values())
			{
				if(material.getItemStack().isOf(item))
				{
					return material;
				}
			}
			return null;
		}
	}
}
