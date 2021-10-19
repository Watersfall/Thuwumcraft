package net.watersfall.thuwumcraft.item.golem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.api.abilities.chunk.GolemMarkersAbility;
import net.watersfall.thuwumcraft.api.golem.GolemMarker;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.Optional;

//TODO turn into interface
public class GolemMarkerItem extends Item
{
	public GolemMarkerItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		if(!context.getWorld().isClient)
		{
			Chunk chunk = context.getWorld().getChunk(context.getBlockPos());
			AbilityProvider<Chunk> provider = AbilityProvider.getProvider(chunk);
			Optional<GolemMarkersAbility> optional = provider.getAbility(GolemMarkersAbility.ID, GolemMarkersAbility.class);
			if(optional.isPresent())
			{
				GolemMarkersAbility ability = optional.get();
				ability.addMarker(new GolemMarker(DyeColor.BLUE, context.getBlockPos(), chunk.getPos(), context.getSide()));
				ability.sync(chunk);
			}
		}
		return ActionResult.success(context.getWorld().isClient);
	}
}
