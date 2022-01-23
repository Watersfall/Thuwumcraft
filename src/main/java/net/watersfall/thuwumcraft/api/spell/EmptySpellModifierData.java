package net.watersfall.thuwumcraft.api.spell;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpellData;
import net.watersfall.thuwumcraft.api.spell.modifier.SpellModifier;

import java.util.List;

public class EmptySpellModifierData extends SpellModifierData
{
	public EmptySpellModifierData()
	{
		super(ThuwumcraftSpellData.EMPTY, new NbtCompound());
	}

	public EmptySpellModifierData(SpellModifierDataType<? extends SpellModifierData> type, NbtCompound tag)
	{
		super(type, tag);
	}

	@Override
	public List<SpellModifier> getModifiers()
	{
		return List.of();
	}
}
