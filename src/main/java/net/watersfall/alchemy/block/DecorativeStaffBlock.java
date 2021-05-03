package net.watersfall.alchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.watersfall.alchemy.api.aspect.Aspect;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DecorativeStaffBlock extends TallPlantBlock
{
	public static final VoxelShape OUTLINE_SHAPE = Block.createCuboidShape(7, 0, 7, 9, 16, 9);
	private final Aspect aspect;
	private String translationKey;

	public DecorativeStaffBlock(Aspect aspect, Settings settings)
	{
		super(settings);
		this.aspect = aspect;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		return true;
	}

	@Override
	public OffsetType getOffsetType()
	{
		return OffsetType.NONE;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return OUTLINE_SHAPE;
	}

	public Aspect getAspect()
	{
		return this.aspect;
	}

	@Override
	public String getTranslationKey()
	{
		if(this.translationKey == null)
		{
			this.translationKey = "block." + this.aspect.getId().getNamespace() + ".staff.decorative";
		}
		return this.translationKey;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options)
	{
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText(this.aspect.getTranslationKey()).setStyle(Style.EMPTY.withColor(aspect.getColor())));
	}
}
