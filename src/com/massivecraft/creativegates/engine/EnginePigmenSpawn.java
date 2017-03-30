package com.massivecraft.creativegates.engine;

import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.creativegates.util.GateUtil;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EnginePigmenSpawn extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EnginePigmenSpawn i = new EnginePigmenSpawn();
	public static EnginePigmenSpawn get() { return i; }
	
	// -------------------------------------------- //
	// LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void noZombiePigmanPortalSpawn(CreatureSpawnEvent event)
	{
		// If a zombie pigman is spawning ...
		if (event.getEntityType() != EntityType.PIG_ZOMBIE) return;
		
		// ... because of a nether portal ...
		if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NETHER_PORTAL) return;
		
		// ... near a gate ...
		Location location = event.getLocation();
		if (!GateUtil.isGateNearby(location.getBlock())) return;
		
		// ... and we are blocking zombie pigman portal spawn ...
		if (UConf.get(location).isPigmanPortalSpawnAllowed()) return;
		
		// ... then block the spawn event.
		event.setCancelled(true);
	}
	
}
