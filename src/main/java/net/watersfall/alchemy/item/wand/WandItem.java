package net.watersfall.alchemy.item.wand;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.item.WandAbility;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WandItem extends Item
{
	public WandItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack stack = user.getStackInHand(hand);
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		provider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(ability -> {
			ability.getSpell().cast(stack, world, user);
		});
		return TypedActionResult.success(stack);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		provider.getAbility(WandAbility.ID, WandAbility.class).ifPresent(ability -> {
			tooltip.add(new TranslatableText("item.waters_alchemy_mod.wand.core").append(": ").append(new LiteralText("Wood")));
			tooltip.add(new TranslatableText("item.waters_alchemy_mod.wand.cap").append(": ").append(new LiteralText("Iron")));
			tooltip.add(new TranslatableText("item.waters_alchemy_mod.wand.spell").append(": ").append(new LiteralText("Sand")));
		});
	}
}
