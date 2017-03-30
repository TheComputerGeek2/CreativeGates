package com.massivecraft.creativegates.cmd.type;

import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionDefault;

public class TypePermissionDefault extends TypeEnum<PermissionDefault>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypePermissionDefault i = new TypePermissionDefault();
	public static TypePermissionDefault get() { return i; }
	public TypePermissionDefault()
	{
		super(PermissionDefault.class);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ChatColor getVisualColor(PermissionDefault value)
	{
		switch (value) {
			case TRUE:
				return ChatColor.GREEN;
			case FALSE:
				return ChatColor.RED;
			case OP:
				return ChatColor.AQUA;
			case NOT_OP:
				return ChatColor.DARK_GREEN;
			default:
				return COLOR_DEFAULT;
		}
	}
	
}
