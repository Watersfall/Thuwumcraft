package net.watersfall.thuwumcraft.world.village;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.watersfall.thuwumcraft.Thuwumcraft;
import net.watersfall.thuwumcraft.world.structure.pool.SpecialSinglePoolElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class VillageAdditions
{
	public static void register()
	{
		addStructure(new Identifier("village/plains/houses"), Thuwumcraft.getId("village/plains/wizard_towers/wizard_tower_0"), 10);
	}

	private static void addStructure(Identifier poolId, Identifier addId, int weight)
	{
		StructurePool pool = BuiltinRegistries.STRUCTURE_POOL.get(poolId);
		if(pool == null)
			return;
		List<StructurePoolElement> oldList = pool.getElementIndicesInRandomOrder(new Random(0));
		Map<StructurePoolElement, Integer> map = new HashMap<>();
		oldList.forEach(element -> {
			map.compute(element, (k, v) -> v == null ? 1 : v + 1);
		});
		List<Pair<StructurePoolElement, Integer>> newList = map.entrySet()
				.stream()
				.map((entry) -> Pair.of(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
		newList.add(Pair.of(new SpecialSinglePoolElement(Either.left(addId), () -> StructureProcessorLists.MOSSIFY_20_PERCENT, StructurePool.Projection.RIGID), weight));
		((MutableRegistry)BuiltinRegistries.STRUCTURE_POOL).set(BuiltinRegistries.STRUCTURE_POOL.getRawId(pool), BuiltinRegistries.STRUCTURE_POOL.getKey(pool).get(),new StructurePool(new Identifier("village/plains/houses"), new Identifier("village/plains/terminators"), newList), Lifecycle.stable());
	}
}
