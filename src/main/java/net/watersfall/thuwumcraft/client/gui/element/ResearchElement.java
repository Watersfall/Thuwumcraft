package net.watersfall.thuwumcraft.client.gui.element;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSounds;
import net.watersfall.thuwumcraft.client.gui.screen.ResearchBookScreen;
import net.watersfall.thuwumcraft.client.gui.screen.ResearchScreen;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ResearchElement extends ItemElement
{
	private static final Identifier ICONS = new Identifier(Thuwumcraft.MOD_ID, "textures/gui/research/research_icons.png");
	private static final Identifier FONT_ID = new Identifier("minecraft", "alt");
	private static final Style STYLE = Style.EMPTY.withFont(FONT_ID);

	private final Research research;
	private final ResearchBookScreen screen;
	private final PlayerResearchAbility ability;
	private final List<Text> readableTooltip;
	private final List<Text> hiddenTooltip;
	private final ItemStack[] stacks;

	public ResearchElement(ResearchBookScreen screen, Research research)
	{
		super(research.getStack().getMatchingStacks(), research.getX(), research.getY());
		this.research = research;
		this.screen = screen;
		this.ability = screen.getAbility();
		this.readableTooltip = Lists.newArrayList(research.getName());
		this.hiddenTooltip = Lists.newArrayList(generateSecretText(research));
		this.stacks = research.getStack().getMatchingStacks();
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		float x = this.x * screen.scale + screen.getMapX();
		float y = this.y * screen.scale + screen.getMapY();
		return mouseX > x && mouseX < x + 16 * screen.scale && mouseY > y && mouseY < y + 16 * screen.scale;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(isMouseOver(mouseX, mouseY) && this.research.getCategory() == screen.getCurrentCategory())
		{
			if(research.isAvailable(ability))
			{
				MinecraftClient.getInstance().setScreen(new ResearchScreen(screen, research));
				MinecraftClient.getInstance().player.playSound(ThuwumcraftSounds.BOOK_OPEN_SOUND, SoundCategory.PLAYERS, 1.0F, (float)Math.random() * 0.2F + 1.1F);
				return true;
			}
		}
		return false;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		if(research.getCategory() == screen.getCurrentCategory() && this.research.isVisible(ability))
		{
			int index = (int) (MinecraftClient.getInstance().world.getTime() / (20F) % stacks.length);
			int scale = (int)MinecraftClient.getInstance().getWindow().getScaleFactor();
			int modX = 8 * scale;
			int modY = 12 * scale;
			int scissorX = screen.getX() * scale + modX;
			int scissorY = MinecraftClient.getInstance().getWindow().getHeight() - (screen.getY() * scale + screen.getBackgroundHeight() * scale) + modY;
			int scissorWidth = screen.getBackgroundWidth() * scale - modX * 2 - scale * 3;
			int scissorHeight = screen.getBackgroundHeight() * scale - modY - scale * 9;
			RenderSystem.enableScissor(scissorX, scissorY, scissorWidth, scissorHeight);
			matrices.scale(screen.scale, screen.scale, 1F);
			int x = (int)(this.x + screen.getMapX() / screen.scale);
			int y = (int)(this.y + screen.getMapY() / screen.scale);
			RenderSystem.setShaderTexture(0, ICONS);
			DrawableHelper.drawTexture(matrices, x, y, 0, 0, 16, 16, 256, 256);
			matrices.translate(0, 0, -1F);
			this.research.getRequirements().forEach((requirement) -> {
				drawArrow(matrices, requirement.getX() + (int)(screen.getMapX() / screen.scale), requirement.getY() + (int)(screen.getMapY() / screen.scale), x, y);
			});
			matrices.translate(0, 0, 1F);
			RenderSystem.getModelViewStack().push();
			RenderSystem.getModelViewStack().scale(screen.scale, screen.scale, 1F);
			MinecraftClient.getInstance().getItemRenderer().renderInGui(stacks[index], x, y);
			RenderSystem.getModelViewStack().pop();
			RenderSystem.applyModelViewMatrix();
			if(ability.hasResearch(this.research))
			{
				Screen.fill(matrices, x, y, x + 16, y + 16, -2130706433);
			}
			else if(research.isAvailable(ability))
			{
				if(isMouseOver(mouseX, mouseY))
				{
					Screen.fill(matrices, x, y, x + 16, y + 16, -2130706433);
				}
				else
				{
					int shift = (int)(Math.sin((MinecraftClient.getInstance().world.getTime() + delta) / 10F) * 64 + 64);
					Color color = new Color(255, 255, 255, shift);
					Screen.fill(matrices, x, y, x + 16, y + 16, color.hashCode());
				}
			}
			else
			{
				matrices.translate(0, 0, 199F);
				Screen.fill(matrices, x, y, x + 16, y + 16, -1072689136);
				matrices.translate(0, 0, -199F);
			}
			matrices.scale(1F / screen.scale, 1F / screen.scale, 1F);
			RenderSystem.disableScissor();
		}
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		if(research.getCategory() == screen.getCurrentCategory())
		{
			if(ability.hasResearch(this.research) || research.isReadable(ability))
			{
				return readableTooltip;
			}
			else
			{
				return hiddenTooltip;
			}
		}
		return Collections.emptyList();
	}

	private Text generateSecretText(Research research)
	{
		Random random = new Random(research.getName().getString().hashCode());
		int length = random.nextInt(research.getName().getString().length()) / 2;
		LiteralText text = new LiteralText(research.getName().getString().substring(0, length));
		return text.setStyle(STYLE);
	}

	protected void drawArrow(MatrixStack matrices, int startX, int startY, int endX, int endY)
	{
		int horizontal = (endX - startX) / 16;
		int vertical = (endY - startY) / 16;
		boolean positiveHorizontal = horizontal > 0;
		if(horizontal > 0)
		{
			DrawableHelper.drawTexture(matrices, endX - 16, endY, 16 * 9, -0.001F, 16, 16, 256, 256);
		}
		else if(horizontal < 0)
		{
			DrawableHelper.drawTexture(matrices, endX + 16, endY, 16 * 8, -0.001F, 16, 16, 256, 256);
		}
		else
		{
			if(vertical > 0)
			{
				DrawableHelper.drawTexture(matrices, endX, endY - 16, 16 * 11, -0.001F, 16, 16, 256, 256);
			}
			else
			{
				DrawableHelper.drawTexture(matrices, endX, endY + 16, 16 * 10, -0.001F, 16, 16, 256, 256);
			}
		}
		if(horizontal > 0)
		{
			for(; horizontal > 0; horizontal--)
			{
				DrawableHelper.drawTexture(matrices, startX + horizontal * 16, startY + vertical * 16, 16, -0.001F, 16, 16, 256, 256);
			}
		}
		else
		{
			for(; horizontal < 0; horizontal++)
			{
				DrawableHelper.drawTexture(matrices, startX + horizontal * 16, startY + vertical * 16, 16, -0.001F, 16, 16, 256, 256);
			}
		}
		if(vertical > 0)
		{
			if(!positiveHorizontal)
			{
				DrawableHelper.drawTexture(matrices, startX, startY + vertical * 16, 112, -0.001F, 16, 16, 256, 256);
			}
			else
			{
				DrawableHelper.drawTexture(matrices, startX, startY + vertical * 16, 16 * 6, -0.001F, 16, 16, 256, 256);
			}
			vertical--;
			for(; vertical > 0; vertical--)
			{
				DrawableHelper.drawTexture(matrices, startX, startY + vertical * 16, 32, -0.001F, 16, 16, 256, 256);
			}
		}
		else
		{
			if(!positiveHorizontal)
			{
				DrawableHelper.drawTexture(matrices, startX, startY + vertical * 16, 16 * 5, -0.001F, 16, 16, 256, 256);
			}
			else
			{
				DrawableHelper.drawTexture(matrices, startX, startY + vertical * 16, 16 * 4, -0.001F, 16, 16, 256, 256);
			}
			vertical++;
			for(; vertical < 0; vertical++)
			{
				DrawableHelper.drawTexture(matrices, startX, startY + vertical * 16, 32, -0.001F, 16, 16, 256, 256);
			}
		}
	}
}
