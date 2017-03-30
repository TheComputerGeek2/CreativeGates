package com.massivecraft.creativegates.util;

import com.massivecraft.massivecore.Couple;

import java.util.HashMap;
import java.util.Map;

public class ModeUtil
{
	private static Map<Couple<Boolean, Boolean>, Couple<Boolean, Boolean>> nextModes = new HashMap<>();
	
	private static void registerNode(Boolean enterEnabledFirst, Boolean exitEnabledFirst, Boolean enterEnabledSecond, Boolean exitEnabledSecond)
	{
		nextModes.put(new Couple<Boolean, Boolean>(enterEnabledFirst, exitEnabledFirst), new Couple<Boolean, Boolean>(enterEnabledSecond, exitEnabledSecond));
	}
	
	static {
		registerNode(false, false, true, false);
		registerNode(true, false, false, true);
		registerNode(false, true, true, true);
		registerNode(true, true, false, false);
	}
	
	public static Couple<Boolean, Boolean> getNext(boolean enterEnabled, boolean exitEnabled)
	{
		return nextModes.get(new Couple<Boolean, Boolean>(enterEnabled, exitEnabled));
	}
	
}
