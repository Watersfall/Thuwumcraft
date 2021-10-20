package net.watersfall.thuwumcraft.item.golem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.watersfall.thuwumcraft.api.abilities.chunk.GolemMarkersAbility;
import net.watersfall.thuwumcraft.api.golem.GolemMarker;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.Optional;

//TODO turn into interface
public class GolemMarkerItem extends Item
{
	private final DyeColor color;
	private String translationKey = null;

	public GolemMarkerItem(Settings settings, DyeColor color)
	{
		super(settings);
		this.color = color;
	}

	@Override
	protected String getOrCreateTranslationKey()
	{
		if(translationKey == null)
		{
			translationKey = Util.createTranslationKey("item", Registry.ITEM.getId(this)).replace("." + color.getName(), "");
		}
		return translationKey;
	}

	@Override
	public String getTranslationKey()
	{
		return this.getOrCreateTranslationKey();
	}

	@Override
	public Text getName(ItemStack stack)
	{
		return new TranslatableText(this.getOrCreateTranslationKey(), new TranslatableText("color.minecraft." + color.getName()));
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
				if(context.getPlayer() != null && context.getPlayer().isSneaking())
				{
					ability.removeMarkers(context.getBlockPos());
				}
				else
				{
					Optional<GolemMarker> check = ability.getMarker(context.getBlockPos(), context.getSide());
					if(check.isPresent())
					{
						ability.removeMarker(context.getBlockPos(), context.getSide());
					}
					else
					{
						ability.addMarker(new GolemMarker(color, context.getBlockPos(), chunk.getPos(), context.getSide()));
					}
				}
				ability.sync(chunk);
			}
		}
		return ActionResult.success(context.getWorld().isClient);
	}
}
