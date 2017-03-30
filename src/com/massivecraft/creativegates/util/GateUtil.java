package com.massivecraft.creativegates.util;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.block.Block;

public class GateUtil
{
	// -------------------------------------------- //
	// IS X NEARBY
	// -------------------------------------------- //
	
	public static boolean isGateNearby(Block block)
	{
		UConf uconf = UConf.get(block);
		if (!uconf.isEnabled()) return false;
		
		final int radius = 3;
		for (int dx = -radius; dx <= radius; dx++)
		{
			for (int dy = -radius; dy <= radius; dy++)
			{
				for (int dz = -radius; dz <= radius; dz++)
				{
					if (CreativeGates.get().getIndex().get(PS.valueOf(block.getRelative(dx, dy, dz))) != null) return true;
				}
			}
		}
		return false;
	}
	
}
