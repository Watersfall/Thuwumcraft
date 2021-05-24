package net.watersfall.thuwumcraft.world.biome;

import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.watersfall.thuwumcraft.AlchemyMod;
import net.watersfall.thuwumcraft.block.AlchemyBlocks;
import net.watersfall.thuwumcraft.particle.AlchemyParticles;
import net.watersfall.thuwumcraft.world.feature.AlchemyFeatures;
import net.watersfall.thuwumcraft.world.feature.structure.AlchemyStructureFeatures;

public class AlchemyBiomes
{
	public static final ConfiguredSurfaceBuilder<TernarySurfaceConfig> MAGIC_FOREST_BIOME_CONFIG;
	public static final ConfiguredSurfaceBuilder<TernarySurfaceConfig> THE_UNKNOWN_BIOME_CONFIG;
	public static final ConfiguredSurfaceBuilder<TernarySurfaceConfig> THE_LOST_FOREST_BIOME_CONFIG;
	public static final Biome MAGIC_FOREST_BIOME;
	public static final Biome THE_UNKNOWN_BIOME;
	public static final Biome THE_LOST_FOREST_BIOME;
	public static final RegistryKey<Biome> MAGIC_FOREST_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, AlchemyMod.getId("magic_forest"));
	public static final RegistryKey<Biome> THE_LOST_FOREST_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, AlchemyMod.getId("the_lost_forest"));
	public static final RegistryKey<Biome> THE_UNKNOWN_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, AlchemyMod.getId("the_unknown"));

	public static Biome createMagicForest()
	{
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
		DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
		DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 100);

		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		generationSettings.surfaceBuilder(MAGIC_FOREST_BIOME_CONFIG);
		DefaultBiomeFeatures.addDefaultUndergroundStructures(generationSettings);
		DefaultBiomeFeatures.addLandCarvers(generationSettings);
		DefaultBiomeFeatures.addDefaultLakes(generationSettings);
		DefaultBiomeFeatures.addDungeons(generationSettings);
		DefaultBiomeFeatures.addMineables(generationSettings);
		DefaultBiomeFeatures.addDefaultOres(generationSettings);
		DefaultBiomeFeatures.addDefaultDisks(generationSettings);
		DefaultBiomeFeatures.addSprings(generationSettings);
		DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
		DefaultBiomeFeatures.addForestFlowers(generationSettings);
		DefaultBiomeFeatures.addDefaultMushrooms(generationSettings);
		DefaultBiomeFeatures.addSwampVegetation(generationSettings);
		DefaultBiomeFeatures.addExtraDefaultFlowers(generationSettings);
		DefaultBiomeFeatures.addSweetBerryBushes(generationSettings);
		DefaultBiomeFeatures.addPlainsTallGrass(generationSettings);
		DefaultBiomeFeatures.addPlainsFeatures(generationSettings);
		generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, AlchemyFeatures.MAGIC_FOREST_TREES);
		generationSettings.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, AlchemyFeatures.MOSSY_ASPECT_ROCKS);
		generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_WATERLILLY);
		return new Biome.Builder()
				.generationSettings(generationSettings.build())
				.spawnSettings(spawnSettings.build())
				.precipitation(Biome.Precipitation.RAIN)
				.category(Biome.Category.FOREST)
				.depth(0.125F)
				.scale(0.05F)
				.temperature(0.8F)
				.downfall(0.4F)
				.temperatureModifier(Biome.TemperatureModifier.NONE)
				.effects(new BiomeEffects.Builder()
						.fogColor(0x9E6BCE)
						.foliageColor(0x40CFAA)
						.grassColor(0x00DFDF)
						.skyColor(0x00FFFF)
						.waterColor(0x8080FF)
						.waterFogColor(0x8080FF)
						.grassColorModifier(BiomeEffects.GrassColorModifier.NONE)
						.particleConfig(new BiomeParticleConfig(AlchemyParticles.MAGIC_FOREST, 0.0025F))
						.build()
				)
				.build();
	}

	public static Biome createUnknown()
	{
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		generationSettings.surfaceBuilder(THE_UNKNOWN_BIOME_CONFIG);
		DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
		return new Biome.Builder()
				.generationSettings(generationSettings.build())
				.spawnSettings(spawnSettings.build())
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.ICY)
				.depth(0.125F)
				.scale(0.05F)
				.temperature(0.0F)
				.downfall(0.0F)
				.temperatureModifier(Biome.TemperatureModifier.NONE)
				.effects(new BiomeEffects.Builder()
						.fogColor(0x000000)
						.foliageColor(0x3D3D43)
						.grassColor(0x3D3D43)
						.skyColor(0x000000)
						.waterColor(0x000000)
						.waterFogColor(0x000000)
						.grassColorModifier(BiomeEffects.GrassColorModifier.NONE)
						.build()
				)
				.build();
	}

	public static Biome createLostForest()
	{
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		generationSettings.surfaceBuilder(THE_LOST_FOREST_BIOME_CONFIG);
		generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, AlchemyFeatures.THE_LOST_FOREST_TREES);
		generationSettings.structureFeature(AlchemyStructureFeatures.CONFIGURED_UNKNOWN_PILLAR);
		return new Biome.Builder()
				.generationSettings(generationSettings.build())
				.spawnSettings(spawnSettings.build())
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.ICY)
				.depth(0.125F)
				.scale(0.05F)
				.temperature(0.0F)
				.downfall(0.0F)
				.temperatureModifier(Biome.TemperatureModifier.NONE)
				.effects(new BiomeEffects.Builder()
						.fogColor(0x000000)
						.foliageColor(0x3D3D43)
						.grassColor(0x3D3D43)
						.skyColor(0x000000)
						.waterColor(0x000000)
						.waterFogColor(0x000000)
						.grassColorModifier(BiomeEffects.GrassColorModifier.NONE)
						.build()
				)
				.build();
	}

	static
	{
		MAGIC_FOREST_BIOME_CONFIG = SurfaceBuilder.DEFAULT.withConfig(new TernarySurfaceConfig(
				Blocks.GRASS_BLOCK.getDefaultState(),
				Blocks.DIRT.getDefaultState(),
				Blocks.SAND.getDefaultState()
		));
		MAGIC_FOREST_BIOME = createMagicForest();
		THE_UNKNOWN_BIOME_CONFIG = SurfaceBuilder.DEFAULT.withConfig(new TernarySurfaceConfig(
				Blocks.DEEPSLATE.getDefaultState(),
				Blocks.DEEPSLATE.getDefaultState(),
				Blocks.DEEPSLATE.getDefaultState()
		));
		THE_UNKNOWN_BIOME = createUnknown();
		THE_LOST_FOREST_BIOME_CONFIG = SurfaceBuilder.DEFAULT.withConfig(new TernarySurfaceConfig(
				AlchemyBlocks.DEEPSLATE_GRASS.getDefaultState(),
				Blocks.DEEPSLATE.getDefaultState(),
				Blocks.DEEPSLATE.getDefaultState()
		));
		THE_LOST_FOREST_BIOME = createLostForest();
	}

	public static void register()
	{
		Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, AlchemyMod.getId("magic_surface"), MAGIC_FOREST_BIOME_CONFIG);
		Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, AlchemyMod.getId("the_unknown_surface"), THE_UNKNOWN_BIOME_CONFIG);
		Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, AlchemyMod.getId("the_lost_forest"), THE_LOST_FOREST_BIOME_CONFIG);
		Registry.register(BuiltinRegistries.BIOME, MAGIC_FOREST_BIOME_KEY.getValue(), MAGIC_FOREST_BIOME);
		Registry.register(BuiltinRegistries.BIOME, THE_UNKNOWN_BIOME_KEY.getValue(), THE_UNKNOWN_BIOME);
		Registry.register(BuiltinRegistries.BIOME, THE_LOST_FOREST_BIOME_KEY.getValue(), THE_LOST_FOREST_BIOME);
		OverworldBiomes.addContinentalBiome(MAGIC_FOREST_BIOME_KEY, OverworldClimate.TEMPERATE, 2D);
	}
}