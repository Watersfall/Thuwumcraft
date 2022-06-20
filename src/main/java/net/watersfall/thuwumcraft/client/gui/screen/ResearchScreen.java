package net.watersfall.thuwumcraft.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.client.gui.element.ItemRequirementElement;
import net.watersfall.thuwumcraft.client.gui.element.RecipeTabElement;
import net.watersfall.thuwumcraft.client.gui.element.ResearchButton;
import net.watersfall.thuwumcraft.client.gui.element.TooltipElement;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.List;

public class ResearchScreen extends Screen
{
	public static final Identifier BACKGROUND = new Identifier("textures/gui/book.png");

	private final PlayerResearchAbility ability = AbilityProvider.getProvider(MinecraftClient.getInstance().player)
			.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class).get();
	private final ResearchBookScreen parent;
	private final Research research;
	private final int screenWidth = 365;
	private final int screenHeight = 225;
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
		int startY = 16;
		x = (width - screenWidth) / 2;
		y = (height - screenHeight) / 2;
		this.researchButton = new ResearchButton(this.research, this.x + 218, this.y + 191);
		for(int i = 0; i < this.tabs.length; i++)
		{
			this.addDrawableChild(new RecipeTabElement(tabs[i], x + screenWidth - 12, this.y + startY +  i * 20, true));
		}
		startX = this.x + 274 - (10 * research.getRequiredItems().size() + 10 * research.getConsumedItems().size());
		int i;
		for(i = 0; i < this.research.getRequiredItems().size(); i++)
		{
			this.addDrawableChild(new ItemRequirementElement(research.getRequiredItems().get(i).getMatchingStacks(), startX + i * 20, this.y + 171, false));
		}
		startX = this.x + 274 - (10 * research.getRequiredItems().size() + 10 * research.getConsumedItems().size()) + i * 20;
		for(i = 0; i < this.research.getConsumedItems().size(); i++)
		{
			this.addDrawableChild(new ItemRequirementElement(research.getConsumedItems().get(i).getMatchingStacks(), startX + i * 20, this.y + 171, true));
		}
		this.addDrawableChild(researchButton);
		if(!ability.hasResearch(research) && research.getConsumedItems().isEmpty() && research.getRequiredItems().isEmpty())
		{
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeIdentifier(research.getId());
			ClientPlayNetworking.send(Thuwumcraft.getId("research_click"), buf);
		}
	}

	@Override
	public void renderBackground(MatrixStack matrices)
	{
		super.renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		matrices.push();
		matrices.translate(x, y, 0);
		matrices.scale(1.25f, 1.25f, 1.25f);
		matrices.translate(-x, -y, 0);
		drawTexture(matrices, this.x, this.y, 90, 1, 146, 181, -256, 256);
		drawTexture(matrices, this.x + 146, this.y, 20, 1, 146, 181, 256, 256);
		matrices.pop();
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
			completedLines = this.textRenderer.wrapLines(this.research.getCompletedDescription(), 149);
			drawText(matrices, completedLines);
		}
		else
		{
			lines = this.textRenderer.wrapLines(this.research.getDescription(), 149);
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
			this.textRenderer.draw(matrices, text.get(i), this.x + 18, this.y + offset, 4210752);
		}
	}

	@Override
	public void close()
	{
		this.client.setScreen(parent);
	}

	@Override
	public boolean shouldPause()
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
