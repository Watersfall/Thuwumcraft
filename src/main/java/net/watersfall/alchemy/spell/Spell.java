package net.watersfall.alchemy.spell;

import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;

import java.util.Collection;
import java.util.HashMap;

public record Spell(SpellAction action, CastingType type, int castingTime, int cooldown, double visCost, int color)
{
	public static final Registry REGISTRY = new Registry();
	public static final Spell FIRE = REGISTRY.register(AlchemyMod.getId("fire"), new Spell(SpellAction.FIRE, CastingType.CONTINUOUS, 10, 1, 0.1, 0xFF0000));
	public static final Spell SAND = REGISTRY.register(AlchemyMod.getId("sand"), new Spell(SpellAction.SAND, CastingType.SINGLE, 5, 5, 0.5, 0xFFFF00));
	public static final Spell SNOW = REGISTRY.register(AlchemyMod.getId("snow"), new Spell(SpellAction.SNOW, CastingType.SINGLE, 5, 5, 0.25, 0xCCCCCC));
	public static final Spell WATER = REGISTRY.register(AlchemyMod.getId("water"), new Spell(SpellAction.WATER, CastingType.CONTINUOUS, 10, 1, 0.05, 0x0000FF));
	public static final Spell ICE = REGISTRY.register(AlchemyMod.getId("ice"), new Spell(SpellAction.ICE, CastingType.SINGLE, 10, 10, 1, 0x7FD2FF));

	public static class Registry
	{
		private final HashMap<Identifier, Spell> map = new HashMap<>();
		private final HashMap<Spell, Identifier> map2 = new HashMap<>();

		private Registry(){}

		public final Spell register(Identifier id, Spell spell)
		{
			map.put(id, spell);
			map2.put(spell, id);
			return spell;
		}

		public final Spell get(Identifier id)
		{
			return map.get(id);
		}

		public final Identifier getId(Spell spell)
		{
			return map2.get(spell);
		}

		public final Collection<Spell> getSpells()
		{
			return map.values();
		}
	}
}
