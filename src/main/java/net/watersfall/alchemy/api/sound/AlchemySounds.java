package net.watersfall.alchemy.api.sound;

import net.minecraft.sound.SoundEvent;
import net.watersfall.alchemy.AlchemyMod;

public class AlchemySounds
{
	public static final SoundEvent CAULDRON_ADD_INGREDIENT = new SoundEvent(AlchemyMod.getId("block.cauldron.add_ingredient"));
	public static final SoundEvent USE_DUST_SOUND = new SoundEvent(AlchemyMod.getId("item.magic_dust.use"));
	public static final SoundEvent BUBBLE_SOUND = new SoundEvent(AlchemyMod.getId("block.cauldron.bubble"));
	public static final SoundEvent BOOK_OPEN_SOUND = new SoundEvent(AlchemyMod.getId("item.research_book.open"));
	public static final SoundEvent RINGING_SOUND = new SoundEvent(AlchemyMod.getId("dimension.the_unknown.ringing"));
	public static final SoundEvent STATIC_SOUND = new SoundEvent(AlchemyMod.getId("dimension.the_unknown.static"));
	public static final SoundEvent POCKET_SAND = new SoundEvent(AlchemyMod.getId("spell.pocket_sand"));
}
