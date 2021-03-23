package net.watersfall.alchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.api.research.Research;
import net.watersfall.alchemy.client.gui.element.ItemElement;
import net.watersfall.alchemy.client.gui.element.RecipeElement;
import net.watersfall.alchemy.client.util.PageCounter;

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
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.parent.render(matrices, mouseX, mouseY, delta);
		client.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		matrices.push();
		matrices.translate(0, 0, 50F);
		drawTexture(matrices, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
		matrices.pop();
	}

	@Override
	protected void init()
	{
		super.init();
		this.x = (width - textureWidth) / 2;
		this.y = (height - textureHeight) / 2;
		int offsetX = x + (this.textureWidth / 2) - (100 / 2);
		int offsetY = y + (this.textureHeight / 4) - (60 / 2);
		for(int i = 0; i < recipeIds.length; i++)
		{
			ItemElement[] items = new ItemElement[recipes[i].getPreviewInputs().size() + 1];
			for(int o = 0; o < recipes[i].getPreviewInputs().size(); o++)
			{
				items[o] = new ItemElement(recipes[i].getPreviewInputs().get(o).getMatchingStacksClient(), offsetX + (o % 3) * 20, offsetY + (o / 3) * 20);
			}
			items[items.length - 1] = new ItemElement(new ItemStack[]{recipes[i].getOutput()}, offsetX + 84, offsetY + 20);
			recipeElements[i] = new RecipeElement(items);
			if(i % 2 == 0)
			{
				offsetY = y + (this.textureHeight / 4) * 3 - (60 / 2);
			}
			else
			{
				offsetY = y + (this.textureHeight / 4) - (60 / 2);
			}
		}
		this.addButton(new TexturedButtonWidget(this.x + 16, this.y + this.height - 24, 16, 16, 208, 0, 0, ICONS, (button -> page.decrement())));
		this.addButton(new TexturedButtonWidget(this.x + this.textureWidth - 32, this.y + this.height - 24, 16, 16, 192, 0, 0, ICONS, (button -> page.increment())));
		this.page = new PageCounter(0, 0, recipeElements.length / 2);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.drawBackground(matrices, delta, mouseX, mouseY);
		for(int i = page.getValue() * 2; i < this.recipeElements.length && i < page.getValue() * 2 + 2; i++)
		{
			this.recipeElements[i].render(matrices, mouseX, mouseY, delta);
			if(recipeElements[i].isMouseOver(mouseX, mouseY))
			{
				this.renderTooltip(matrices, this.recipeElements[i].getTooltip(mouseX, mouseY), mouseX, mouseY);
			}
		}
		matrices.push();
		matrices.translate(0, 0, 51F);
		super.render(matrices, mouseX, mouseY, delta);
		matrices.pop();
	}

	@Override
	public void onClose()
	{
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
}
