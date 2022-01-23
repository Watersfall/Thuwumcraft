package net.watersfall.thuwumcraft.spell;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.api.spell.SpellModifierData;
import net.watersfall.thuwumcraft.api.spell.SpellModifierDataType;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpellData;
import net.watersfall.thuwumcraft.api.spell.modifier.IntegerSpellModifier;
import net.watersfall.thuwumcraft.api.spell.modifier.SpellModifier;

import java.util.List;

public class SnowSpellModifierData extends SpellModifierData
{
	public final IntegerSpellModifier velocityModifier;

	public SnowSpellModifierData(SpellModifierDataType<? extends SnowSpellModifierData> type, NbtCompound tag)
	{
		super(type, tag);
		this.velocityModifier = new IntegerSpellModifier(tag);
	}

	public SnowSpellModifierData(IntegerSpellModifier velocityModifier)
	{
		super(ThuwumcraftSpellData.SNOW, new NbtCompound());
		this.velocityModifier = velocityModifier;
	}

	@Override
	public List<SpellModifier> getModifiers()
	{
		return List.of(velocityModifier);
	}
}
