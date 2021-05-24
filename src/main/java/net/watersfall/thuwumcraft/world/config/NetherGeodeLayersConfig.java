package net.watersfall.thuwumcraft.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.GeodeLayerConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class NetherGeodeLayersConfig
{
	public static final Codec<NetherGeodeLayersConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			GeodeLayerConfig.CODEC.fieldOf("config").forGetter(object -> object.layer),
			BlockStateProvider.TYPE_CODEC.fieldOf("required_block").forGetter(object -> object.required),
			Codec.INT.fieldOf("multiplier").forGetter(object -> object.multiplier)
	).apply(instance, NetherGeodeLayersConfig::new));

	public final GeodeLayerConfig layer;
	public final BlockStateProvider required;
	public final int multiplier;

	public NetherGeodeLayersConfig(GeodeLayerConfig layer, BlockStateProvider required, int multiplier)
	{
		this.layer = layer;
		this.required = required;
		this.multiplier = multiplier;
	}
}
