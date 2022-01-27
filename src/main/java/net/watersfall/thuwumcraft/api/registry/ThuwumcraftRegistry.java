package net.watersfall.thuwumcraft.api.registry;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.watersfall.thuwumcraft.api.research.Research;
import net.watersfall.thuwumcraft.api.research.ResearchCategory;
import net.watersfall.thuwumcraft.api.spell.SpellModifierDataType;
import net.watersfall.thuwumcraft.api.spell.SpellType;
import net.watersfall.thuwumcraft.item.wand.WandCapMaterial;
import net.watersfall.thuwumcraft.item.wand.WandCoreMaterial;
import net.watersfall.thuwumcraft.registry.ThuwumcraftDoubleIdRegistryImpl;
import net.watersfall.thuwumcraft.registry.ThuwumcraftRegistryImpl;

import java.util.Set;

public interface ThuwumcraftRegistry<K, V>
{
	ThuwumcraftRegistry<Identifier, Research> RESEARCH = new ThuwumcraftRegistryImpl<>();
	ThuwumcraftRegistry<Identifier, ResearchCategory> RESEARCH_CATEGORY = new ThuwumcraftRegistryImpl<>();
	ThuwumcraftDoubleIdRegistry<Item, WandCoreMaterial> WAND_CORE = new ThuwumcraftDoubleIdRegistryImpl<>();
	ThuwumcraftDoubleIdRegistry<Item, WandCapMaterial> WAND_CAP = new ThuwumcraftDoubleIdRegistryImpl<>();
	ThuwumcraftRegistry<Identifier, SpellType<?>> SPELL = new ThuwumcraftRegistryImpl<>();
	ThuwumcraftRegistry<Identifier, SpellModifierDataType<?>> SPELL_DATA = new ThuwumcraftRegistryImpl<>();

	V register(K id, V value);

	V get(K id);

	K getId(V value);

	Set<V> values();

	void clear();
}
