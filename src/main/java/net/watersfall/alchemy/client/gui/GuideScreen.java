package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.research.Research;

public class GuideScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_screen.png");
	private static final Identifier BACKGROUND = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_background.png");
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");
	private int researchBackgroundWidth;
	private int researchBackgroundHeight;
	private float mapX = 0;
	private float mapY = 0;

	PlayerEntity player;
	public GuideScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
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
		this.drawBackground(matrices, delta, mouseX, mouseY);
		int x = (int)getXOffset();
		int y = (int)getYOffset();
		client.getTextureManager().bindTexture(ICONS);
		int finalX = x;
		int finalY = y;
		Research.REGISTRY.getAll().forEach(research -> {
			drawTexture(matrices, research.getX() + finalX, research.getY() + finalY, 0, 0, 16, 16, 256, 256);
			research.getRequirements().forEach((requirement -> {
				this.drawArrow(matrices, research.getX() + finalX, research.getY() + finalY, requirement.getX() + finalX, requirement.getY() + finalY);
			}));
		});
		Research.REGISTRY.getAll().forEach(research -> {
			this.itemRenderer.renderInGui(research.getStack(), research.getX() + finalX, research.getY() + finalY);
		});
		client.getTextureManager().bindTexture(TEXTURE);
		x = (width - backgroundWidth) / 2;
		y = (height - backgroundHeight) / 2;
		matrices.push();
		matrices.translate(0, 0, 1000F);
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
		matrices.pop();
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

	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		return super.mouseClicked(mouseX, mouseY, button);
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
