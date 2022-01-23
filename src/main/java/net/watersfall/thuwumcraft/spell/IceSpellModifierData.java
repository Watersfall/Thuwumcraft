package net.watersfall.thuwumcraft.spell;

import net.minecraft.nbt.NbtCompound;
import net.watersfall.thuwumcraft.api.spell.SpellModifierData;
import net.watersfall.thuwumcraft.api.spell.SpellModifierDataType;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpellData;
import net.watersfall.thuwumcraft.api.spell.modifier.IntegerSpellModifier;
import net.watersfall.thuwumcraft.api.spell.modifier.SpellModifier;

import java.util.List;

public class IceSpellModifierData extends SpellModifierData
{
	public final IntegerSpellModifier velocityModifier;
	public final IntegerSpellModifier otherModifier;

	public IceSpellModifierData(SpellModifierDataType<? extends IceSpellModifierData> type, NbtCompound tag)
	{
		super(type, tag);
		this.velocityModifier = new IntegerSpellModifier(tag.getCompound("velocity"));
		this.otherModifier = new IntegerSpellModifier(tag.getCompound("other"));
	}

	public IceSpellModifierData(IntegerSpellModifier velocityModifier)
	{
		super(ThuwumcraftSpellData.ICE, new NbtCompound());
		this.velocityModifier = velocityModifier;
		this.otherModifier = new IntegerSpellModifier("other", 0, 10, 2);
	}

	@Override
	public List<SpellModifier> getModifiers()
	{
		return List.of(velocityModifier, otherModifier);
	}
}
