package net.watersfall.alchemy.world.biome;

import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.watersfall.alchemy.AlchemyMod;
import net.watersfall.alchemy.world.feature.AlchemyFeatures;

public class AlchemyBiomes
{
	public static final ConfiguredSurfaceBuilder<TernarySurfaceConfig> MAGIC_FOREST_BIOME_CONFIG;
	public static final Biome MAGIC_FOREST_BIOME;
	public static final RegistryKey<Biome> MAGIC_FOREST_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, AlchemyMod.getId("magic_forest"));

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
		DefaultBiomeFeatures.addForestGrass(generationSettings);
		DefaultBiomeFeatures.addDefaultMushrooms(generationSettings);
		DefaultBiomeFeatures.addSwampVegetation(generationSettings);
		DefaultBiomeFeatures.addExtraDefaultFlowers(generationSettings);
		DefaultBiomeFeatures.addSweetBerryBushes(generationSettings);
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
	}

	public static void register()
	{
		Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, AlchemyMod.getId("magic_surface"), MAGIC_FOREST_BIOME_CONFIG);
		Registry.register(BuiltinRegistries.BIOME, MAGIC_FOREST_BIOME_KEY.getValue(), MAGIC_FOREST_BIOME);
		OverworldBiomes.addContinentalBiome(MAGIC_FOREST_BIOME_KEY, OverworldClimate.TEMPERATE, 2D);
	}
}