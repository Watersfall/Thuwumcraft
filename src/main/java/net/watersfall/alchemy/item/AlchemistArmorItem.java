package net.watersfall.alchemy.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlchemistArmorItem extends ArmorItem
{
	private final float resistance;

	public AlchemistArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings, float resistance)
	{
		super(material, slot, settings);
		this.resistance = resistance;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.waters_alchemy_mod.magic_resistance_tooltip", resistance));
	}

	public float getResistance()
	{
		return this.resistance;
	}
}
