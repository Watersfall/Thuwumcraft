package net.watersfall.thuwumcraft.registry;

import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.spell.EmptySpellModifierData;
import net.watersfall.thuwumcraft.api.spell.SpellModifierData;
import net.watersfall.thuwumcraft.spell.data.FireSpellData;
import net.watersfall.thuwumcraft.spell.data.IceSpellModifierData;
import net.watersfall.thuwumcraft.spell.data.SandSpellData;
import net.watersfall.thuwumcraft.spell.data.SnowSpellModifierData;
import net.watersfall.thuwumcraft.api.spell.SpellModifierDataType;

public class ThuwumcraftSpellData
{
	public static SpellModifierDataType<EmptySpellModifierData> EMPTY;
	public static SpellModifierDataType<IceSpellModifierData> ICE;
	public static SpellModifierDataType<SnowSpellModifierData> SNOW;
	public static SpellModifierDataType<FireSpellData> FIRE;
	public static SpellModifierDataType<SandSpellData> SAND;

	public static void register()
	{
		EMPTY = register("empty", EmptySpellModifierData::new);
		ICE = register("ice", IceSpellModifierData::new);
		SNOW = register("snow", SnowSpellModifierData::new);
		FIRE = register("fire", FireSpellData::new);
		SAND = register("sand", SandSpellData::new);
	}

	private static <T extends SpellModifierData> SpellModifierDataType<T> register(String id, SpellModifierDataType.SpellFactory<T> factory)
	{
		return (SpellModifierDataType<T>)ThuwumcraftRegistry.SPELL_DATA.register(Thuwumcraft.getId(id), new SpellModifierDataType<T>(factory));
	}
}
