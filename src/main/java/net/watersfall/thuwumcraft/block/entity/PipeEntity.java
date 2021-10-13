package net.watersfall.thuwumcraft.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.watersfall.thuwumcraft.api.aspect.AspectStack;
import net.watersfall.thuwumcraft.api.client.render.AspectRenderer;
import net.watersfall.thuwumcraft.api.lookup.AspectContainer;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = AspectRenderer.class)
public class PipeEntity extends BlockEntity implements AspectContainer, BlockEntityClientSerializable, AspectRenderer
{
	public AspectStack stack;

	public PipeEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.ASPECT_PIPE_ENTITY, pos, state);
		this.stack = AspectStack.EMPTY;
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
				if(!simulate)
				{
					this.stack.increment(stack.getCount());
					this.sync();
				}
				return AspectStack.EMPTY;
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

	@Override
	public int getSuction()
	{
		return 1;
	}

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		this.stack = new AspectStack(nbt.getCompound("aspect"));
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		nbt.put("aspect", this.stack.toNbt());
		return nbt;
	}

	@Override
	public void fromClientTag(NbtCompound tag)
	{
		this.stack = new AspectStack(tag.getCompound("aspect"));
	}

	@Override
	public NbtCompound toClientTag(NbtCompound nbt)
	{
		nbt.put("aspect", this.stack.toNbt());
		return nbt;
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
	public void setup(MatrixStack matrices, BlockHitResult hit)
	{
		AspectRenderer.super.setup(matrices, hit);
		Direction direction = hit.getSide();
		if(direction.getAxis() == Direction.Axis.Y)
		{
			matrices.translate(0, direction == Direction.UP ? -0.75 : -1.25, 0);
		}
		else
		{
			matrices.translate(direction.getOffsetX() / 3D, -1, direction.getOffsetZ() / 3D);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public boolean shouldRenderInEvent()
	{
		return true;
	}
}
