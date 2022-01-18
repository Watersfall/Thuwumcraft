package net.watersfall.thuwumcraft.item.wand;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface WandCapMaterial extends WandComponent
{
	CapRechargeType getRechargeType();

	Identifier getId();

	int getColor();

	String getTranslationKey();

	Text getName();
}
