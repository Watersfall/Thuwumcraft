package net.watersfall.thuwumcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.entity.spell.IceProjectileEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpells;
import net.watersfall.thuwumcraft.spell.modifier.IntegerSpellModifier;

public class IceSpell extends Spell<IceSpellModifierData>
{
	public IceSpell()
	{
		super(ThuwumcraftSpells.ICE, 20, 10, 1, CastingType.SINGLE, 0x7FD2FF, new IceSpellModifierData(new IntegerSpellModifier("velocity", 0, 10, 4)));
	}

	public IceSpell(SpellType<IceSpell> type, NbtCompound tag)
	{
		super(type, tag);
	}

	@Override
	public TypedActionResult<ItemStack> cast(ItemStack stack, World world, PlayerEntity player)
	{
		double modifier = getModifiers().velocityModifier.getValue() / 4F;
		IceProjectileEntity entity = new IceProjectileEntity(world, player);
		entity.setVelocity(player.getRotationVector().multiply(modifier));
		world.playSoundFromEntity(player, player, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	}
}
