package net.watersfall.thuwumcraft.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.client.recipe.BookRenderableRecipe;
import net.watersfall.thuwumcraft.api.client.registry.ThuwumcraftClientRegistry;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.client.gui.element.RecipeElement;
import net.watersfall.thuwumcraft.client.gui.element.RecipePage;
import net.watersfall.thuwumcraft.client.util.PageCounter;
import net.watersfall.thuwumcraft.research.ResearchImpl;

public class ResearchTab extends Screen
{
	private static final Identifier BACKGROUND_TEXTURE = Thuwumcraft.getId("textures/gui/research/research_tab.png");
	private static final Identifier ICONS = new Identifier("textures/gui/book.png");

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

	protected ResearchTab(ResearchImpl.RecipeGroup group, ResearchScreen parent)
	{
		super(new TranslatableText(""));
		this.recipeIds = group.recipes().toArray(new Identifier[0]);
		this.recipes = new Recipe[recipeIds.length];
		this.recipeElements = new RecipeElement[recipeIds.length];
		this.parent = parent;
		for(int i = 0; i < recipeIds.length; i++)
		{
			Recipe<?> recipe = MinecraftClient.getInstance().world.getRecipeManager().get(recipeIds[i]).get();
			recipes[i] = recipe;
		}
		this.requiresComplete = group.isResearchRequired();
		this.items = new ItemStack[recipes.length];
		for(int i = 0; i < items.length; i++)
		{
			items[i] = recipes[i].getOutput();
		}
	}

	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		matrices.push();
		matrices.translate(0, 0, 50F);
		drawTexture(matrices, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
		matrices.pop();
	}

	@Override
	protected void init()
	{
		parent.init(client, width, height);
		super.init();
		this.x = (width - textureWidth) / 2;
		this.y = (height - textureHeight) / 2;
		int offsetY = y + (this.textureHeight / 4) - (60 / 2);
		int offsetY2 = y + (this.textureHeight / 4) * 3 - (60 / 2);
		int count = 0;
		for(int i = 0; i < recipeIds.length; i++)
		{
			BookRenderableRecipe recipe = (BookRenderableRecipe)recipes[i];
			recipeElements[i] = ThuwumcraftClientRegistry.RECIPE_TAB_TYPE.get(recipe.getBookType()).generateRecipeLayout(recipes[i], x, offsetY, textureWidth, textureHeight);
			count++;
		}
		this.addDrawableChild(new PageTurnWidget(this.x + 16, this.y + this.textureHeight - 24, false, button -> page.decrement(), true));
		this.addDrawableChild(new PageTurnWidget(this.x + this.textureWidth - 24 - 23, this.y + this.textureHeight - 24, true, button -> page.increment(), true));
		this.recipePages = new RecipePage[count];
		count = 0;
		for(int i = 0; i < recipeElements.length; i++)
		{
			recipePages[count++] = new RecipePage(recipeElements[i]);
		}
		this.page = new PageCounter(0, 0, recipePages.length - 1);
		this.parent.init();
		researchButtonOriginalState = this.parent.getResearchButton().isEnabled();
		this.parent.getResearchButton().disable();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		matrices.push();
		matrices.translate(0, 0, -50);
		this.parent.render(matrices, mouseX, mouseY, delta);
		matrices.pop();
		matrices.push();
		matrices.translate(0, 0, 100);
		this.drawBackground(matrices, delta, mouseX, mouseY);
		this.recipePages[page.getValue()].render(matrices, mouseX, mouseY, delta);
		if(recipePages[page.getValue()].isMouseOver(mouseX, mouseY))
		{
			this.renderTooltip(matrices, this.recipePages[page.getValue()].getTooltip(mouseX, mouseY), mouseX, mouseY);
		}
		((ButtonWidget)(this.children().get(0))).visible = true;
		((ButtonWidget)(this.children().get(1))).visible = true;
		if(page.getValue() <= page.getMin())
		{
			((ButtonWidget)(this.children().get(0))).visible = false;
		}
		if(page.getValue() >= page.getMax())
		{
			((ButtonWidget)(this.children().get(1))).visible = false;
		}
		matrices.pop();
	}

	@Override
	public void close()
	{
		this.parent.childOpen = false;
		if(researchButtonOriginalState)
			this.parent.getResearchButton().enable();
		this.client.setScreen(parent);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		super.mouseClicked(mouseX, mouseY, button);
		return this.parent.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean shouldPause()
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
