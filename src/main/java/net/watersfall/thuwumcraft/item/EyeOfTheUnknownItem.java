package net.watersfall.thuwumcraft.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.Optional;

public class EyeOfTheUnknownItem extends Item
{
	private static final TranslatableText knownName = new TranslatableText("item.thuwumcraft.eye_of_the_known");
	private static final TranslatableText unknownName = new TranslatableText("item.thuwumcraft.eye_of_the_unknown");

	public EyeOfTheUnknownItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public Text getName(ItemStack stack)
	{
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
		{
			return getClientName(stack);
		}
		return knownName;
	}

	public Text getClientName(ItemStack stack)
	{
		if(MinecraftClient.getInstance().player != null)
		{
			//Todo: A better way of doing this
			Optional<PlayerResearchAbility> optional = AbilityProvider.getProvider(MinecraftClient.getInstance().player).getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
			if(optional.isPresent() && optional.get().hasAdvancement(Thuwumcraft.getId("the_unknown/root")))
			{
				return knownName;
			}
		}
		return unknownName;
	}
}
