package net.watersfall.alchemy.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
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
import net.watersfall.alchemy.api.sound.AlchemySounds;
import net.watersfall.alchemy.client.gui.element.CategoryTabElement;
import net.watersfall.alchemy.client.gui.element.ResearchElement;
import net.watersfall.alchemy.client.gui.element.TooltipElement;

import java.util.*;
import java.util.List;

public class ResearchBookScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier BORDER = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_screen.png");
	private static final Identifier BACKGROUND = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_background.png");
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");
	private final PlayerResearchAbility ability;
	private float mapX;
	private float mapY;
	private ResearchCategory currentCategory;
	private CategoryTabElement[] categories;

	PlayerEntity player;
	public ResearchBookScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		player = inventory.player;
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
		ability = optional.get();
		this.currentCategory = ResearchCategory.REGISTRY.getFirst();
		categories = new CategoryTabElement[ResearchCategory.REGISTRY.getAll().size()];
		int i = 0;
		for(ResearchCategory category : ResearchCategory.REGISTRY.getAll())
		{
			categories[i] = new CategoryTabElement(this, category, x + 1, y + 24 +  i * 24, false);
			i++;
		}
	}

	@Override
	protected void init()
	{
		super.init();
		this.children.clear();
		this.mapX = this.width / 2F - 8;
		this.mapY = this.height / 2F - 8;
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
		matrices.translate(0, 0, -1F);
		MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND);
		drawTexture(matrices, 24, 12, 0, 0, width - 48, height - 24, width - 48, height - 24);
		matrices.translate(0, 0, 201F);
		MinecraftClient.getInstance().getTextureManager().bindTexture(BORDER);
		drawTexture(matrices, 0, 0, 0, 0, width, height, width, height);
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
		mapX = MathHelper.clamp(mapX, 0, this.width);
		mapY = MathHelper.clamp(mapY, 0, this.height);
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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
}
