package net.watersfall.thuwumcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpells;
import net.watersfall.thuwumcraft.spell.modifier.IntegerSpellModifier;

public class SnowSpell extends Spell<SnowSpellModifierData>
{
	public SnowSpell()
	{
		super(ThuwumcraftSpells.SNOW, 10, 5, 1, CastingType.SINGLE, 0xCCCCCC, new SnowSpellModifierData(new IntegerSpellModifier("velocity", 0, 10, 5)));
	}

	public SnowSpell(SpellType<SnowSpell> type, NbtCompound tag)
	{
		super(type, tag);
	}

	@Override
	public TypedActionResult<ItemStack> cast(ItemStack stack, World world, PlayerEntity player)
	{
		double modifier = getModifiers().velocityModifier.getValue() / 4F;
		SnowballEntity snowball = new SnowballEntity(world, player);
		snowball.setVelocity(player.getRotationVector().multiply(modifier));
		world.playSoundFromEntity(player, player, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(snowball);
		return TypedActionResult.success(stack);
	}
}
