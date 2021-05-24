package net.watersfall.thuwumcraft.world.structure;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.watersfall.thuwumcraft.AlchemyMod;

import java.util.Random;
import java.util.function.Function;

public class UnknownPillarGenerator
{
	private static final Identifier CHEST = AlchemyMod.getId("unknown/deco/pillar_0");
	private static final Identifier NO_CHEST = AlchemyMod.getId("unknown/deco/pillar_1");

	public static class Piece extends SimpleStructurePiece
	{
		private Structure noChestStructure;
		private Structure chestStructure;

		public static Piece of(StructureManager manager, BlockPos pos)
		{
			StructurePlacementData placementData = (new StructurePlacementData())
					.setRotation(BlockRotation.NONE)
					.setMirror(BlockMirror.NONE)
					.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			return new Piece(manager, NO_CHEST, "", placementData, pos);
		}

		public static StructurePiece of(ServerWorld serverWorld, NbtCompound nbtCompound)
		{
			return new Piece(nbtCompound, serverWorld, (identifier -> {
				return new StructurePlacementData()
						.setRotation(BlockRotation.NONE)
						.setMirror(BlockMirror.NONE)
						.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			}));
		}

		public Piece(StructureManager structureManager, Identifier identifier, String template, StructurePlacementData structurePlacementData, BlockPos blockPos)
		{
			super(AlchemyStructurePieceTypes.UNKNOWN_PILLAR, 0, structureManager, identifier, template, structurePlacementData, blockPos);
			init(structureManager);
		}

		public Piece(NbtCompound nbtCompound, ServerWorld world, Function<Identifier, StructurePlacementData> function)
		{
			super(AlchemyStructurePieceTypes.UNKNOWN_PILLAR, nbtCompound, world, function);
			init(world.getStructureManager());
		}

		private void init(StructureManager manager)
		{
			chestStructure = manager.getStructure(CHEST);
			noChestStructure = manager.getStructure(NO_CHEST);
		}

		@Override
		protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox)
		{

		}

		@Override
		public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos pos)
		{
			pos = pos.offset(Direction.DOWN);
			boundingBox.offset(0, -1, 0);
			this.pos = pos;
			if(random.nextInt(10) == 0)
			{
				this.structure = this.chestStructure;
			}
			else
			{
				this.structure = this.noChestStructure;
			}
			return super.generate(world, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, pos);
		}
	}
}
