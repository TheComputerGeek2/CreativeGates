package com.massivecraft.creativegates.engine;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.massivecore.Engine;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashSet;
import java.util.Set;

public class EngineDestroyGate extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EngineDestroyGate i = new EngineDestroyGate();
	public static EngineDestroyGate get() { return i; }
	
	// -------------------------------------------- //
	// LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockBreakEvent event)
	{
		destroyGate(event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(EntityChangeBlockEvent event)
	{
		destroyGate(event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(EntityExplodeEvent event)
	{
		for (Block block : event.blockList())
		{
			destroyGate(block);
		}
	}
	
	// This one looks weird since it needs to handle beds as well
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockPistonExtendEvent event)
	{
		Set<Block> blocks = new HashSet<Block>();
		
		Block piston = event.getBlock();
		Block extension = piston.getRelative(event.getDirection());
		blocks.add(extension);
		
		for (Block block : event.getBlocks())
		{
			blocks.add(block);
			blocks.add(block.getRelative(event.getDirection()));
		}
		
		for (Block block : blocks)
		{
			destroyGate(block);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockPistonRetractEvent event)
	{
		destroyGate(event.getBlock().getRelative(event.getDirection(), 1));
		if (event.isSticky())
		{
			destroyGate(event.getBlock().getRelative(event.getDirection(), 2));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockFadeEvent event)
	{
		destroyGate(event.getBlock());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void destroyGate(BlockBurnEvent event)
	{
		destroyGate(event.getBlock());
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static void destroyGate(Block block)
	{
		UGate ugate = UGate.get(block);
		if (ugate == null) return;
		ugate.destroy();
	}
	
}
