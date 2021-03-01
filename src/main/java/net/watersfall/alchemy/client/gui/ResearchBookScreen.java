package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.Ability;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.research.Research;

import java.util.Optional;

public class ResearchBookScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier TEXTURE = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_screen.png");
	private static final Identifier BACKGROUND = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_background.png");
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");
	private int researchBackgroundWidth;
	private int researchBackgroundHeight;
	private float mapX = 0;
	private float mapY = 0;

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
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
		PlayerResearchAbility ability = optional.get();
		this.drawBackground(matrices, delta, mouseX, mouseY);
		int xOffset = (int)getXOffset();
		int yOffset = (int)getYOffset();
		client.getTextureManager().bindTexture(ICONS);
		Research.REGISTRY.getAll().forEach(research -> {
			int x = research.getX() + xOffset;
			int y = research.getY() + yOffset;
			drawTexture(matrices, x, y, 0, 0, 16, 16, 256, 256);
			research.getRequirements().forEach((requirement -> {
				this.drawArrow(matrices, x, y, x, y);
			}));
		});
		Research.REGISTRY.getAll().forEach(research -> {
			int x = research.getX() + xOffset;
			int y = research.getY() + yOffset;
			this.itemRenderer.renderInGui(research.getStack(), x, y);
			if(ability.getResearch().contains(research))
			{
				fill(matrices, x, y, x + 16, y + 16, -2130706433);
			}
			if(mouseX > x && mouseX < x + 16)
			{
				if(mouseY > y && mouseY < y + 16)
				{
					fill(matrices, x, y, x + 16, y + 16, -2130706433);
				}
			}
		});
		client.getTextureManager().bindTexture(TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
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
		int xOffset = (int)getXOffset();
		int yOffset = (int)getYOffset();
		Research.REGISTRY.getAll().forEach(research -> {
			int x = xOffset + research.getX();
			int y = yOffset + research.getY();
			if(mouseX > x && mouseX < x + 16)
			{
				if(mouseY > y && mouseY < y + 16)
				{
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeIdentifier(research.getId());
					ClientPlayNetworking.send(AlchemyMod.getId("research_click"), buf);
				}
			}
		});
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
