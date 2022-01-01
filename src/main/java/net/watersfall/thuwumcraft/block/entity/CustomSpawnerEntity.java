package net.watersfall.thuwumcraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.watersfall.thuwumcraft.registry.ThuwumcraftBlockEntities;
import net.watersfall.thuwumcraft.world.CustomMobSpawnerLogic;
import org.jetbrains.annotations.Nullable;

public class CustomSpawnerEntity extends BlockEntity
{
	private final CustomMobSpawnerLogic logic = new CustomMobSpawnerLogic();

	public CustomSpawnerEntity(BlockPos pos, BlockState state)
	{
		super(ThuwumcraftBlockEntities.CUSTOM_SPAWNER, pos, state);
	}

	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		this.logic.readNbt(this.world, this.pos, tag);
	}

	public void writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		this.logic.writeNbt(this.world, this.pos, tag);
	}

	public static void clientTick(World world, BlockPos pos, BlockState state, CustomSpawnerEntity blockEntity)
	{
		blockEntity.logic.clientTick(world, pos);
	}

	public static void serverTick(World world, BlockPos pos, BlockState state, CustomSpawnerEntity blockEntity)
	{
		blockEntity.logic.serverTick((ServerWorld)world, pos);
	}

	@Nullable
	public BlockEntityUpdateS2CPacket toUpdatePacket()
	{
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public NbtCompound toInitialChunkDataNbt()
	{
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		nbtCompound.remove("SpawnPotentials");
		return nbtCompound;
	}

	public CustomMobSpawnerLogic getLogic()
	{
		return this.logic;
	}
}
