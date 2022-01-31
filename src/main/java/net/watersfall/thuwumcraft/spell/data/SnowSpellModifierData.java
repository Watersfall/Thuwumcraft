package net.watersfall.thuwumcraft.spell.data;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.api.spell.SpellModifierData;
import net.watersfall.thuwumcraft.api.spell.SpellModifierDataType;
import net.watersfall.thuwumcraft.api.spell.modifier.BooleanSpellModifier;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpellData;
import net.watersfall.thuwumcraft.api.spell.modifier.IntegerSpellModifier;
import net.watersfall.thuwumcraft.api.spell.modifier.SpellModifier;

import java.util.List;

public class SnowSpellModifierData extends SpellModifierData
{
	public final IntegerSpellModifier velocityModifier;
	public final BooleanSpellModifier icy;

	public SnowSpellModifierData(SpellModifierDataType<? extends SnowSpellModifierData> type, NbtCompound tag)
	{
		super(type, tag);
		this.velocityModifier = new IntegerSpellModifier(tag.getCompound("spell_modifier.thuwumcraft.velocity"));
		this.icy = new BooleanSpellModifier(tag.getCompound("spell_modifier.thuwumcraft.icy"));
	}

	public SnowSpellModifierData(IntegerSpellModifier velocityModifier, BooleanSpellModifier icy)
	{
		super(ThuwumcraftSpellData.SNOW, new NbtCompound());
		this.velocityModifier = velocityModifier;
		this.icy = icy;
	}

	public boolean isIcy()
	{
		return icy.getValue();
	}

	@Override
	public List<SpellModifier> getModifiers()
	{
		return List.of(velocityModifier, icy);
	}
}
