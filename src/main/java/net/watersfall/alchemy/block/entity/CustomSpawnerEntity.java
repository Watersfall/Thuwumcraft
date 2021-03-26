package net.watersfall.alchemy.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import net.watersfall.alchemy.block.CustomSpawnerBlock;
import org.jetbrains.annotations.Nullable;

public class CustomSpawnerEntity extends BlockEntity
{
	private final MobSpawnerLogic logic = new MobSpawnerLogic()
	{
		public void sendStatus(World world, BlockPos pos, int i)
		{
			world.addSyncedBlockEvent(pos, Blocks.SPAWNER, i, 0);
		}

		public void setSpawnEntry(@Nullable World world, BlockPos pos, MobSpawnerEntry spawnEntry) {

			super.setSpawnEntry(world, pos, spawnEntry);
			if (world != null)
			{
				BlockState blockState = world.getBlockState(pos);
				world.updateListeners(pos, blockState, blockState, 4);
			}
		}
	};

	public CustomSpawnerEntity(BlockPos pos, BlockState state)
	{
		super(AlchemyBlockEntities.CUSTOM_SPAWNER_ENTITY, pos, state);
	}

	public void readNbt(NbtCompound tag)
	{
		super.readNbt(tag);
		this.logic.readNbt(this.world, this.pos, tag);
	}

	public NbtCompound writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);
		this.logic.writeNbt(this.world, this.pos, tag);
		return tag;
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
		return new BlockEntityUpdateS2CPacket(this.pos, 1, this.toInitialChunkDataNbt());
	}

	public NbtCompound toInitialChunkDataNbt()
	{
		NbtCompound nbtCompound = this.writeNbt(new NbtCompound());
		nbtCompound.remove("SpawnPotentials");
		return nbtCompound;
	}

	public boolean onSyncedBlockEvent(int type, int data)
	{
		return this.logic.method_8275(this.world, type) || super.onSyncedBlockEvent(type, data);
	}

	public MobSpawnerLogic getLogic()
	{
		return this.logic;
	}
}
