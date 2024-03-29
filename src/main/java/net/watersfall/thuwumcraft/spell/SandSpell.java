package net.watersfall.thuwumcraft.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.spell.CastingType;
import net.watersfall.thuwumcraft.api.spell.Spell;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.entity.spell.SandEntity;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSounds;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpells;
import net.watersfall.thuwumcraft.spell.data.SandSpellData;

public class SandSpell extends Spell<SandSpellData>
{
	public SandSpell()
	{
		super(ThuwumcraftSpells.SAND, 10, 5, 1, CastingType.SINGLE, 0xFFFF00, new SandSpellData());
	}

	public SandSpell(SpellType<SandSpell> type, NbtCompound tag)
	{
		super(type, tag);
	}

	@Override
	public TypedActionResult<ItemStack> cast(ItemStack stack, World world, PlayerEntity player)
	{
		SandEntity entity = new SandEntity(world, player);
		Vec3d pos = player.getPos();
		pos = pos.add(player.getRotationVector().multiply(1.5));
		entity.setPos(pos.x, pos.y, pos.z);
		entity.updateTrackedPosition(pos.x, pos.y, pos.z);
		entity.setVelocity(player.getRotationVector());
		entity.setBlindnessTime(modifiers.getBlindingTime());
		world.playSoundFromEntity(player, player, ThuwumcraftSounds.POCKET_SAND, SoundCategory.PLAYERS, 1.0F, 1.0F);
		world.spawnEntity(entity);
		return TypedActionResult.success(stack);
	}
}
