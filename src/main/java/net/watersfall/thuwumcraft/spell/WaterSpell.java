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
import net.watersfall.thuwumcraft.api.spell.EmptySpellModifierData;
import net.watersfall.thuwumcraft.api.spell.Spell;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.entity.spell.WaterEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpells;

public class WaterSpell extends Spell<EmptySpellModifierData>
{
	public WaterSpell()
	{
		super(ThuwumcraftSpells.WATER, 10, 1, 1, CastingType.CONTINUOUS, 0x0000FF, new EmptySpellModifierData());
	}

	public WaterSpell(SpellType<WaterSpell> type, NbtCompound tag)
	{
		super(type, tag);
	}

	@Override
	public TypedActionResult<ItemStack> cast(ItemStack stack, World world, PlayerEntity player)
	{
		WaterEntity entity = new WaterEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(2));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector().multiply(0.5));
		world.playSoundFromEntity(player, player, SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	}
}
