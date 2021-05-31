package net.watersfall.thuwumcraft.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.sound.AlchemySounds;
import net.watersfall.thuwumcraft.gui.ResearchBookHandler;

public class ResearchBookItem extends Item
{
	public ResearchBookItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		if(!world.isClient)
		{
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(user);
			provider.sync(user);
		}
		else
		{
			MinecraftClient.getInstance().world.playSoundFromEntity(user, user, AlchemySounds.BOOK_OPEN_SOUND, SoundCategory.PLAYERS, 1.0F, (float)Math.random() * 0.2F + 1.1F);
		}
		user.openHandledScreen(createScreenHandlerFactory(user.getStackInHand(hand)));
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

	private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack)
	{
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
				new ResearchBookHandler(syncId, player.getInventory()), stack.getName());
	}
}
