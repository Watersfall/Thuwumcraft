package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArcaneLampEntity extends BlockEntity
{
	private final List<BlockPos> lights;

	public ArcaneLampEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.ARCANE_LAMP_ENTITY, pos, state);
		this.lights = new ArrayList<>();
	}

	public void addLight(BlockPos pos)
	{
		lights.add(pos);
	}

	public List<BlockPos> getLights()
	{
		return lights;
	}

	@Override
	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		if(tag.contains("lights"))
		{
			Arrays.stream(tag.getLongArray("lights")).forEach(value -> {
				lights.add(BlockPos.fromLong(value));
			});
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		tag.put("lights", new NbtLongArray(lights.stream().mapToLong(BlockPos::asLong).toArray()));
		return tag;
	}
}
