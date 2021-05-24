package net.watersfall.thuwumcraft.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.*;

public class NetherGeodeConfig implements FeatureConfig
{public static final Codec<Double> RANGE = Codec.doubleRange(0.0D, 1.0D);
	public static final Codec<NetherGeodeConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			NetherGeodeLayersConfig.CODEC.fieldOf("blocks1").forGetter((config) -> config.layerConfig1),
			NetherGeodeLayersConfig.CODEC.fieldOf("blocks2").forGetter((config) -> config.layerConfig2),
			NetherGeodeLayersConfig.CODEC.fieldOf("blocks3").forGetter((config) -> config.layerConfig3),
			GeodeLayerThicknessConfig.CODEC.fieldOf("layers").forGetter((config) -> config.layerThicknessConfig),
			GeodeCrackConfig.CODEC.fieldOf("crack").forGetter((config) -> config.crackConfig),
			NetherGeodeSizeConfig.CODEC.fieldOf("size").forGetter(config -> config.sizeConfig),
			RANGE.fieldOf("use_potential_placements_chance").orElse(0.35D).forGetter((config) -> config.usePotentialPlacementsChance),
			RANGE.fieldOf("use_alternate_layer0_chance").orElse(0.0D).forGetter((config) -> config.useAlternateLayer0Chance),
			Codec.BOOL.fieldOf("placements_require_layer0_alternate").orElse(true).forGetter((config) -> config.placementsRequireLayer0Alternate),
			RANGE.fieldOf("noise_multiplier").orElse(0.05D).forGetter((config) -> config.noiseMultiplier),
			Codec.INT.fieldOf("invalid_blocks_threshold").forGetter((config) -> config.invalidBlocksThreshold)
	).apply(instance, NetherGeodeConfig::new));
	public final NetherGeodeLayersConfig layerConfig1;
	public final NetherGeodeLayersConfig layerConfig2;
	public final NetherGeodeLayersConfig layerConfig3;
	public final GeodeLayerThicknessConfig layerThicknessConfig;
	public final GeodeCrackConfig crackConfig;
	public final NetherGeodeSizeConfig sizeConfig;
	public final double usePotentialPlacementsChance;
	public final double useAlternateLayer0Chance;
	public final boolean placementsRequireLayer0Alternate;
	public final double noiseMultiplier;
	public final int invalidBlocksThreshold;

	public NetherGeodeConfig(NetherGeodeLayersConfig layerConfig1,
							 NetherGeodeLayersConfig layerConfig2,
							 NetherGeodeLayersConfig layerConfig3,
							 GeodeLayerThicknessConfig layerThicknessConfig,
							 GeodeCrackConfig crackConfig,
							 NetherGeodeSizeConfig sizeConfig,
							 double usePotentialPlacementsChance,
							 double useAlternateLayer0Chance,
							 boolean placementsRequireLayer0Alternate,
							 double noiseMultiplier,
							 int invalidBlocksThreshold)
	{
		this.layerConfig1 = layerConfig1;
		this.layerConfig2 = layerConfig2;
		this.layerConfig3 = layerConfig3;
		this.layerThicknessConfig = layerThicknessConfig;
		this.crackConfig = crackConfig;
		this.sizeConfig = sizeConfig;
		this.usePotentialPlacementsChance = usePotentialPlacementsChance;
		this.useAlternateLayer0Chance = useAlternateLayer0Chance;
		this.placementsRequireLayer0Alternate = placementsRequireLayer0Alternate;
		this.noiseMultiplier = noiseMultiplier;
		this.invalidBlocksThreshold = invalidBlocksThreshold;
	}
}
