package net.watersfall.alchemy.api.abilities.common;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.aspect.Aspect;
import net.watersfall.alchemy.api.aspect.AspectStack;

import java.util.HashMap;
import java.util.List;

public interface AspectStorageAbility<T> extends Ability<T>
{
	public static final Identifier ID = AlchemyMod.getId("aspect_storage_ability");

	NbtCompound getTag();

	@Override
	default Identifier getId()
	{
		return ID;
	}

	int getSize();

	AspectStack getAspect(Aspect aspect);

	AspectStack removeAspect(Aspect aspect, int count);

	void setAspect(AspectStack stack);

	void addAspect(AspectStack stack);

	List<AspectStack> getAspects();
}
