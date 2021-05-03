package net.watersfall.alchemy.world.structure;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
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
import net.watersfall.alchemy.AlchemyMod;

import java.util.Random;

public class UnknownPillarGenerator
{
	private static final Identifier CHEST = AlchemyMod.getId("unknown/deco/pillar_0");
	private static final Identifier NO_CHEST = AlchemyMod.getId("unknown/deco/pillar_1");

	public static class Piece extends SimpleStructurePiece
	{
		private Structure noChestStructure;
		private Structure chestStructure;

		public Piece(StructureManager manager, BlockPos pos)
		{
			super(AlchemyStructurePieceTypes.UNKNOWN_PILLAR, 0);
			this.pos = pos;
			init(manager);
		}

		public Piece(ServerWorld world, NbtCompound tag)
		{
			super(AlchemyStructurePieceTypes.UNKNOWN_PILLAR, tag);
			init(world.getStructureManager());
		}

		private void init(StructureManager manager)
		{
			this.structure = manager.getStructure(NO_CHEST);
			this.noChestStructure = this.structure;
			this.chestStructure = manager.getStructure(CHEST);
			StructurePlacementData placementData = (new StructurePlacementData())
					.setRotation(BlockRotation.NONE)
					.setMirror(BlockMirror.NONE)
					.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			this.setStructureData(this.structure, this.pos, placementData);
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
