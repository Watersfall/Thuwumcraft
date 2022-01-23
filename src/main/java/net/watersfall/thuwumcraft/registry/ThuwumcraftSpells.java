package net.watersfall.thuwumcraft.registry;

import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.spell.*;

public class ThuwumcraftSpells
{
	public static SpellType<FireSpell> FIRE;
	public static SpellType<IceSpell> ICE;
	public static SpellType<WaterSpell> WATER;
	public static SpellType<SandSpell> SAND;
	public static SpellType<SnowSpell> SNOW;

	public static void register()
	{
		FIRE = (SpellType<FireSpell>)ThuwumcraftRegistry.SPELL.register(Thuwumcraft.getId("fire"), new SpellType<>(FireSpell::new, FireSpell::new));
		ICE = (SpellType<IceSpell>)ThuwumcraftRegistry.SPELL.register(Thuwumcraft.getId("ice"), new SpellType<>(IceSpell::new, IceSpell::new));
		WATER = (SpellType<WaterSpell>)ThuwumcraftRegistry.SPELL.register(Thuwumcraft.getId("water"), new SpellType<>(WaterSpell::new, WaterSpell::new));
		SAND = (SpellType<SandSpell>)ThuwumcraftRegistry.SPELL.register(Thuwumcraft.getId("sand"), new SpellType<>(SandSpell::new, SandSpell::new));
		SNOW = (SpellType<SnowSpell>)ThuwumcraftRegistry.SPELL.register(Thuwumcraft.getId("snow"), new SpellType<>(SnowSpell::new, SnowSpell::new));
	}
}
