package com.massivecraft.creativegates.engine;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.creativegates.entity.UConfColls;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.creativegates.util.VoidUtil;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.MassiveCore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class EngineStabilize extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EngineStabilize i = new EngineStabilize();
	public static EngineStabilize get() { return i; }
	
	// -------------------------------------------- //
	// LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void stabilizePortalContent(BlockPhysicsEvent event)
	{
		// If a portal block is running physics ...
		Block block = event.getBlock();
		if (block.getType() != Material.PORTAL) return;
		
		// ... and we are filling or that block is stable according to our algorithm ...
		if (!(CreativeGates.get().isFilling() || isPortalBlockStable(block))) return;
		
		// ... then block the physics to stop the portal from disappearing.
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void stabilizePortalContent(BlockFromToEvent event)
	{
		UConf uconf = UConfColls.get().getForWorld(event.getBlock().getWorld().getName()).get(MassiveCore.INSTANCE);
		if ( ! uconf.isUsingWater()) return;
		if (UGate.get(event.getBlock()) == null && UGate.get(event.getToBlock()) == null) return;
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void stabilizePortalContent(BlockPlaceEvent event)
	{
		stabilizePortalContentBlock(event.getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void stabilizePortalContent(PlayerBucketFillEvent event)
	{
		stabilizePortalContentBlock(event.getBlockClicked(), event);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void stabilizePortalContent(PlayerBucketEmptyEvent event)
	{
		stabilizePortalContentBlock(event.getBlockClicked(), event);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static void stabilizePortalContentBlock(Block block, Cancellable cancellable)
	{
		if (UGate.get(block) == null) return;
		cancellable.setCancelled(true);
	}
	
	public static boolean isPortalBlockStable(Block block)
	{
		if (VoidUtil.isVoid(block.getRelative(+0, +1, +0))) return false;
		if (VoidUtil.isVoid(block.getRelative(+0, -1, +0))) return false;
		
		if (VoidUtil.isntVoid(block.getRelative(+1, +0, +0)) && VoidUtil.isntVoid(block.getRelative(-1, +0, +0))) return true;
		if (VoidUtil.isntVoid(block.getRelative(+0, +0, +1)) && VoidUtil.isntVoid(block.getRelative(+0, +0, -1))) return true;
		
		return false;
	}
	
}
