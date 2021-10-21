package net.watersfall.thuwumcraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.client.gui.element.ScrollElement;
import net.watersfall.thuwumcraft.gui.ThaumatoriumHandler;
import net.watersfall.thuwumcraft.recipe.CrucibleRecipe;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRecipes;

import java.util.ArrayList;
import java.util.List;

public class ThaumatoriumScreen extends HandledScreen<ThaumatoriumHandler>
{
	private static final Identifier BACKGROUND_TEXTURE = Thuwumcraft.getId("textures/gui/container/thaumatorium.png");
	private boolean scrolling = false;

	private ScrollElement scroll = null;

	public ThaumatoriumScreen(ThaumatoriumHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
	}

	@Override
	protected void init()
	{
		this.backgroundWidth = 176;
		this.backgroundHeight = 180;
		this.playerInventoryTitleY = 87;
		super.init();
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		super.render(matrices, mouseX, mouseY, delta);
		ItemStack input = this.handler.getSlot(0).getStack();
		List<ItemStack> list = new ArrayList<>();
		client.world.getRecipeManager().listAllOfType(ThuwumcraftRecipes.CRUCIBLE).forEach(recipe -> {
			if(recipe.catalyst.test(input))
			{
				list.add(recipe.getOutput());
			}
		});
		if(scroll == null && list.size() > 0)
		{
			scroll = new ScrollElement(x(75), y(13), y(84) - 7, list.size() * 18);
			this.addDrawableChild(scroll);
		}
		else if(list.size() == 0)
		{
			this.remove(scroll);
			scroll = null;
		}
		RenderSystem.enableScissor(scale(x(39)), client.getWindow().getHeight() - scale(y(83)), scale(34), scale(69));
		for(int i = 0; i < list.size(); i++)
		{
			int scrollAmount = scroll == null ? 0 : scroll.y();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
			if(mouseX > x(39) && mouseX < x(39) + 34 && mouseY > y(14) + i * 18 + scrollAmount && mouseY < y(14) + i * 18 + 18 + scrollAmount)
			{
				this.drawTexture(matrices, x(39), y(14) + i * 18 + scrollAmount, 176, 18, 34, 18);
			}
			else
			{
				this.drawTexture(matrices, x(39), y(14) + i * 18 + scrollAmount, 176, 0, 34, 18);
			}
			client.getItemRenderer().renderInGui(list.get(i), x(39) + 9, y(14) + 1 + i * 18 + scrollAmount);
		}
		RenderSystem.disableScissor();
		int i = 0;
		for(AspectStack stack : this.handler.getEntity().getRequiredAspects().values())
		{
			client.getItemRenderer().renderInGui(new ItemStack(stack.getAspect().getItem(), stack.getCount()), x(81) + i * 18, y(68));
			client.getItemRenderer().renderGuiItemOverlay(textRenderer, new ItemStack(stack.getAspect().getItem(), stack.getCount()), x(81) + i * 18, y(68));
			i++;
		}
	}

	public int x(int x)
	{
		return this.x + x;
	}

	public int y(int y)
	{
		return this.y + y;
	}

	public int scale(int i)
	{
		return i * (int)client.getWindow().getScaleFactor();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(scroll != null && scroll.isMouseOver(mouseX, mouseY))
		{
			scrolling = true;
			return true;
		}
		if(mouseX > x(38) && mouseX < x(38) + 36 && mouseY > y(13) && mouseY < y(13) + 71)
		{
			ItemStack input = this.handler.getSlot(0).getStack();
			List<CrucibleRecipe> list = new ArrayList<>();
			client.world.getRecipeManager().listAllOfType(ThuwumcraftRecipes.CRUCIBLE).forEach(recipe -> {
				if(recipe.catalyst.test(input))
				{
					list.add(recipe);
				}
			});
			int scrollAmount = scroll == null ? 0 : scroll.y();
			for(int i = 0; i < list.size(); i++)
			{
				if(mouseX > x(39) && mouseX < x(39) + 34 && mouseY > y(14) + i * 18 + scrollAmount && mouseY < y(14) + i * 18 + 18 + scrollAmount)
				{
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeIdentifier(list.get(i).getId());
					ClientPlayNetworking.send(Thuwumcraft.getId("thaumatorium_click"), buf);
					return true;
				}
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button)
	{
		scrolling = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount)
	{
		if(scroll != null)
		{
			return scroll.mouseScrolled(mouseX, mouseY, amount);
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
	{
		if(scroll != null && scrolling)
		{
			return scroll.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
}
