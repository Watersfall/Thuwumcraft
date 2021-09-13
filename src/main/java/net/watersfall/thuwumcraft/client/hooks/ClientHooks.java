package net.watersfall.thuwumcraft.client.hooks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3f;
import net.watersfall.thuwumcraft.api.client.item.CustomTooltipDataComponent;
import net.watersfall.thuwumcraft.api.client.item.MultiTooltipComponent;
import net.watersfall.thuwumcraft.client.ThuwumcraftClient;
import net.watersfall.thuwumcraft.client.util.ScreenHelper;
import net.watersfall.thuwumcraft.registry.ThuwumcraftStatusEffects;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ClientHooks
{
	private static final ItemStack STACK = new ItemStack(Items.SHIELD, 1);

	public static void addTooltipsToCreativeScreen(Screen screen, ItemStack stack, MatrixStack matrices, int x, int y, List<Text> list, CallbackInfo info)
	{
		if(MultiTooltipComponent.REGISTRY.get(stack.getItem()) != null)
		{
			ScreenHelper.renderTooltip(screen, matrices, list, stack.getTooltipData(), stack, x, y);
			info.cancel();
		}
	}

	public static void renderLivingEntity(LivingEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider)
	{
		if(entity.hasStatusEffect(ThuwumcraftStatusEffects.PROJECTILE_SHIELD))
		{
			matrices.push();
			matrices.translate(-1.5f, 1f, -0.4f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(STACK, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider, 0);
			matrices.pop();
			matrices.push();
			matrices.translate(1.5f, 1f, 0.4f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(STACK, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider, 0);
			matrices.pop();
			matrices.push();
			matrices.translate(0.4f, 1f, -1.5f);
			matrices.scale(3f, 3f, 3f);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180f));
			MinecraftClient.getInstance().getItemRenderer().renderItem(STACK, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider, 0);
			matrices.pop();
			matrices.push();
			matrices.translate(-0.4f, 1f, 1.5f);
			matrices.scale(3f, 3f, 3f);
			MinecraftClient.getInstance().getItemRenderer().renderItem(STACK, ModelTransformation.Mode.GROUND, 15728640, 655360, matrices, vertexConsumerProvider, 0);
			matrices.pop();
		}
	}

	public static void onSetRecipes()
	{
		MultiTooltipComponent.REGISTRY.reload();
		ThuwumcraftClient.registerAspectTooltips();
	}

	public static void screenCreateItemTooltipData(TooltipData data, List<TooltipComponent> list, CallbackInfo info)
	{
		if (data instanceof CustomTooltipDataComponent)
		{
			list.add(((CustomTooltipDataComponent)data).getComponent());
			info.cancel();
		}
	}

	public static void screenRenderTooltip(ItemStack stack, Screen screen, List<Text> list, MatrixStack matrices, int x, int y, CallbackInfo info)
	{
		if(MultiTooltipComponent.REGISTRY.get(stack.getItem()) != null)
		{
			ScreenHelper.renderTooltip(screen, matrices, list, stack.getTooltipData(), stack, x, y);
			info.cancel();
		}
	}

	public static Optional<List<TooltipComponent>> getTooltipComponents(ItemStack stack)
	{
		if(MultiTooltipComponent.REGISTRY.get(stack.getItem()) != null)
		{
			List<Function<ItemStack, TooltipComponent>> list = MultiTooltipComponent.REGISTRY.get(stack.getItem());
			List<TooltipComponent> list2 = new ArrayList<>();
			for(int i = 0; i < list.size(); i++)
			{
				if(list.get(i) != null)
				{
					TooltipComponent component = list.get(i).apply(stack);
					if(component != null)
					{
						list2.add(list.get(i).apply(stack));
					}
				}
			}
			return Optional.of(list2);
		}
		return Optional.empty();
	}
}
