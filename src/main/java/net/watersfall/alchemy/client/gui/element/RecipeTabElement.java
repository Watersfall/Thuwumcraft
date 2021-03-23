package net.watersfall.alchemy.client.gui.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.client.gui.ResearchTab;

public class RecipeTabElement extends TabElement
{
	private final ResearchTab tab;

	public RecipeTabElement(ResearchTab tab, int x, int y, boolean inverted)
	{
		super(null, x, y, inverted);
		this.items = new ItemElement(tab.getItems(), x, y, inverted ? x + 4 : x - 4, y, this);
		this.tab = tab;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(button == 0 && isMouseOver(mouseX, mouseY))
		{
			if(MinecraftClient.getInstance().currentScreen != this.tab)
			{
				MinecraftClient.getInstance().player.playSound(AlchemySounds.BOOK_OPEN_SOUND, SoundCategory.PLAYERS, 1.0F, (float)Math.random() * 0.2F + 1.1F);
			}
			MinecraftClient.getInstance().openScreen(this.tab);
			return true;
		}
		return false;
	}
}
