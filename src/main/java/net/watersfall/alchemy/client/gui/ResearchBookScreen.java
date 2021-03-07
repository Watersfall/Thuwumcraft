package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.research.Research;
import net.watersfall.alchemy.api.research.ResearchCategory;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ResearchBookScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_screen.png");
	private static final Identifier BACKGROUND = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_background.png");
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");
	private static final Identifier FONT_ID = new Identifier("minecraft", "alt");
	private static final Style STYLE = Style.EMPTY.withFont(FONT_ID);;
	private int researchBackgroundWidth;
	private int researchBackgroundHeight;
	private float mapX = 0;
	private float mapY = 0;
	private ResearchCategory category = ResearchCategory.REGISTRY.getFirst();

	PlayerEntity player;
	public ResearchBookScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		player = inventory.player;
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - researchBackgroundWidth) / 2;
		int y = (height - researchBackgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, researchBackgroundWidth, researchBackgroundHeight);
	}

	@Override
	protected void init()
	{
		this.backgroundWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
		this.backgroundHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
		this.researchBackgroundWidth = backgroundWidth - 20;
		this.researchBackgroundHeight = backgroundHeight - 20;
		this.playerInventoryTitleX = -1000;
		super.init();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		matrices.push();
		client.getTextureManager().bindTexture(TEXTURE);
		int x1 = (width - backgroundWidth) / 2;
		int y1 = (height - backgroundHeight) / 2;
		matrices.translate(0, 0, 400F);
		drawTexture(matrices, x1, y1, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
		matrices.pop();
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
		PlayerResearchAbility ability = optional.get();
		this.drawBackground(matrices, delta, mouseX, mouseY);
		int xOffset = (int)getXOffset();
		int yOffset = (int)getYOffset();
		client.getTextureManager().bindTexture(ICONS);
		Research.REGISTRY.getAll().forEach(research -> {
			if(research.getCategory() == this.category)
			{
				int x = research.getX() + xOffset;
				int y = research.getY() + yOffset;
				if(research.isVisible(ability))
				{
					matrices.push();
					matrices.translate(0, 0, 1D);
					drawTexture(matrices, x, y, 0, 0, 16, 16, 256, 256);
					matrices.pop();
					research.getRequirements().forEach((requirement -> {
						int x2 = requirement.getX() + xOffset;
						int y2 = requirement.getY() + yOffset;
						this.drawArrow(matrices, x2, y2, x, y);
					}));
				}
			}
		});
		Iterator<ResearchCategory> categories = ResearchCategory.REGISTRY.getAll().iterator();
		matrices.push();
		matrices.translate(0, 0, 1000D);
		for(int i = 0; categories.hasNext(); i++)
		{
			ResearchCategory cat = categories.next();
			drawTexture(matrices, this.x, this.y + 24 +  i * 24, 0, 0, 16, 16, 256, 256);
		}
		matrices.pop();
		Research.REGISTRY.getAll().forEach(research -> {
			if(research.getCategory() == this.category && research.isVisible(ability))
			{
				int x = research.getX() + xOffset;
				int y = research.getY() + yOffset;
				this.itemRenderer.renderInGui(research.getStack(), x, y);
				matrices.push();
				if(research.isAvailable(ability))
				{
					matrices.translate(0, 0, 2D);
					if(!ability.getResearch().contains(research))
					{
						if(mouseX > x && mouseX < x + 16 && mouseY > y && mouseY < y + 16)
						{
							fill(matrices, x, y, x + 16, y + 16, -2130706433);
						}
						else
						{
							int shift = (int)(Math.sin((client.world.getTime() + delta) / 10F) * 64 + 64);
							Color color = new Color(255, 255, 255, shift);
							fill(matrices, x, y, x + 16, y + 16, color.hashCode());
						}
					}
					else
					{
						fill(matrices, x, y, x + 16, y + 16, -2130706433);
					}
				}
				else
				{
					matrices.translate(0, 0, 398);
					this.fillGradient(matrices, x, y, x + 16, y + 16, -1072689136, -804253680);
				}
				matrices.pop();
				if(mouseX > x && mouseX < x + 16)
				{
					if(mouseY > y && mouseY < y + 16)
					{
						if(research.isReadable(ability))
						{
							drawMouseoverTooltip(matrices, research.getName().asOrderedText(), mouseX, mouseY);
						}
						else
						{
							drawMouseoverTooltip(matrices, generateSecretText(research), mouseX, mouseY);
						}
					}
				}
			}
		});
		categories = ResearchCategory.REGISTRY.getAll().iterator();
		for(int i = 0; categories.hasNext(); i++)
		{
			ResearchCategory cat = categories.next();
			if(mouseX > x && mouseX < x + 16)
			{
				if(mouseY > y + 24 + i * 24 && mouseY < y + i * 24 + 40)
				{
					matrices.push();
					drawMouseoverTooltip(matrices, cat.getName().asOrderedText(), mouseX, mouseY);
					matrices.pop();
				}
			}
		}
	}

	private OrderedText generateSecretText(Research research)
	{
		Random random = new Random(research.getName().getString().hashCode());
		int length = random.nextInt(research.getName().getString().length()) / 2;
		LiteralText text = new LiteralText(research.getName().getString().substring(0, length));
		return text.setStyle(STYLE).asOrderedText();
	}

	protected void drawMouseoverTooltip(MatrixStack matrices, OrderedText text, int x, int y)
	{
		List<OrderedText> list = new ArrayList<>();
		list.add(text);
		this.renderOrderedTooltip(matrices, list, x, y);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
	{
		mapX += (deltaX * 2);
		mapY -= (deltaY * 2);
		mapX = MathHelper.clamp(mapX, -512, 512);
		mapY = MathHelper.clamp(mapY, -512, 512);
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	protected void drawArrow(MatrixStack matrices, int startX, int startY, int endX, int endY)
	{
		int horizontal = (endX - startX) / 16;
		int vertical = (endY - startY) / 16;
		boolean positiveHorizontal = horizontal > 0;
		if(horizontal > 0)
		{
			drawTexture(matrices, endX - 16, endY, 16 * 9, 0, 16, 16);
		}
		else if(horizontal < 0)
		{
			drawTexture(matrices, endX + 16, endY, 16 * 8, 0, 16, 16);
		}
		else
		{
			if(vertical > 0)
			{
				drawTexture(matrices, endX, endY - 16, 16 * 11, 0, 16, 16);
			}
			else
			{
				drawTexture(matrices, endX, endY + 16, 16 * 10, 0, 16, 16);
			}
		}
		if(horizontal > 0)
		{
			for(; horizontal > 0; horizontal--)
			{
				drawTexture(matrices, startX + horizontal * 16, startY + vertical * 16, 16, 0, 16, 16);
			}
		}
		else
		{
			for(; horizontal < 0; horizontal++)
			{
				drawTexture(matrices, startX + horizontal * 16, startY + vertical * 16, 16, 0, 16, 16);
			}
		}
		if(vertical > 0)
		{
			if(!positiveHorizontal)
			{
				drawTexture(matrices, startX, startY + vertical * 16, 112, 0, 16, 16);
			}
			else
			{
				drawTexture(matrices, startX, startY + vertical * 16, 16 * 6, 0, 16, 16);
			}
			vertical--;
			for(; vertical > 0; vertical--)
			{
				drawTexture(matrices, startX, startY + vertical * 16, 32, 0, 16, 16);
			}
		}
		else
		{
			if(!positiveHorizontal)
			{
				drawTexture(matrices, startX, startY + vertical * 16, 16 * 5, 0, 16, 16);
			}
			else
			{
				drawTexture(matrices, startX, startY + vertical * 16, 16 * 4, 0, 16, 16);
			}
			vertical++;
			for(; vertical < 0; vertical++)
			{
				drawTexture(matrices, startX, startY + vertical * 16, 32, 0, 16, 16);
			}
		}
		int test = 0;
		test++;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button)
	{
		if(button == 0)
		{
			AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
			Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
			PlayerResearchAbility ability = optional.get();
			int xOffset = (int)getXOffset();
			int yOffset = (int)getYOffset();
			Research.REGISTRY.getAll().forEach(research -> {
				if(research.getCategory() == this.category && research.isAvailable(ability))
				{
					int x = xOffset + research.getX();
					int y = yOffset + research.getY();
					if(mouseX > x && mouseX < x + 16)
					{
						if(mouseY > y && mouseY < y + 16)
						{
							this.client.openScreen(new ResearchScreen(this, research));
						}
					}
				}
			});
			Iterator<ResearchCategory> categories = ResearchCategory.REGISTRY.getAll().iterator();
			for(int i = 0; categories.hasNext(); i++)
			{
				ResearchCategory cat = categories.next();
				if(mouseX > x && mouseX < x + 16)
				{
					if(mouseY > y + 24 + i * 24 && mouseY < y + i * 24 + 40)
					{
						this.category = cat;
						break;
					}
				}
			}
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	public float getXOffset()
	{
		return ((width / 2f)) + (mapX / 2f) - 8F;
	}

	public float getYOffset()
	{
		return (height / 2f) - (mapY / 2f) - 8F;
	}
}
