package net.watersfall.alchemy.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.screen.ResearchBookHandler;

public class ResearchBookItem extends Item
{
	public ResearchBookItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		user.openHandledScreen(createScreenHandlerFactory(user.getStackInHand(hand)));
		world.playSound(
				user.getX(),
				user.getEyeY(),
				user.getZ(),
				AlchemySounds.BOOK_OPEN_SOUND,
				SoundCategory.PLAYERS,
				1.0F,
				(float)Math.random() * 0.2F + 1.1F,
				false);
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

	private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack)
	{
		return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
				new ResearchBookHandler(syncId, player.getInventory()), stack.getName());
	}
}
