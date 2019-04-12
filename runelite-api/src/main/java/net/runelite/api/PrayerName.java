package net.runelite.api;

public enum PrayerName
{
	THICK_SKIN("Thick Skin"),
	BURST_OF_STRENGTH("Burst of Strength"),
	CLARITY_OF_THOUGHT("Clarity of Thought"),
	SHARP_EYE("Sharp Eye"),
	MYSTIC_WILL("Mystic Will"),
	ROCK_SKIN("Rock Skin"),
	SUPERHUMAN_STRENGTH("Superhuman Strength"),
	IMPROVED_REFLEXES("Improved Reflexes"),
	RAPID_RESTORE("Rapid Restore"),
	RAPID_HEAL("Rapid Heal"),
	PROTECT_ITEM("Protect Item"),
	HAWK_EYE("Hawk Eye"),
	MYSTIC_LORE("Mystic Lore"),
	STEEL_SKIN("Steel Skin"),
	ULTIMATE_STRENGTH("Ultimate Strength"),
	INCREDIBLE_REFLEXES("Incredible Reflexes"),
	PROTECT_FROM_MAGIC("Protect from Magic"),
	PROTECT_FROM_MISSILES("Protect from Missiles"),
	PROTECT_FROM_MELEE("Protect from Melee"),
	EAGLE_EYE("Eagle Eye"),
	MYSTIC_MIGHT("Mystic Might"),
	RETRIBUTION("Retribution"),
	REDEMPTION("Redemption"),
	SMITE("Smite"),
	PRESERVE("Preserve"),
	CHIVALRY("Chivalry"),
	PIETY("Piety"),
	RIGOUR("Rigour"),
	AUGURY("Augury");

	private final String name;

	PrayerName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the name of the prayer.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
}
