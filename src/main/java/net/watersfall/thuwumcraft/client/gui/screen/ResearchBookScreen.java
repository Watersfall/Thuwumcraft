package net.watersfall.thuwumcraft.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.abilities.entity.PlayerResearchAbility;
import net.watersfall.thuwumcraft.api.registry.ThuwumcraftRegistry;
import net.watersfall.thuwumcraft.api.research.ResearchCategory;
import net.watersfall.thuwumcraft.client.gui.element.CategoryTabElement;
import net.watersfall.thuwumcraft.client.gui.element.ResearchElement;
import net.watersfall.thuwumcraft.client.gui.element.TooltipElement;
import net.watersfall.thuwumcraft.registry.ThuwumcraftSounds;
import net.watersfall.wet.api.abilities.AbilityProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ResearchBookScreen extends HandledScreen<ScreenHandler>
{
	private static final Identifier BORDER = new Identifier(Thuwumcraft.MOD_ID, "textures/gui/research/research_screen.png");
	private static final Identifier BACKGROUND = new Identifier(Thuwumcraft.MOD_ID, "textures/gui/research/research_background.png");
	private static final Identifier ICONS = new Identifier(Thuwumcraft.MOD_ID, "textures/gui/research/research_icons.png");
	private PlayerResearchAbility ability;
	private float mapX;
	private float mapY;
	private ResearchCategory currentCategory;
	private CategoryTabElement[] categories;
	public float scale = 0.7F;
	public int bottomY = 0;

	PlayerEntity player;
	public ResearchBookScreen(ScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		player = inventory.player;
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
		ability = optional.get();
		this.currentCategory = ThuwumcraftRegistry.RESEARCH_CATEGORY.get(ability.getLastCategory());
		if(currentCategory == null)
		{
			this.currentCategory = ThuwumcraftRegistry.RESEARCH_CATEGORY.get(Thuwumcraft.getId("starter"));
		}
		if(this.currentCategory == null)
		{
			this.currentCategory = ThuwumcraftRegistry.RESEARCH_CATEGORY.values().stream().findFirst().get();
		}
	}

	@Override
	protected void init()
	{
		this.backgroundWidth = 432;
		this.backgroundHeight = 252;
		super.init();
		this.children().clear();
		float scale = (float)client.getWindow().getScaleFactor();
		this.mapX = ability.getX();
		this.mapY = ability.getY();
		this.scale = ability.getScale();
		int total = 0;
		List<ResearchCategory> tempCat = new ArrayList<>();
		for(ResearchCategory category : ThuwumcraftRegistry.RESEARCH_CATEGORY.values())
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
		ThuwumcraftRegistry.RESEARCH.values().forEach((research -> {
			this.addDrawableChild(new ResearchElement(this, research));
		}));
		for(int i = 0; i < this.categories.length; i++)
		{
			this.addDrawableChild(categories[i]);
		}
		this.bottomY = height * (int)scale - (backgroundHeight * (int)scale) - this.y * (int)scale;
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		matrices.push();
		super.renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		drawTexture(matrices, this.x + 6, this.y + 7, 0, 0, backgroundWidth - 13, backgroundHeight - 15, backgroundWidth - 13, backgroundHeight - 15);
		matrices.pop();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		AbilityProvider<Entity> provider = AbilityProvider.getProvider(player);
		Optional<PlayerResearchAbility> optional = provider.getAbility(PlayerResearchAbility.ID, PlayerResearchAbility.class);
		ability = optional.get();
		this.drawBackground(matrices, delta, mouseX, mouseY);
		this.children().forEach((child) -> {
			if(child instanceof Drawable)
			{
				((Drawable)child).render(matrices, mouseX, mouseY, delta);
			}
		});
		RenderSystem.setShaderTexture(0, BORDER);
		drawTexture(matrices, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
		this.children().forEach(child -> {
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
		ability.setX(mapX);
		ability.setY(mapY);
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
		ability.setScale(scale);
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
			client.player.playSound(ThuwumcraftSounds.BOOK_OPEN_SOUND, SoundCategory.PLAYERS, 1.0F, (float)Math.random() * 0.2F + 1.1F);
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

	public int getBackgroundWidth()
	{
		return backgroundWidth;
	}

	public int getBackgroundHeight()
	{
		return backgroundHeight;
	}

	@Override
	public void close()
	{
		super.close();
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeFloat(mapX);
		buf.writeFloat(mapY);
		buf.writeFloat(scale);
		buf.writeIdentifier(currentCategory.getId());
		ClientPlayNetworking.send(Thuwumcraft.getId("player_close_research_book"), buf);
	}
}
