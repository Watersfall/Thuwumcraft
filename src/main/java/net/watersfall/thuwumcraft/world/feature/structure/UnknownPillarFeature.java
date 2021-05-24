package net.watersfall.thuwumcraft.world.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.watersfall.thuwumcraft.world.structure.UnknownPillarGenerator;

import java.util.Random;

public class UnknownPillarFeature extends StructureFeature<DefaultFeatureConfig>
{
	public UnknownPillarFeature(Codec<DefaultFeatureConfig> codec)
	{
		super(codec);
	}

	@Override
	public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory()
	{
		return Start::new;
	}

	@Override
	protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, ChunkRandom random, ChunkPos chunkPos, Biome biome, ChunkPos chunkPos2, DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView)
	{
		return super.shouldStartAt(chunkGenerator, biomeSource, worldSeed, random, chunkPos, biome, chunkPos2, featureConfig, heightLimitView);
	}

	public static class Start extends StructureStart<DefaultFeatureConfig>
	{
		public Start(StructureFeature<DefaultFeatureConfig> feature, ChunkPos chunkPos, int i, long l)
		{
			super(feature, chunkPos, i, l);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos chunkPos, Biome biome, DefaultFeatureConfig featureConfig, HeightLimitView heightLimitView)
		{
			int x = chunkPos.getStartX() + random.nextInt(16);
			int z = chunkPos.getStartZ() + random.nextInt(16);
			int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, heightLimitView);
			BlockPos pos = new BlockPos(x, y, z);
			this.children.add(UnknownPillarGenerator.Piece.of(manager, pos));
			this.setBoundingBoxFromChildren();
		}

		@Override
		public void generateStructure(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox box, ChunkPos chunkPos)
		{
			super.generateStructure(world, structureAccessor, chunkGenerator, random, box, chunkPos);
		}
	}
}
