package net.watersfall.thuwumcraft.world.biome;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.registry.ThuwumcraftParticles;
import net.watersfall.thuwumcraft.world.feature.ThuwumcraftPlacedFeatures;

public class ThuwumcraftBiomes
{/**
	 * TODO: Unknown Leakage
	 *
	 * Magical forests are the result of magic from the Unknown leaking into the overworld.
	 * Typically, this magic is only enough to encourage plant growth and give the area a
	 * nice blue and purple glow. This magic also fills the area, and the world, with vis.
	 * Most vis in the world emanates out of these tears, called nodes. While nodes are
	 * present in other parts of the world, the most powerful ones are exclusively found
	 * in magical forests
	 */
	public static final Biome MAGIC_FOREST_BIOME;

	public static final Biome THE_UNKNOWN_BIOME;
	public static final Biome THE_LOST_FOREST_BIOME;
	public static final RegistryKey<Biome> MAGIC_FOREST_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, Thuwumcraft.getId("magic_forest"));
	public static final RegistryKey<Biome> THE_LOST_FOREST_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, Thuwumcraft.getId("the_lost_forest"));
	public static final RegistryKey<Biome> THE_UNKNOWN_BIOME_KEY = RegistryKey.of(Registry.BIOME_KEY, Thuwumcraft.getId("the_unknown"));

	public static Biome createMagicForest()
	{
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
		DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
		DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 100, false);

		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		DefaultBiomeFeatures.addLandCarvers(generationSettings);
		DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
		DefaultBiomeFeatures.addDungeons(generationSettings);
		DefaultBiomeFeatures.addMineables(generationSettings);
		DefaultBiomeFeatures.addDefaultOres(generationSettings);
		DefaultBiomeFeatures.addDefaultDisks(generationSettings);
		DefaultBiomeFeatures.addSprings(generationSettings);
		DefaultBiomeFeatures.addForestFlowers(generationSettings);
		DefaultBiomeFeatures.addPlainsTallGrass(generationSettings);
		DefaultBiomeFeatures.addPlainsFeatures(generationSettings);
		generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_WATERLILY);
		DefaultBiomeFeatures.addExtraDefaultFlowers(generationSettings);
		DefaultBiomeFeatures.addDefaultMushrooms(generationSettings);
		DefaultBiomeFeatures.addSwampVegetation(generationSettings);
		DefaultBiomeFeatures.addSweetBerryBushes(generationSettings);
		generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ThuwumcraftPlacedFeatures.MAGIC_FOREST_TREES_PLACED);
		generationSettings.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, ThuwumcraftPlacedFeatures.MOSSY_ASPECT_ROCKS_PLACED);
		return new Biome.Builder()
				.generationSettings(generationSettings.build())
				.spawnSettings(spawnSettings.build())
				.precipitation(Biome.Precipitation.RAIN)
				.category(Biome.Category.FOREST)
				.temperature(0.8F)
				.downfall(0.8F)
				.temperatureModifier(Biome.TemperatureModifier.NONE)
				.effects(new BiomeEffects.Builder()
						.fogColor(0x9E6BCE)
						.foliageColor(0x40CFAA)
						.grassColor(0x00DFDF)
						.skyColor(0x00FFFF)
						.waterColor(0x8080FF)
						.waterFogColor(0x8080FF)
						.grassColorModifier(BiomeEffects.GrassColorModifier.NONE)
						.particleConfig(new BiomeParticleConfig(ThuwumcraftParticles.MAGIC_FOREST, 0.0025F))
						.build()
				)
				.build();
	}

	public static Biome createUnknown()
	{
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
		return new Biome.Builder()
				.generationSettings(generationSettings.build())
				.spawnSettings(spawnSettings.build())
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.ICY)
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
		generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ThuwumcraftPlacedFeatures.THE_LOST_FOREST_TREES_PLACED);
		return new Biome.Builder()
				.generationSettings(generationSettings.build())
				.spawnSettings(spawnSettings.build())
				.precipitation(Biome.Precipitation.NONE)
				.category(Biome.Category.ICY)
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
		MAGIC_FOREST_BIOME = createMagicForest();
		THE_UNKNOWN_BIOME = createUnknown();
		THE_LOST_FOREST_BIOME = createLostForest();
	}

	public static void register()
	{
		Registry.register(BuiltinRegistries.BIOME, MAGIC_FOREST_BIOME_KEY.getValue(), MAGIC_FOREST_BIOME);
		Registry.register(BuiltinRegistries.BIOME, THE_UNKNOWN_BIOME_KEY.getValue(), THE_UNKNOWN_BIOME);
		Registry.register(BuiltinRegistries.BIOME, THE_LOST_FOREST_BIOME_KEY.getValue(), THE_LOST_FOREST_BIOME);
	}
}