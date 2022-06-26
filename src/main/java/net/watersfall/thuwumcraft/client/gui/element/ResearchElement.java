package net.watersfall.thuwumcraft.client.gui.element;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.client.gui.screen.ResearchBookScreen;
import net.watersfall.thuwumcraft.client.gui.screen.ResearchScreen;
import net.watersfall.thuwumcraft.client.util.RenderHelper;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSounds;

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
		super(research.getDisplayStack().getMatchingStacks(), research.getX(), research.getY());
		this.research = research;
		this.screen = screen;
		this.ability = screen.getAbility();
		this.readableTooltip = Lists.newArrayList(research.getName());
		this.hiddenTooltip = Lists.newArrayList(generateSecretText(research));
		this.stacks = research.getDisplayStack().getMatchingStacks();
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		float x = this.x * screen.scale + screen.getMapX();
		float y = this.y * screen.scale + screen.getMapY();
		return mouseX > x - 13 && mouseX < x + 13 * screen.scale && mouseY > y - 13 && mouseY < y + 13 * screen.scale;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(isMouseOver(mouseX, mouseY) && this.research.getCategory() == screen.getCurrentCategory())
		{
			if(research.isClickable(ability))
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
			matrices.push();
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
			RenderSystem.enableDepthTest();
			this.research.getPrerequisiteResearch().forEach((requirementId) -> {
				Research requirement = ThuwumcraftRegistry.RESEARCH.get(requirementId);
				drawArrow(matrices, requirement.getX() + (int)(screen.getMapX() / screen.scale), requirement.getY() + (int)(screen.getMapY() / screen.scale), x, y);
			});
			DiffuseLighting.disableGuiDepthLighting();
			if(!research.isClickable(ability))
			{
				DiffuseLighting.method_34742();
				RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 1.0F);
			}
			int vOffset = 26;
			if(ability.hasResearch(research))
			{
				vOffset = 0;
			}
			matrices.translate(0, 0, 1F);
			Research.Icon icon = research.getIcon();
			RenderSystem.setShaderTexture(0, icon.texture());
			DrawableHelper.drawTexture(matrices, x - 13, y - 13, icon.u(), icon.v() + vOffset, 26, 26, 256, 256);
			matrices.push();
			RenderSystem.getModelViewStack().push();
			RenderSystem.getModelViewStack().scale(screen.scale, screen.scale, 1F);
			RenderHelper.drawItemInGui(stacks[index], matrices, x - 8, y - 8);
			RenderSystem.getModelViewStack().pop();
			RenderSystem.applyModelViewMatrix();
			matrices.pop();
			matrices.scale(1F / screen.scale, 1F / screen.scale, 1F);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.disableScissor();
			matrices.pop();
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
		startY -= 13;
		int horizontal = (endX - startX) / 13;
		float vertical2 = (endY - startY - 13) / 13f;
		int vertical = (int)vertical2;
		if(horizontal > 0)
		{
			DrawableHelper.drawTexture(matrices, endX - 26, endY - 7, 13 * 3, 128, 13, 13, 256, 256);
		}
		else if(horizontal < 0)
		{
			DrawableHelper.drawTexture(matrices, endX + 13, endY - 7, 13 * 2, 128, 13, 13, 256, 256);
		}
		else if(vertical > 0)
		{
			DrawableHelper.drawTexture(matrices, endX - 7, endY - 26, 13 * 5, 128, 13, 13, 256, 256);
		}
		else if(vertical < 0)
		{
			DrawableHelper.drawTexture(matrices, endX - 7, endY + 13, 13 * 4, 128, 13, 13, 256, 256);
		}
		if(horizontal > 0)
		{
			horizontal--;
			for(; horizontal > -1; horizontal--)
			{
				drawLine(matrices, startX + (13 * horizontal), endY - 7, false);
			}
		}
		else
		{
			for(; horizontal < 0; horizontal++)
			{
				drawLine(matrices, startX + (13 * horizontal), endY - 7, false);
			}
		}
		if(vertical > 0)
		{
			for(; vertical > 0; vertical--)
			{
				drawLine(matrices, startX - 7, startY + (13 * vertical), true);
			}
		}
		else
		{
			vertical++;
			for(; vertical < 1; vertical++)
			{
				drawLine(matrices, startX - 7, startY + (13 * vertical), true);
			}
		}
	}

	private void drawLine(MatrixStack matrices, int x, int y, boolean vertical)
	{
		int u = vertical ? 13 : 0;
		int v = 128;
		DrawableHelper.drawTexture(matrices, x, y, u, v, 13, 13, 256, 256);
	}
}
