package com.massivecraft.creativegates;

import com.massivecraft.creativegates.cmd.CmdCg;
import com.massivecraft.creativegates.cmd.type.TypePermissionDefault;
import com.massivecraft.creativegates.engine.EngineDestroyGate;
import com.massivecraft.creativegates.engine.EnginePigmenSpawn;
import com.massivecraft.creativegates.engine.EngineStabilize;
import com.massivecraft.creativegates.engine.EngineTools;
import com.massivecraft.creativegates.engine.EngineUseGate;
import com.massivecraft.creativegates.engine.EngineVanillaGate;
import com.massivecraft.creativegates.entity.MConf;
import com.massivecraft.creativegates.entity.MConfColl;
import com.massivecraft.creativegates.entity.MLangColl;
import com.massivecraft.creativegates.entity.UConfColls;
import com.massivecraft.creativegates.entity.UGateColls;
import com.massivecraft.creativegates.index.IndexCombined;
import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.type.RegistryType;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;

public class CreativeGates extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static CreativeGates i;
	public static CreativeGates get() { return i; }
	public CreativeGates() { CreativeGates.i = this; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// Aspects
	private Aspect aspect;
	public Aspect getAspect() { return this.aspect; }
	public Multiverse getMultiverse() { return this.getAspect().getMultiverse(); }
	
	// Index
	private final IndexCombined index = new IndexCombined();
	public IndexCombined getIndex() { return this.index; }
	
	// Filling
	private boolean filling = false;
	public boolean isFilling() { return this.filling; }
	public void setFilling(boolean filling) { this.filling = filling; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onEnableInner()
	{
		// Initialize Aspects
		this.aspect = AspectColl.get().get(Const.ASPECT, true);
		this.aspect.register();
		this.aspect.setDesc(
			"<i>What gates do exist.",
			"<i>What the config options are set to."
		);

		// Index
		this.getIndex().clear();
		
		// Types
		RegistryType.register(PermissionDefault.class, TypePermissionDefault.get());
		
		// Activate
		this.activate(
			// Coll
			MConfColl.class,
			MLangColl.class,
			UConfColls.class,
			UGateColls.class,
		
			// Engine
			EngineTools.class,
			EngineStabilize.class,
			EngineVanillaGate.class,
			EnginePigmenSpawn.class,
			EngineDestroyGate.class,
			EngineUseGate.class,
			
			// Command
			CmdCg.class
		);
	
		// Schedule a permission update.
		// Possibly it will be useful due to the way Bukkit loads permissions.
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run()
			{
				MConf.get().updatePerms();
			}
		});
	}
	
	@Override
	public void onDisable()
	{
		this.getIndex().clear();
		super.onDisable();
	}
	
}
