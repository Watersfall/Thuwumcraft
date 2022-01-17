package net.watersfall.thuwumcraft.item.wand;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.api.abilities.item.WandFocusAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.wet.api.abilities.AbilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WandFocusItem extends Item
{
	public WandFocusItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
		AbilityProvider<ItemStack> provider = AbilityProvider.getProvider(stack);
		provider.getAbility(WandFocusAbility.ID, WandFocusAbility.class).ifPresent(ability -> {
			if(ability.getSpell() != null)
			{
				tooltip.add(new TranslatableText("item.thuwumcraft.wand.spell").append(": ").append(new LiteralText(ThuwumcraftRegistry.SPELL.getId(ability.getSpell().getType()).toString())));
			}
		});
	}
}
