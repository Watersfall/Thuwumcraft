package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.chunk.VisAbility;

public class VisLiquifierEntity extends BlockEntity
{
	public VisLiquifierEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.VIS_LIQUIFIER_ENTITY, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, VisLiquifierEntity entity)
	{
		if(world.getTime() % 20 == 10)
		{
			Chunk chunk = world.getWorldChunk(pos);
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(chunk);
			provider.getAbility(VisAbility.ID, VisAbility.class).ifPresent(ability -> {
				if(ability.getVis() > 5)
				{
					ability.setVis(ability.getVis() - 5);
					ability.sync(chunk);
					ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Items.DIAMOND.getDefaultStack());
				}
			});
		}
	}
}
