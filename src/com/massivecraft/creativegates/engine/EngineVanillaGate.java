package com.massivecraft.creativegates.engine;

import com.massivecraft.creativegates.util.GateUtil;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class EngineVanillaGate extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EngineVanillaGate i = new EngineVanillaGate();
	public static EngineVanillaGate get() { return i; }
	
	// -------------------------------------------- //
	// LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void disableVanillaGates(PlayerPortalEvent event)
	{
		disableVanillaGates(event.getFrom(), event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void disableVanillaGates(EntityPortalEvent event)
	{
		disableVanillaGates(event.getFrom(), event);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static void disableVanillaGates(Location location, Cancellable cancellable)
	{
		if (GateUtil.isGateNearby(location.getBlock()))
		{
			cancellable.setCancelled(true);
		}
	}
}
