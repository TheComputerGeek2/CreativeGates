package com.massivecraft.creativegates.engine;

import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.MLang;
import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class EngineUseGate extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EngineUseGate i = new EngineUseGate();
	public static EngineUseGate get() { return i; }
	
	// -------------------------------------------- //
	// LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void useGate(PlayerMoveEvent event)
	{
		// If a player ...
		Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		// ... is moving from one block to another ...
		if (MUtil.isSameBlock(event)) return;
		
		// ... and there is a gate in the new block ...
		UGate ugate = UGate.get(event.getTo());
		if (ugate == null) return;
		
		// ... and if the gate is intact ...
		if (!ugate.isIntact())
		{
			// We try to detect that a gate was destroyed once it happens by listening to a few events.
			// However there will always be cases we miss and by checking at use we catch those as well.
			// Examples could be map corruption or use of WorldEdit.
			ugate.destroy();
			return;
		}
		
		// ... and gates are enabled here ...
		UConf uconf = UConf.get(event.getTo());
		if (!uconf.isEnabled()) return;
		
		// ... and we have permission to use gates ...
		if (!Perm.USE.has(player, uconf.isUsePermissionVerbose())) return;
		
		// ... and the gate has enter enabled ...
		if (!ugate.isEnterEnabled())
		{
			String message = Txt.parse(MLang.get().gateUseEnterDisabled);
			player.sendMessage(message);
			return;
		}
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... then transport the player.
		ugate.transport(player);
	}
	
}
