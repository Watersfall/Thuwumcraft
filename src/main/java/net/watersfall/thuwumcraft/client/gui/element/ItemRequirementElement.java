package net.watersfall.thuwumcraft.client.gui.element;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.Thuwumcraft;

import java.util.List;

public class ItemRequirementElement extends ItemElement
{
	private static final Identifier ICONS = new Identifier(Thuwumcraft.MOD_ID, "textures/gui/research/research_icons.png");
	private static final TranslatableText CONSUMED_TEXT = new TranslatableText("consumed");

	private final boolean consumed;
	private boolean added;
	private Item[] items;

	public ItemRequirementElement(ItemStack[] stacks, int x, int y, boolean consumed)
	{
		super(stacks, x, y);
		this.consumed = consumed;
		items = new Item[stacks.length];
		for(int i = 0; i < items.length; i++)
		{
			this.items[i] = stacks[i].getItem();
		}
		added = false;
	}

	@Override
	public List<Text> getTooltip(int mouseX, int mouseY)
	{
		List<Text> list = super.getTooltip(mouseX, mouseY);
		if(consumed && !added)
		{
			((MutableText)list.get(0)).append(CONSUMED_TEXT);
			this.added = true;
		}
		return list;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		super.render(matrices, mouseX, mouseY, delta);
		RenderSystem.setShaderTexture(0, ICONS);
		matrices.translate(0, 0, 200F);
		if(MinecraftClient.getInstance().player.getInventory().containsAny(Sets.newHashSet(items)))
		{
			DrawableHelper.drawTexture(matrices, this.x + 8, this.y + 8, 224 / 2, 0, 8, 8, 128, 128);
		}
		else
		{
			DrawableHelper.drawTexture(matrices, this.x + 8, this.y + 8, 240 / 2, 0, 8, 8, 128, 128);
		}
		matrices.translate(0, 0, -200F);
	}
}
