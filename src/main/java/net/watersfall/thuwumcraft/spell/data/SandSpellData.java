package net.watersfall.thuwumcraft.spell.data;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.api.spell.SpellModifierData;
import net.watersfall.thuwumcraft.api.spell.SpellModifierDataType;
import net.watersfall.thuwumcraft.api.spell.modifier.IntegerSpellModifier;
import net.watersfall.thuwumcraft.api.spell.modifier.SpellModifier;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpellData;

import java.util.List;

public class SandSpellData extends SpellModifierData
{
	private final IntegerSpellModifier blinding;

	public SandSpellData(SpellModifierDataType<? extends SpellModifierData> type, NbtCompound tag)
	{
		super(type, tag);
		blinding = new IntegerSpellModifier(tag.getCompound("spell_modifier.thuwumcraft.blindness"));
	}

	public SandSpellData()
	{
		super(ThuwumcraftSpellData.SAND, new NbtCompound());
		blinding = new IntegerSpellModifier("spell_modifier.thuwumcraft.blindness", 1, 10, 5);
	}

	public int getBlindingTime()
	{
		return blinding.getValue();
	}

	@Override
	public List<SpellModifier> getModifiers()
	{
		return List.of(blinding);
	}
}
