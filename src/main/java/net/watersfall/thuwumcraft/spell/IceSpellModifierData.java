package net.watersfall.thuwumcraft.spell;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpellData;
import net.watersfall.thuwumcraft.spell.modifier.IntegerSpellModifier;
import net.watersfall.thuwumcraft.spell.modifier.SpellModifier;

import java.util.List;

public class IceSpellModifierData extends SpellModifierData
{
	public final IntegerSpellModifier velocityModifier;

	public IceSpellModifierData(SpellModifierDataType<? extends IceSpellModifierData> type, NbtCompound tag)
	{
		super(type, tag);
		this.velocityModifier = new IntegerSpellModifier(tag);
	}

	public IceSpellModifierData(IntegerSpellModifier velocityModifier)
	{
		super(ThuwumcraftSpellData.ICE, new NbtCompound());
		this.velocityModifier = velocityModifier;
	}

	@Override
	public List<SpellModifier> getModifiers()
	{
		return List.of(velocityModifier);
	}
}
