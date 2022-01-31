package net.watersfall.thuwumcraft.client.gui.button;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import net.watersfall.thuwumcraft.api.spell.modifier.BooleanSpellModifier;

public class SpellToggleButton extends CheckboxWidget
{
	private final BooleanSpellModifier modifier;

	public SpellToggleButton(int x, int y, int width, int height, Text message, boolean checked, boolean showMessage, BooleanSpellModifier modifier)
	{
		super(x, y, width, height, message, checked, showMessage);
	 	this.modifier = modifier;
	}

	@Override
	public void onPress()
	{
		super.onPress();
		modifier.setValue(this.isChecked());
	}
}
