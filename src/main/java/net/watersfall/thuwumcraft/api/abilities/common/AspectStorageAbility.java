package net.watersfall.thuwumcraft.api.abilities.common;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.AlchemyMod;
import net.watersfall.thuwumcraft.api.abilities.Ability;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;

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
