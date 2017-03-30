package com.massivecraft.creativegates.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.EnumSet;
import java.util.Set;

public class VoidUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static Set<Material> VOID_MATERIALS = EnumSet.of(Material.AIR);
	
	// -------------------------------------------- //
	// CHECKS
	// -------------------------------------------- //
	
	public static boolean isVoid(Material material)
	{
		return VOID_MATERIALS.contains(material);
	}
	
	public static boolean isntVoid(Material material)
	{
		return !isVoid(material);
	}
	
	public static boolean isVoid(Block block)
	{
		return isVoid(block.getType());
	}
	
	public static boolean isntVoid(Block block)
	{
		return !isVoid(block);
	}
	
}
