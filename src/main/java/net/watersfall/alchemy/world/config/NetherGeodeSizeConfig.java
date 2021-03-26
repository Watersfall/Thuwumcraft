package net.watersfall.alchemy.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class NetherGeodeSizeConfig
{
	public static final Codec<NetherGeodeSizeConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Codec.intRange(1, 10).fieldOf("min_outer_wall_distance").orElse(4).forGetter((config) -> config.minOuterWallDistance),
			Codec.intRange(1, 20).fieldOf("max_outer_wall_distance").orElse(6).forGetter((config) -> config.maxOuterWallDistance),
			Codec.intRange(1, 10).fieldOf("min_distribution_points").orElse(3).forGetter((config) -> config.minDistributionPoints),
			Codec.intRange(1, 20).fieldOf("max_distribution_points").orElse(5).forGetter((config) -> config.maxDistributionPoints),
			Codec.intRange(0, 10).fieldOf("min_point_offset").orElse(1).forGetter((config) -> config.minPointOffset),
			Codec.intRange(0, 10).fieldOf("max_point_offset").orElse(3).forGetter((config) -> config.maxPointOffset),
			Codec.INT.fieldOf("min_gen_offset").orElse(-16).forGetter((config) -> config.minGenOffset),
			Codec.INT.fieldOf("max_gen_offset").orElse(16).forGetter((config) -> config.maxGenOffset)
	).apply(instance, NetherGeodeSizeConfig::new));

	public final int minOuterWallDistance;
	public final int maxOuterWallDistance;
	public final int minDistributionPoints;
	public final int maxDistributionPoints;
	public final int minPointOffset;
	public final int maxPointOffset;
	public final int minGenOffset;
	public final int maxGenOffset;

	public NetherGeodeSizeConfig(int minOuterWallDistance, int maxOuterWallDistance, int minDistributionPoints, int maxDistributionPoints, int minPointOffset, int maxPointOffset, int minGenOffset, int maxGenOffset)
	{
		this.minOuterWallDistance = minOuterWallDistance;
		this.maxOuterWallDistance = maxOuterWallDistance;
		this.minDistributionPoints = minDistributionPoints;
		this.maxDistributionPoints = maxDistributionPoints;
		this.minPointOffset = minPointOffset;
		this.maxPointOffset = maxPointOffset;
		this.minGenOffset = minGenOffset;
		this.maxGenOffset = maxGenOffset;
	}
}
