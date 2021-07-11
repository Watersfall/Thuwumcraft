package net.watersfall.thuwumcraft.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
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
		if(world.isClient)
		{
			MinecraftClient.getInstance().world.playSoundFromEntity(user, user, AlchemySounds.BOOK_OPEN_SOUND, SoundCategory.PLAYERS, 1.0F, (float)Math.random() * 0.2F + 1.1F);
		}
		user.openHandledScreen(createScreenHandlerFactory(user.getStackInHand(hand)));
		return TypedActionResult.consume(user.getStackInHand(hand));
	}

	private NamedScreenHandlerFactory createScreenHandlerFactory(ItemStack stack)
	{
		return new ExtendedScreenHandlerFactory()
		{
			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
			{
				return new ResearchBookHandler(syncId, player.getInventory(), null);
			}

			@Override
			public Text getDisplayName()
			{
				return new LiteralText("");
			}

			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf)
			{
				AbilityProvider.getProvider(player).getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).ifPresent(ability -> {
					ability.toPacket(buf);
				});
			}
		};
	}
}
