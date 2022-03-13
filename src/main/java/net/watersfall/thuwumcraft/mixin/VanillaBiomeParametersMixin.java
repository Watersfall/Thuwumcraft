package net.watersfall.thuwumcraft.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import net.watersfall.thuwumcraft.world.biome.ThuwumcraftBiomes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public abstract class VanillaBiomeParametersMixin
{
	@Shadow protected abstract void writeBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome);

	@Shadow @Final private MultiNoiseUtil.ParameterRange[] temperatureParameters;

	@Shadow @Final private MultiNoiseUtil.ParameterRange[] humidityParameters;

	@Shadow @Final private MultiNoiseUtil.ParameterRange midInlandContinentalness;

	@Shadow @Final private MultiNoiseUtil.ParameterRange nearInlandContinentalness;

	@Shadow @Final private MultiNoiseUtil.ParameterRange[] erosionParameters;

	@Inject(method = "writeVanillaBiomeParameters", at = @At("TAIL"))
	public void thuwumcraft$addBiome(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, CallbackInfo info)
	{
		writeMagicForest(parameters, MultiNoiseUtil.ParameterRange.of(-1, 0));
	}

	private void writeMagicForest(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange weirdness)
	{
		writeBiomeParameters(parameters, temperatureParameters[3], humidityParameters[2], midInlandContinentalness, erosionParameters[5], weirdness, 0, ThuwumcraftBiomes.MAGIC_FOREST_BIOME_KEY);
		writeBiomeParameters(parameters, temperatureParameters[3], humidityParameters[3], nearInlandContinentalness, erosionParameters[4], weirdness, 0, ThuwumcraftBiomes.MAGIC_FOREST_BIOME_KEY);
	}
}
