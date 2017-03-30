package com.massivecraft.creativegates.entity;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

@EditorName("CreativeGates Language")
public class MLang extends Entity<MLang>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MLang i;
	public static MLang get() { return i; }
	
	@Override
	public MLang load(MLang that)
	{
		super.load(that);
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public String gateAlreadyThere = "<b>There is no room for a new gate since there already is one here.";
	public String noDisplayName = "<b>You must name the %s before creating a gate with it.";
	
	public String cantResolveFrame = "<b>There is no frame for the gate, or it's to big.";
	public String frameNeeds = "<b>The frame must contain %s<b>.";
	public String gateFormed = "<g>A \"<h>%s<g>\" gate takes form in front of you.";
	
	public String toolUsedItemDisappear = "<i>The %s disappears.";
	public String toolUsedItemNameRemoved = "<i>The %s seems to have lost it's power.";
	public String toolUseButNoGate = "<i>You use the %s on the %s but there seem to be no gate.";
	public String useXOnY = "<i>You use the %s on the %s...";
	
	public String restrictedCreator = "<i>... the gate is restricted but you are the creator ...";
	public String restrictedNotCreator = "<b>... the gate is restricted and you are not the creator.";
	
	public String infoRevealed = "<i>Some gate inscriptions are revealed:";
	public String infoNetwork = "<k>network: <v>%s";
	public String infoGates = "<k>gates: <v>%d";
	
	public String gateSecretOn = "<h>Only you <i>can read the gate inscriptions now.";
	public String gateSecretOff = "<h>Anyone <i>can read the gate inscriptions now.";
	public String onlyCreatorCanChangeSecret = "<i>It seems <h>only the gate creator <i>can change inscription readability.";
	
	public String gateModeChangedEnterEnabled = "<g>enter enabled";
	public String gateModeChangedEnterDisabled = "<b>enter disabled";
	public String gateModeChangedExitEnabled = "<g>exit enabled";
	public String gateModeChangedExitDisabled = "<b>exit disabled";
	public String gateModeChanged = "<i>The gate now has %s <i>and %s<i>.";
	
	public String gateUseEnterDisabled = "<i>This gate has enter disabled.";
	
	public String gateDoesntLeadAnywhere = "<i>This gate does not seem to lead anywhere.";
	public String gateDestinationDescription = "the gate destination";
	
}
