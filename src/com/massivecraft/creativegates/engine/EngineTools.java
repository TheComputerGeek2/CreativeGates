package com.massivecraft.creativegates.engine;

import com.massivecraft.creativegates.entity.MLang;
import com.massivecraft.creativegates.util.FloodUtil;
import com.massivecraft.creativegates.GateOrientation;
import com.massivecraft.creativegates.util.GateUtil;
import com.massivecraft.creativegates.util.MaterialCountUtil;
import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.creativegates.entity.UGateColls;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EngineTools extends Engine
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static EngineTools i = new EngineTools();
	public static EngineTools get() { return i; }
	
	// -------------------------------------------- //
	// LISTENERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void tools(PlayerInteractEvent event)
	{
		// If a player ...
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		
		// ... is clicking a block ...
		final Block clickedBlock = event.getClickedBlock();
		if (clickedBlock == null) return;
		
		// ... and gates are enabled here ...
		UConf uconf = UConf.get(clickedBlock);
		if (!uconf.isEnabled()) return;
		
		// ... and the item in hand ...
		final ItemStack currentItem = event.getItem();
		if (InventoryUtil.isNothing(currentItem)) return;
		final Material material = currentItem.getType();
		
		// ... is in any way an interesting material ...
		if (!isTool(uconf, material)) return;
		
		// ... then find the current gate ...
		final UGate currentGate = UGate.get(clickedBlock);
		
		String message = null;
		
		// ... and if ...
		if (material == uconf.getMaterialCreate())
		{
			// ... we are trying to create ...
			
			// ... check permission node ...
			if (!Perm.CREATE.has(player, uconf.isToolCreatePermissionVerbose())) return;
			
			// ... check if the place is occupied ...
			if (currentGate != null)
			{
				message = Txt.parse(MLang.get().gateAlreadyThere);
				player.sendMessage(message);
				return;
			}
			
			// ... check if the item is named ...
			ItemMeta currentItemMeta = currentItem.getItemMeta();
			if (!currentItemMeta.hasDisplayName())
			{
				message = Txt.parse(MLang.get().noDisplayName, Txt.getMaterialName(material));
				player.sendMessage(message);
				return;
			}
			String newNetworkId = ChatColor.stripColor(currentItemMeta.getDisplayName());
			
			// ... perform the flood fill ...
			Block startBlock = clickedBlock.getRelative(event.getBlockFace());
			Map.Entry<GateOrientation, Set<Block>> gateFloodInfo = FloodUtil.getGateFloodInfo(startBlock);
			if (gateFloodInfo == null)
			{
				message = Txt.parse(MLang.get().cantResolveFrame, Txt.getMaterialName(material));
				player.sendMessage(message);
				return;
			}
			
			GateOrientation gateOrientation = gateFloodInfo.getKey();
			Set<Block> blocks = gateFloodInfo.getValue();
			
			// ... ensure the required blocks are present ...
			Map<Material, Integer> materialCounts = MaterialCountUtil.count(blocks);
			if (!MaterialCountUtil.has(materialCounts, uconf.getBlocksrequired()))
			{
				message = Txt.parse(MLang.get().frameNeeds, MaterialCountUtil.desc(uconf.getBlocksrequired()));
				player.sendMessage(message);
				return;
			}
			
			// ... calculate the exit location ...
			PS exit = PS.valueOf(player.getLocation());
			exit = exit.withPitch(0F);
			exit = exit.withYaw(gateOrientation.getExitYaw(exit, PS.valueOf(blocks.iterator().next())));
			
			// ... calculate the coords ...
			Set<PS> coords = new HashSet<PS>();
			for (Block block : blocks)
			{
				coords.add(PS.valueOf(block).withWorld(null));
			}
			
			// ... create the gate ...
			UGate newGate = UGateColls.get().get(startBlock).create();
			newGate.setCreatorId(IdUtil.getId(player));
			newGate.setNetworkId(newNetworkId);
			newGate.setExit(exit);
			newGate.setCoords(coords);
			
			// ... set the air blocks to portal material ...
			newGate.fill();
			
			// ... run fx ...
			newGate.fxKitCreate(player);
			
			// ... fx-inform the player ...
			message = Txt.parse(MLang.get().gateFormed, newNetworkId);
			player.sendMessage(message);
			
			// ... item cost ...
			if (uconf.isRemovingCreateToolItem())
			{
				// ... remove one item amount...
				
				// (decrease count in hand)
				ItemStack newItem = new ItemStack(currentItem);
				newItem.setAmount(newItem.getAmount() - 1);
				InventoryUtil.setWeapon(player, newItem);
				
				// (message)
				message = Txt.parse(MLang.get().toolUsedItemDisappear, Txt.getMaterialName(material));
				player.sendMessage(message);
			}
			else if (uconf.isRemovingCreateToolName())
			{
				// ... just remove the item name ...
				
				// (decrease count in hand)
				ItemStack newItemNamed = new ItemStack(currentItem);
				newItemNamed.setAmount(newItemNamed.getAmount() - 1);
				InventoryUtil.setWeapon(player, newItemNamed);
				
				// (add one unnamed)
				ItemStack newItemUnnamed = new ItemStack(currentItem);
				ItemMeta newItemUnnamedMeta = newItemUnnamed.getItemMeta();
				newItemUnnamedMeta.setDisplayName(null);
				newItemUnnamed.setItemMeta(newItemUnnamedMeta);
				newItemUnnamed.setAmount(1);
				player.getInventory().addItem(newItemUnnamed);
				
				// Update soon
				InventoryUtil.updateSoon(player);
				
				// (message)
				message = Txt.parse(MLang.get().toolUsedItemNameRemoved, Txt.getMaterialName(material));
				player.sendMessage(message);
			}
		}
		else
		{
			// ... we are trying to do something else that create ...
			
			// ... and there is a gate ...
			if (currentGate == null)
			{
				// ... and there is no gate ...
				if (GateUtil.isGateNearby(clickedBlock))
				{
					// ... but there is portal nearby.
					
					// ... exit with a message.
					player.sendMessage(Txt.parse(MLang.get().toolUseButNoGate, Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType())));
					return;
				}
				else
				{
					// ... and there is no portal nearby ...
					
					// ... exit quietly.
					return;
				}
			}
			
			// ... and we are not using water ...
			if (!uconf.isUsingWater())
			{
				// ... update the portal orientation
				currentGate.fill();
			}
			
			// ... send use action description ...
			message = Txt.parse(MLang.get().useXOnY, Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType()));
			player.sendMessage(message);
			
			// ... check restriction ...
			if (currentGate.isRestricted())
			{
				if (currentGate.isCreator(player))
				{
					message = Txt.parse(MLang.get().restrictedCreator);
					player.sendMessage(message);
				}
				else
				{
					message = Txt.parse(MLang.get().restrictedNotCreator);
					player.sendMessage(message);
					return;
				}
			}
			
			if (material == uconf.getMaterialInspect())
			{
				// ... we are trying to inspect ...
				message = Txt.parse(MLang.get().infoRevealed);
				player.sendMessage(message);
				
				message = Txt.parse(MLang.get().infoNetwork, currentGate.getNetworkId());
				player.sendMessage(message);
				
				message = Txt.parse(MLang.get().infoGates, currentGate.getGateChain().size());
				player.sendMessage(message);
			}
			else if (material == uconf.getMaterialSecret())
			{
				// ... we are trying to change secret state ...
				
				boolean creator = currentGate.isCreator(player);
				if (creator)
				{
					boolean secret = !currentGate.isRestricted();
					currentGate.setRestricted(secret);
					
					message = (secret ? Txt.parse(MLang.get().gateSecretOn) : Txt.parse(MLang.get().gateSecretOff));
					player.sendMessage(message);
				}
				else
				{
					message = Txt.parse(MLang.get().onlyCreatorCanChangeSecret, Txt.getMaterialName(material), Txt.getMaterialName(clickedBlock.getType()));
					player.sendMessage(message);
				}
			}
			else if (material == uconf.getMaterialMode())
			{
				// ... we are trying to change mode ...
				
				currentGate.toggleMode();
				
				String enter = currentGate.isEnterEnabled() ? Txt.parse(MLang.get().gateModeChangedEnterEnabled) : Txt.parse(MLang.get().gateModeChangedEnterDisabled);
				String exit = currentGate.isExitEnabled() ? Txt.parse(MLang.get().gateModeChangedExitEnabled) : Txt.parse(MLang.get().gateModeChangedExitDisabled);
				
				message = Txt.parse(MLang.get().gateModeChanged, enter, exit);
				player.sendMessage(message);
			}
		}
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	private static boolean isTool(UConf conf, Material material)
	{
		if (material == conf.getMaterialInspect()) return true;
		if (material == conf.getMaterialMode()) return true;
		if (material == conf.getMaterialSecret()) return true;
		if (material == conf.getMaterialCreate()) return true;
		return false;
	}
	
}
