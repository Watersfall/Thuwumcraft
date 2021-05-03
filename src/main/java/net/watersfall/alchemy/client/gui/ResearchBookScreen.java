package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.abilities.AbilityProvider;
import net.watersfall.alchemy.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.alchemy.api.research.Research;
import net.watersfall.alchemy.api.research.ResearchCategory;
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.client.gui.element.CategoryTabElement;
import net.watersfall.alchemy.client.gui.element.ResearchElement;
import net.watersfall.alchemy.client.gui.element.TooltipElement;

import java.util.*;

public class ResearchBookScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier BORDER = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_screen.png");
	private static final Identifier BACKGROUND = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_background.png");
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");
	private final int textureWidth = 431;
	private final int textureHeight = 256;
	private final PlayerResearchAbility ability;
	private float mapX;
	private float mapY;
	private ResearchCategory currentCategory;
	private CategoryTabElement[] categories;
	public float scale = 0.7F;
	public int bottomY = 0;
	private int x, y;

	PlayerEntity player;
	public ResearchBookScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		player = inventory.player;
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
		ability = optional.get();
		this.currentCategory = ResearchCategory.REGISTRY.getFirst();
	}

	@Override
	public void init(MinecraftClient client, int width, int height)
	{
		super.init(client, width, height);
		int scale = (int)client.getWindow().getScaleFactor();
		this.bottomY = height * scale - (textureHeight * scale) - this.y * scale;
	}

	@Override
	protected void init()
	{
		this.backgroundWidth = textureWidth;
		this.backgroundHeight = textureHeight;
		super.init();
		this.x = this.field_2776;
		this.y = this.field_2800;
		this.children.clear();
		this.mapX = this.textureWidth / (scale * 2F) - 8;
		this.mapY = this.textureHeight / (scale * 2F) - 8;
		int total = 0;
		List<ResearchCategory> tempCat = new ArrayList<>();
		for(ResearchCategory category : ResearchCategory.REGISTRY.getAll())
		{
			if(category.isVisible(ability))
			{
				tempCat.add(category);
				total++;
			}
		}
		tempCat.sort(Comparator.comparingInt(ResearchCategory::getIndex));
		categories = new CategoryTabElement[total];
		for(int i = 0; i < total; i++)
		{
			categories[i] = new CategoryTabElement(this, tempCat.get(i), x - 24, y + 12 +  i * 24, false);
		}
		Research.REGISTRY.getAll().forEach((research -> {
			this.addChild(new ResearchElement(this, research));
		}));
		for(int i = 0; i < this.categories.length; i++)
		{
			this.addChild(categories[i]);
		}
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		matrices.push();
		matrices.translate(0, 0, -2F);
		super.renderBackground(matrices);
		matrices.pop();
		matrices.push();
		matrices.translate(0, 0, -1F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		drawTexture(matrices, this.x + 12, this.y + 12, 0, 0, backgroundWidth - 24, backgroundHeight - 24, backgroundWidth - 24, backgroundHeight - 24);
		matrices.translate(0, 0, 201F);
		RenderSystem.setShaderTexture(0, BORDER);
		drawTexture(matrices, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
		matrices.pop();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.drawBackground(matrices, delta, mouseX, mouseY);
		this.children.forEach((child) -> {
			if(child instanceof Drawable)
			{
				((Drawable)child).render(matrices, mouseX, mouseY, delta);
			}
		});
		this.children.forEach(child -> {
			if(child.isMouseOver(mouseX, mouseY) && child instanceof TooltipElement)
			{
				this.renderTooltip(matrices, ((TooltipElement)child).getTooltip(mouseX, mouseY), mouseX, mouseY);
			}
		});
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
	{
		mapX += (deltaX);
		mapY += (deltaY);
		mapX = MathHelper.clamp(mapX, 0, this.width / scale);
		mapY = MathHelper.clamp(mapY, 0, this.height / scale);
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount)
	{
		if(amount < 0)
		{
			scale = MathHelper.clamp(scale - 0.05F, 0.5F, 1F);
		}
		else
		{
			scale = MathHelper.clamp(scale + 0.05F, 0.5F, 1F);
		}
		return true;
	}

	public float getMapX()
	{
		return this.mapX;
	}

	public float getMapY()
	{
		return this.mapY;
	}

	public PlayerResearchAbility getAbility()
	{
		return ability;
	}

	public ResearchCategory getCurrentCategory()
	{
		return this.currentCategory;
	}

	public void setCurrentCategory(ResearchCategory category)
	{
		if(category != currentCategory)
		{
			this.currentCategory = category;
			client.player.playSound(AlchemySounds.BOOK_OPEN_SOUND, SoundCategory.PLAYERS, 1.0F, (float)Math.random() * 0.2F + 1.1F);
		}
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}
