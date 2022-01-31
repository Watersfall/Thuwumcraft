package net.watersfall.thuwumcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.spell.CastingType;
import net.watersfall.thuwumcraft.api.spell.Spell;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.api.spell.modifier.IntegerSpellModifier;
import net.watersfall.thuwumcraft.entity.spell.FireEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpells;
import net.watersfall.thuwumcraft.spell.data.FireSpellData;

public class FireSpell extends Spell<FireSpellData>
{

	public FireSpell(SpellType<FireSpell> type, NbtCompound tag)
	{
		super(type, tag);
	}

	public FireSpell()
	{
		super(ThuwumcraftSpells.FIRE, 10, 1, 1, CastingType.CONTINUOUS, 0xFF0000, new FireSpellData(new IntegerSpellModifier("spell_modifier.thuwumcraft.damage", 1, 5, 3), new IntegerSpellModifier("spell_modifier.thuwumcraft.fire_time", 0, 3, 1)));
	}

	@Override
	public TypedActionResult<ItemStack> cast(ItemStack stack, World world, PlayerEntity player)
	{
		FireEntity entity = new FireEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(2));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector().multiply(0.5));
		entity.setFireTime(modifiers.getFireTime());
		entity.setDamage(modifiers.getDamage());
		world.playSoundFromEntity(player, player, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	}
}
