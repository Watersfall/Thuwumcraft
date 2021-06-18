package net.watersfall.thuwumcraft.api.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BeforeActions
{
	/**
	 * @return True to cancel attacking
	 */
	default boolean beforeHit(ItemStack stack, LivingEntity target, LivingEntity attacker) { return false; }

	/**
	 * @return True to cancel block breaking
	 */
	default boolean beforeMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) { return false; }
}
