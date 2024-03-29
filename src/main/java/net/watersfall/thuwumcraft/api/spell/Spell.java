package net.watersfall.thuwumcraft.api.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.block.entity.FocalManipulatorBlockEntity;

/**
 * TODO: Arbitrary Spell Data
 *
 * 		Arbitrary spell data should be attachable to spells.
 * An example would be applying penalties to any spell with too
 * high of a power, or other random effects when casting
 */

public abstract class Spell<T extends SpellModifierData>
{
	protected final SpellType<? extends Spell<T>> type;
	protected final int cooldown;
	protected final int castingTime;
	protected final double cost;
	protected final CastingType castingType;
	protected final T modifiers;
	protected final int color;
	protected String translationKey;

	public Spell(SpellType<? extends Spell<T>> type, NbtCompound nbt)
	{
		this.type = type;
		this.cooldown = nbt.getInt("cooldown");
		this.castingTime = nbt.getInt("casting_time");
		this.cost = nbt.getDouble("cost");
		this.castingType = CastingType.valueOf(nbt.getString("casting_type"));
		this.color = nbt.getInt("color");
		NbtCompound dataTag = nbt.getCompound("data");
		this.modifiers = (T)ThuwumcraftRegistry.SPELL_DATA.get(new Identifier(dataTag.getString("id"))).create(dataTag);
	}

	public Spell(SpellType<? extends Spell<T>> type, int cooldown, int castingTime, double cost, CastingType castingType, int color, T modifiers)
	{
		this.type = type;
		this.cooldown = cooldown;
		this.castingTime = castingTime;
		this.cost = cost;
		this.castingType = castingType;
		this.modifiers = modifiers;
		this.color = color;
	}

	public int getCooldown()
	{
		return cooldown;
	}

	public int getCastingTime()
	{
		return castingTime;
	}

	public double getCost()
	{
		return cost;
	}

	public CastingType getCastingType()
	{
		return castingType;
	}

	public T getModifiers()
	{
		return modifiers;
	}

	public SpellType<? extends Spell<T>> getType()
	{
		return type;
	}

	public abstract TypedActionResult<ItemStack> cast(ItemStack stack, World world, PlayerEntity player);

	public int getColor()
	{
		return color;
	}

	public void toNbt(NbtCompound nbt)
	{
		nbt.putInt("cooldown", cooldown);
		nbt.putInt("casting_time", castingTime);
		nbt.putDouble("cost", cost);
		nbt.putString("casting_type", castingType.name());
		nbt.put("data", modifiers.toNbt(new NbtCompound()));
		nbt.putInt("color", color);
	}

	public String getTranslationKey()
	{
		if(translationKey == null)
		{
			translationKey = Util.createTranslationKey("spell", ThuwumcraftRegistry.SPELL.getId(this.getType()));
		}
		return translationKey;
	}

	public boolean isAvailable(ServerPlayerEntity player, FocalManipulatorBlockEntity blockEntity, PlayerResearchAbility research)
	{
		return true;
	}

	public TranslatableText getName()
	{
		return new TranslatableText(getTranslationKey());
	}
}
