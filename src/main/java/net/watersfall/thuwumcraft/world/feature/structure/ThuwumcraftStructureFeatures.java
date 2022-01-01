//package net.watersfall.thuwumcraft.world.feature.structure;
//
//import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
//import net.minecraft.util.registry.BuiltinRegistries;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.util.registry.RegistryKey;
//import net.minecraft.world.gen.GenerationStep;
//import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
//import net.minecraft.world.gen.feature.DefaultFeatureConfig;
//import net.minecraft.world.gen.feature.StructureFeature;
//import net.watersfall.thuwumcraft.Thuwumcraft;
//
//public class ThuwumcraftStructureFeatures
//{
//	public static final StructureFeature<DefaultFeatureConfig> UNKNOWN_PILLAR = new UnknownPillarFeature(DefaultFeatureConfig.CODEC);
//	public static final ConfiguredStructureFeature<?, ?> CONFIGURED_UNKNOWN_PILLAR = UNKNOWN_PILLAR.configure(DefaultFeatureConfig.DEFAULT);
//	public static final RegistryKey<ConfiguredStructureFeature<?, ?>> CONFIGURED_UNKNOWN_PILLAR_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, Thuwumcraft.getId("unknown_pillar"));
//
//	public static void register()
//	{
//		FabricStructureBuilder.create(Thuwumcraft.getId("unknown_pillar"), UNKNOWN_PILLAR)
//				.step(GenerationStep.Feature.SURFACE_STRUCTURES)
//				.superflatFeature(UNKNOWN_PILLAR.configure(DefaultFeatureConfig.DEFAULT))
//				.defaultConfig(4, 2, 12)
//				.adjustsSurface()
//				.register();
//		Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, CONFIGURED_UNKNOWN_PILLAR_KEY.getValue(), CONFIGURED_UNKNOWN_PILLAR);
//	}
//}
