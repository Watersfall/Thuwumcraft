package net.watersfall.thuwumcraft.api.abilities.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.watersfall.wet.api.abilities.Ability;
import net.watersfall.wet.api.abilities.AbilityClientSerializable;

/**
 * TODO: Warp
 *
 * 		Warp is a living beings attunement to magic. Those whose minds
 * have	not been awakened perceive this as a bad thing, but those who
 * have experienced the unknown know it to be a wonderful thing.
 *
 * 		Warp can be broken down into three categories: temporary warp,
 * normal warp, and permanent warp.	Temporary warp typically comes from
 * the, generally incorrect, use of more simple magic. Being temporary,
 * it generally subsides soon after tainting the mind. Normal warp
 * is a bit more lasting, occupying the mind until special effort is
 * made to clear it away. These two forms of warp are generally
 * negative, lacking most of the magical awakening ability of permanent
 * warping, but still torturing the mind. Permanent warp, unlike the
 * other types, and much like the name, never goes away. A thaumaturge
 * who ventures into the unknown will forever be affected by it.
 *
 * 		Warp manifests itself in a thaumaturge in various ways.
 * Typically, a first exposure will come from experiments going wrong,
 * or delving into research that is beyond the extreme basics. This
 * warp will manifest itself in generally mild, if uncomfortable,
 * inconveniences. Small hallucinations, mild sickness, and a general
 * feeling of paranoia are common at this point. Many novice thaumaturges
 * get dissuaded, or quit thaumaturgy altogether due to this, but those
 * who choose to persist...
 *
 * 		Thaumaturges who continue find the earlier symptoms of warp
 * growing ever more common and intense. The feeling of someone being
 * behind, or hearing a blowing wind that isn't there, progresses into
 * hearing and feeling spiders crawling on your skin. To knowing you're
 * being followed but seeing the creature vanish in a puff of smoke when
 * you look at it. The mind breaking down at trying to make sense of the
 * new magical world you are beginning to perceive, and comprehend the
 * effect of uncovering it has done to you. However, warp seems to be
 * unable to progress past this phase through the standard means. Some
 * form of interference, an outside push, seems to be needed to open
 * up warp to being something useful.
 *
 * 		The first journey to the unknown seems to be that outside push.
 * The essence of the world permeates the thaumaturge, opening up their
 * mind to many more possibilities in the world of magic. Immediately after
 * reaching this dimension, the thaumaturges magical abilities become
 * expanded, making channeling magic easier, significantly so for an
 * extremely warped mind. The thaumaturge themselves also becomes capable
 * of holding vis inside of them, similar to the environment or a wand.
 * Like before, the amount of vis stored increases with warp.
 *
 * 		This awakening, however, is not without consequences. The
 * pre-awakened hallucinations were more real than thought. Seeing
 * magical beings becomes much easier and, more importantly, they
 * now take serious notice of you. Mind Spiders are one such being,
 * incorrectly recognizing you as one of the ancient thaumaturges native
 * to the unknown. Little is known about these spiders other than that
 * they have an incredible capacity for magic, and are drawn to others
 * who do as well. There are other, much more human, creatures attracted
 * to those who've reached this state.
 *
 * // TODO: Come up with some crimson cult thing
 *
 * Standard Effects of Warp; These apply to anyone with warp but without permanent warp
 *		- Ominous messages: "I am being watched", "I need to get out of here", etc.
 *		- Hallucinations: Hearing hostile mob spawns, seeing fake translucent mobs and players
 *		- Status Effects: Blindness, night vision
 *		- Increasing or decreasing temporary and normal warp
 *		- Revealing unknown fluid
 *
 * Effects of Permanent Warp; These apply exclusively because of permanent warp level
 * 		- Increased vis capacity of wands, represented through reduced costs
 * 		- Mind spiders
 * 		- Spawning of crimson cult equivalent
 * 		- Revealing/Discovering eldritch research
 */
public interface PlayerWarpAbility extends Ability<Entity>, AbilityClientSerializable<Entity>
{
	public static final Identifier ID = new Identifier("thuwumcraft", "warp");

	double getTemporaryWarp();

	void setTemporaryWarp(double warp);

	double getNormalWarp();

	void setNormalWarp(double warp);

	double getPermanentWarp();

	void setPermanentWarp(double warp);

	default double getTotalWarp()
	{
		return getTemporaryWarp() + getNormalWarp() + getPermanentWarp();
	}

	@Override
	default Identifier getId()
	{
		return ID;
	}
}
