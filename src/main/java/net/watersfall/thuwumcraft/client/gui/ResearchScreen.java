package net.watersfall.thuwumcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.AbilityProvider;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.client.gui.element.ItemRequirementElement;
import net.watersfall.thuwumcraft.client.gui.element.RecipeTabElement;
import net.watersfall.thuwumcraft.client.gui.element.ResearchButton;
import net.watersfall.thuwumcraft.client.gui.element.TooltipElement;

import java.util.List;

public class ResearchScreen extends Screen
{
	public static final Identifier BACKGROUND = Thuwumcraft.getId("textures/gui/research/research_page.png");

	private final PlayerResearchAbility ability = AbilityProvider.getProvider(MinecraftClient.getInstance().player)
			.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get();
	private final ResearchBookScreen parent;
	private final Research research;
	private final int textureWidth = 384;
	private final int textureHeight = 272;
	private final int screenWidth = 384;
	private final int screenHeight = 256;
	private int x;
	private int y;
	private final ResearchTab[] tabs;
	private ResearchButton researchButton;
	private List<OrderedText> lines;
	private List<OrderedText> completedLines;
	public boolean childOpen = false;

	public ResearchScreen(ResearchBookScreen parent, Research research)
	{
		super(research.getName());
		this.parent = parent;
		this.research = research;
		this.tabs = new ResearchTab[research.getTabs().length];
		for(int i = 0; i < tabs.length; i++)
		{
			this.tabs[i] = new ResearchTab(research.getTabs()[i], this);
		}
		lines = null;
		completedLines = null;
	}

	@Override
	protected void init()
	{
		super.init();
		int startX = 378;
		int startY = 20;
		x = (width - screenWidth) / 2;
		y = (height - screenHeight) / 2;
		this.researchButton = new ResearchButton(this.research, this.x + 227, this.y + 217);
		for(int i = 0; i < this.tabs.length; i++)
		{
			this.addDrawableChild(new RecipeTabElement(tabs[i], this.x + startX, this.y + startY +  i * 24, true));
		}
		startX = this.x + 283 - (10 * research.getRequiredItems().size() + 10 * research.getConsumedItems().size());
		int i;
		for(i = 0; i < this.research.getRequiredItems().size(); i++)
		{
			this.addDrawableChild(new ItemRequirementElement(research.getRequiredItems().get(i).getMatchingStacks(), startX + i * 20, this.y + 195, false));
		}
		startX = this.x + 283 - (10 * research.getRequiredItems().size() + 10 * research.getConsumedItems().size()) + i * 20;
		for(i = 0; i < this.research.getConsumedItems().size(); i++)
		{
			this.addDrawableChild(new ItemRequirementElement(research.getConsumedItems().get(i).getMatchingStacks(), startX + i * 20, this.y + 195, true));
		}
		this.addDrawableChild(researchButton);
	}

	@Override
	public void renderBackground(MatrixStack matrices)
	{
		super.renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		drawTexture(matrices, x, y, 0, 0, screenWidth, screenHeight, textureWidth, textureHeight);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.renderBackground(matrices);
		if(!ability.hasResearch(this.research) && this.research.isAvailable(ability))
		{
			this.researchButton.enable();
		}
		else
		{
			this.researchButton.disable();
		}
		if(ability.hasResearch(this.research))
		{
			if(completedLines == null)
			{
				completedLines = this.textRenderer.wrapLines(this.research.getCompletedDescription(), 160);
			}
			drawText(matrices, completedLines);
		}
		else
		{
			if(lines == null)
			{
				lines = this.textRenderer.wrapLines(this.research.getDescription(), 160);
			}
			drawText(matrices, lines);
		}
		for(int i = 0; i < this.children().size(); i++)
		{
			if(this.children().get(i) instanceof Drawable)
			{
				if(this.children().get(i) instanceof ItemRequirementElement)
				{
					if(!ability.hasResearch(research))
					{
						((Drawable)this.children().get(i)).render(matrices, mouseX, mouseY, delta);
					}
				}
				else
				{
					((Drawable)this.children().get(i)).render(matrices, mouseX, mouseY, delta);
				}
			}
		}
		if(!childOpen)
		{
			for(int i = 0; i < this.children().size(); i++)
			{
				if(this.children().get(i).isMouseOver(mouseX, mouseY) && this.children().get(i) instanceof TooltipElement)
				{
					this.renderTooltip(matrices, ((TooltipElement)this.children().get(i)).getTooltip(mouseX, mouseY), mouseX, mouseY);
				}
			}
		}
	}

	private void drawText(MatrixStack matrices, List<OrderedText> text)
	{
		textRenderer.draw(matrices, this.title, this.x + this.screenWidth / 4F - (textRenderer.getWidth(this.title.asOrderedText()) / 2F), this.y + 24, 4210752);
		int offset = 40;
		for(int i = 0; i < text.size(); i++, offset += 9)
		{
			this.textRenderer.draw(matrices, text.get(i), this.x + 16F, this.y + offset, 4210752);
		}
	}

	@Override
	public void onClose()
	{
		this.client.setScreen(parent);
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	public Research getResearch()
	{
		return this.research;
	}

	public ResearchButton getResearchButton()
	{
		return this.researchButton;
	}
}
