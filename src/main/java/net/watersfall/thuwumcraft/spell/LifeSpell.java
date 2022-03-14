package net.watersfall.thuwumcraft.spell;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.spell.CastingType;
import net.watersfall.thuwumcraft.api.spell.Spell;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.network.s2c.HealingParticleS2CPacket;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSpells;
import net.watersfall.thuwumcraft.spell.data.LifeSpellData;

public class LifeSpell extends Spell<LifeSpellData>
{
	public LifeSpell(SpellType<LifeSpell> type, NbtCompound nbt)
	{
		super(type, nbt);
	}

	public LifeSpell()
	{
		super(ThuwumcraftSpells.LIFE, 40, 20, 1, CastingType.SINGLE, 0xFF4444, new LifeSpellData(4, false));
	}

	@Override
	public TypedActionResult<ItemStack> cast(ItemStack stack, World world, PlayerEntity player)
	{
		if(!modifiers.isProjectile() && !world.isClient)
		{
			player.heal(modifiers.getAmount());
			HealingParticleS2CPacket packet = new HealingParticleS2CPacket(player, modifiers.getAmount());
			PacketByteBuf buf = PacketByteBufs.create();
			packet.write(buf);
			ServerPlayNetworking.send((ServerPlayerEntity)player, HealingParticleS2CPacket.ID, buf);
			PlayerLookup.tracking(player).forEach(target -> {
				ServerPlayNetworking.send(target, HealingParticleS2CPacket.ID, buf);
			});
		}
		return TypedActionResult.success(stack, world.isClient);
	}
}
