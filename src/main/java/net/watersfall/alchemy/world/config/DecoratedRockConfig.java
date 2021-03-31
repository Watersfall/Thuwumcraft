package net.watersfall.alchemy.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class DecoratedRockConfig implements FeatureConfig
{
	public static final Codec<DecoratedRockConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BlockStateProvider.TYPE_CODEC.fieldOf("primary_block").forGetter(object -> object.primaryBlock)
	).apply(instance, DecoratedRockConfig::new));

	public final BlockStateProvider primaryBlock;

	public DecoratedRockConfig(BlockStateProvider primaryBlock)
	{
		this.primaryBlock = primaryBlock;
	}
}
