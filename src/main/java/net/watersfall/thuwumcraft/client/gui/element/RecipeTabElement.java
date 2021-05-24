package net.watersfall.thuwumcraft.client.gui.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.sound.AlchemySounds;
import net.watersfall.thuwumcraft.client.gui.ResearchTab;

public class RecipeTabElement extends TabElement
{
	private final ResearchTab tab;
	private final PlayerResearchAbility ability;

	public RecipeTabElement(ResearchTab tab, int x, int y, boolean inverted)
	{
		super(null, x, y, inverted);
		this.items = new ItemElement(tab.getItems(), x, y, inverted ? x + 4 : x - 4, y, this);
		this.tab = tab;
		ability = AbilityProvider.getProvider(MinecraftClient.getInstance().player).getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(button == 0 && isMouseOver(mouseX, mouseY) && (!tab.requiresComplete() || ability.hasResearch(tab.getResearch())))
		{
			if(MinecraftClient.getInstance().currentScreen != this.tab)
			{
				MinecraftClient.getInstance().player.playSound(AlchemySounds.BOOK_OPEN_SOUND, SoundCategory.PLAYERS, 1.0F, (float)Math.random() * 0.2F + 1.1F);
			}
			this.tab.getParent().childOpen = true;
			MinecraftClient.getInstance().openScreen(this.tab);
			return true;
		}
		return false;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		if(!tab.requiresComplete() || ability.hasResearch(tab.getResearch()))
		{
			super.render(matrices, mouseX, mouseY, delta);
		}
	}
}
