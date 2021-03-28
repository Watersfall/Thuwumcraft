package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.client.gui.RecipeTabType;
import net.watersfall.alchemy.api.item.AspectItem;
import net.watersfall.alchemy.api.research.Research;
import net.watersfall.alchemy.client.gui.element.ItemElement;
import net.watersfall.alchemy.client.gui.element.RecipeElement;
import net.watersfall.alchemy.client.gui.element.RecipePage;
import net.watersfall.alchemy.client.util.PageCounter;
import net.watersfall.alchemy.recipe.AlchemyRecipes;
import net.watersfall.alchemy.recipe.PedestalRecipe;

import java.awt.*;

public class ResearchTab extends Screen
{
	private static final Identifier BACKGROUND_TEXTURE = AlchemyMod.getId("textures/gui/research/research_tab.png");
	private static final Identifier ICONS = new Identifier(AlchemyMod.MOD_ID, "textures/gui/research/research_icons.png");

	private int x;
	private int y;
	private final Identifier[] recipeIds;
	public final Recipe<?>[] recipes;
	private final ResearchScreen parent;
	private final RecipeElement[] recipeElements;
	private final int textureWidth = 384 / 2;
	private final int textureHeight = 256;
	private PageCounter page;
	private final boolean requiresComplete;
	private final ItemStack[] items;
	private RecipePage[] recipePages;
	private boolean researchButtonOriginalState;

	protected ResearchTab(Research.RecipeGroup group, ResearchScreen parent)
	{
		super(new TranslatableText(""));
		this.recipeIds = group.getRecipes();
		this.recipes = new Recipe[recipeIds.length];
		this.recipeElements = new RecipeElement[recipeIds.length];
		this.parent = parent;
		for(int i = 0; i < recipeIds.length; i++)
		{
			Recipe<?> recipe = MinecraftClient.getInstance().world.getRecipeManager().get(recipeIds[i]).get();
			recipes[i] = recipe;
		}
		this.requiresComplete = group.requiresComplete();
		this.items = new ItemStack[recipes.length];
		for(int i = 0; i < items.length; i++)
		{
			items[i] = recipes[i].getOutput();
		}
	}

	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		this.parent.render(matrices, mouseX, mouseY, delta);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		matrices.push();
		matrices.translate(0, 0, 50F);
		drawTexture(matrices, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
		matrices.pop();
	}

	@Override
	public void init(MinecraftClient client, int width, int height)
	{
		super.init(client, width, height);
		parent.init(client, width, height);
	}

	@Override
	protected void init()
	{
		super.init();
		this.x = (width - textureWidth) / 2;
		this.y = (height - textureHeight) / 2;
		int offsetY = y + (this.textureHeight / 4) - (60 / 2);
		int offsetY2 = y + (this.textureHeight / 4) * 3 - (60 / 2);
		int count = 0;
		for(int i = 0; i < recipeIds.length; i++)
		{
			recipeElements[i] = RecipeTabType.REGISTRY.get(recipes[i].getType()).generateRecipeLayout(recipes[i], x, offsetY, textureWidth, textureHeight);
			if(recipeElements[i].twoPage && i + 1 < recipes.length)
			{
				i++;
				recipeElements[i] = RecipeTabType.REGISTRY.get(recipes[i].getType()).generateRecipeLayout(recipes[i], x, offsetY, textureWidth, textureHeight);
				if(!recipeElements[i].twoPage)
				{
					count++;
				}
				else
				{
					recipeElements[i] = RecipeTabType.REGISTRY.get(recipes[i].getType()).generateRecipeLayout(recipes[i], x, offsetY2, textureWidth, textureHeight);
				}
			}
			count++;
		}
		this.addButton(new TexturedButtonWidget(this.x + 16, this.y + this.height - 24, 16, 16, 208, 0, 0, ICONS, (button -> page.decrement())));
		this.addButton(new TexturedButtonWidget(this.x + this.textureWidth - 32, this.y + this.height - 24, 16, 16, 192, 0, 0, ICONS, (button -> page.increment())));
		this.recipePages = new RecipePage[count];
		count = 0;
		for(int i = 0; i < recipeElements.length; i++)
		{
			if(recipeElements[i].twoPage && i + 1 < recipeElements.length && recipeElements[i + 1].twoPage)
			{
				recipePages[count++] = new RecipePage(recipeElements[i], recipeElements[++i]);
			}
			else
			{
				recipePages[count++] = new RecipePage(recipeElements[i]);
			}
		}
		this.page = new PageCounter(0, 0, recipePages.length - 1);
		this.parent.init();
		researchButtonOriginalState = this.parent.getResearchButton().isEnabled();
		this.parent.getResearchButton().disable();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.drawBackground(matrices, delta, mouseX, mouseY);
		this.recipePages[page.getValue()].render(matrices, mouseX, mouseY, delta);
		if(recipePages[page.getValue()].isMouseOver(mouseX, mouseY))
		{
			this.renderTooltip(matrices, this.recipePages[page.getValue()].getTooltip(mouseX, mouseY), mouseX, mouseY);
		}
		matrices.push();
		matrices.translate(0, 0, 51F);
		super.render(matrices, mouseX, mouseY, delta);
		matrices.pop();
	}

	@Override
	public void onClose()
	{
		this.parent.childOpen = false;
		if(researchButtonOriginalState)
			this.parent.getResearchButton().enable();
		this.client.openScreen(parent);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		super.mouseClicked(mouseX, mouseY, button);
		return this.parent.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	public boolean requiresComplete()
	{
		return this.requiresComplete;
	}

	public ItemStack[] getItems()
	{
		return this.items;
	}

	public Research getResearch()
	{
		return this.parent.getResearch();
	}

	public ResearchScreen getParent()
	{
		return this.parent;
	}
}
