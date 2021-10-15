package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.watersfall.thuwumcraft.api.aspect.Aspect;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.api.aspect.Aspects;
import net.watersfall.thuwumcraft.api.client.render.AspectRenderer;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = AspectRenderer.class)
public class EssentiaRefineryBlockEntity extends BlockEntity implements AspectContainer, BlockEntityClientSerializable, AspectRenderer
{
	private AspectStack stack;
	private static final int MAX_COUNT = 64;

	public EssentiaRefineryBlockEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.ESSENTIA_REFINERY, pos, state);
		stack = AspectStack.EMPTY;
	}

	@Override
	public AspectStack insert(AspectStack stack, boolean simulate)
	{
		if(stack.isEmpty())
		{
			return stack;
		}
		if(!this.stack.isEmpty())
		{
			if(this.stack.getAspect() == stack.getAspect())
			{
				int increment = Math.min(MAX_COUNT - this.stack.getCount(), stack.getCount());
				if(!simulate)
				{
					this.stack.increment(increment);
					this.sync();
				}
				stack.decrement(increment);
				return stack;
			}
		}
		else
		{
			if(!simulate)
			{
				this.stack = stack;
				this.sync();
			}
			return AspectStack.EMPTY;
		}
		return stack;
	}

	@Override
	public AspectStack extract(AspectStack stack, boolean simulate)
	{
		if(stack.isEmpty() && this.stack.isEmpty())
		{
			return stack;
		}
		if(!this.stack.isEmpty())
		{
			if(this.stack.getAspect() == stack.getAspect() || stack.isEmpty())
			{
				int extract = Math.min(stack.getCount(), this.stack.getCount());
				if(!simulate)
				{
					this.stack.decrement(extract);
					this.sync();
				}
				return new AspectStack(this.stack.getAspect(), extract);
			}
		}
		return AspectStack.EMPTY;
	}

	public AspectStack getStack()
	{
		return this.stack;
	}

	@Override
	public int getSuction()
	{
		return 2;
	}

	@Override
	public void readNbt(NbtCompound nbt)
	{
		stack = new AspectStack(nbt.getCompound("aspect"));
		super.readNbt(nbt);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt)
	{
		nbt.put("aspect", stack.toNbt());
		return super.writeNbt(nbt);
	}

	@Override
	public void fromClientTag(NbtCompound tag)
	{
		this.stack = AspectStack.EMPTY;
		if(tag.contains("aspect"))
		{
			Aspect aspect = Aspects.getAspectById(Identifier.tryParse(tag.getString("aspect")));
			int count = tag.getInt("count");
			this.stack = new AspectStack(aspect, count);
		}
	}

	@Override
	public NbtCompound toClientTag(NbtCompound tag)
	{
		if(!this.stack.isEmpty())
		{
			tag.putString("aspect", this.stack.getAspect().getId().toString());
			tag.putInt("count", this.stack.getCount());
		}
		return tag;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void setup(MatrixStack matrices, BlockHitResult hit)
	{
		AspectRenderer.super.setup(matrices, hit);
		if(hit.getBlockPos().equals(this.getPos()))
		{
			Direction direction = hit.getSide();
			if(direction.getAxis() == Direction.Axis.Y)
			{
				matrices.translate(0, direction == Direction.UP ? -0.5 : -1.25, 0);
			}
			else
			{
				matrices.translate(direction.getOffsetX() / 2D, -1, direction.getOffsetZ() / 2D);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public Collection<AspectStack> getStacksForRender()
	{
		if(stack.isEmpty())
		{
			return Collections.emptyList();
		}
		return List.of(stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public boolean shouldRenderInEvent()
	{
		return true;
	}
}
