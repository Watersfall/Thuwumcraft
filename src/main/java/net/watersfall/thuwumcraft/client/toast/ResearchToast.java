package net.watersfall.thuwumcraft.client.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.watersfall.thuwumcraft.api.research.Research;

public class ResearchToast implements Toast
{
	private Research research;

	public ResearchToast(Research research)
	{
		this.research = research;
	}

	@Override
	public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime)
	{
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());
		manager.getClient().getItemRenderer().renderInGui(research.getStack().getMatchingStacks()[0], 8, 8);
		manager.getClient().textRenderer.draw(matrices, new TranslatableText("research.toast.research_complete").formatted(Formatting.DARK_PURPLE), 30F, 7F, -1);
		manager.getClient().textRenderer.draw(matrices, research.getName().copy().formatted(Formatting.GRAY), 30F, 18F, -1);
		if(startTime > 5000L)
		{
			return Visibility.HIDE;
		}
		return Visibility.SHOW;
	}
}
