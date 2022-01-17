package net.watersfall.thuwumcraft.registry;

import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.spell.EmptySpellModifierData;
import net.watersfall.thuwumcraft.spell.IceSpellModifierData;
import net.watersfall.thuwumcraft.spell.SnowSpellModifierData;
import net.watersfall.thuwumcraft.spell.SpellModifierDataType;

public class ThuwumcraftSpellData
{
	public static SpellModifierDataType<EmptySpellModifierData> EMPTY;
	public static SpellModifierDataType<IceSpellModifierData> ICE;
	public static SpellModifierDataType<SnowSpellModifierData> SNOW;

	public static void register()
	{
		EMPTY = (SpellModifierDataType<EmptySpellModifierData>)ThuwumcraftRegistry.SPELL_DATA.register(Thuwumcraft.getId("empty"), new SpellModifierDataType<>(EmptySpellModifierData::new));
		ICE = (SpellModifierDataType<IceSpellModifierData>)ThuwumcraftRegistry.SPELL_DATA.register(Thuwumcraft.getId("ice"), new SpellModifierDataType<IceSpellModifierData>(IceSpellModifierData::new));
		SNOW = (SpellModifierDataType<SnowSpellModifierData>)ThuwumcraftRegistry.SPELL_DATA.register(Thuwumcraft.getId("snow"), new SpellModifierDataType<SnowSpellModifierData>(SnowSpellModifierData::new));
	}
}
