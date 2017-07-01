package com.marcsello.WalkFixer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class WalkFixer extends JavaPlugin implements Listener {
	
	private final float DefaultWalkSpeed = 0.2f; // https://bukkit.org/threads/default-walk-speed.131599/

	private void attemptFix(Player target) {
		
		// These two lines the actual fix
		// the rest of the code is not interesting
		target.setWalkSpeed(DefaultWalkSpeed); // repair walkspeed
		target.saveData(); // save this
		
	}
	
	private boolean isAffected(Player ply) { // is the player affected by the bug
		
		float wspd = ply.getWalkSpeed();
		
		if (this.getConfig().getBoolean("zero-bad-only")) {
			
			return wspd == 0.0f;
			
		} else {
			
			return wspd != DefaultWalkSpeed;
			
		}
		
	}
	
    @Override
    public void onEnable() {
    	
    	this.saveDefaultConfig(); // first time config save 
    	
    	if (this.getConfig().getBoolean("attempt-autofix")) { // bind only when needed 
    		getServer().getPluginManager().registerEvents(this, this);
    	}
    	
    }

    @Override
    public void onDisable() {
    	// don't we need to unbind the event handler? no..? okay.
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    
    	// Check if we got the arguments right	
    	
    	if (args.length > 1) { // only 0 (self) or 1 (other)
    		return false;
    	}
    	
    	// The next few sections might look overcomplicated, but we want to get permissions right for every version of the command
    	// i.e.: the player targeting itself by entering their name.
    	
    	// Figure out the target
    	
    	Player ply = null;
    	Boolean self_target = false;
    	
    	if (args.length == 0) {
    		
    		if (sender instanceof Player) {
    			ply = (Player) sender; // the actual target
    		} // Senior console targeting noone... this will lead to an error in the next step
    		
    		self_target = true; // no arguments = targeting itself
    		
    	} else { // one argument present
    		
    		ply = Bukkit.getPlayerExact(args[0]);
    		
	    	if (sender instanceof Player) {
	    		
	    		self_target = ((Player) sender == ply);
	    		
	    	} // otherwise it's probably the console, which can't target itself
	    	
    	}
    	
    	// Check if poor console wanna be a player
    	
    	if (self_target && (!(sender instanceof Player))) {
    		sender.sendMessage("Please specify a target"); // since the console don't have a name, we can safely assume that the command was invoked without arguments
    		return true;
    	}
    	
    	// Check for permissions (we do this before the player validity check!)
	    	
    	
    	if (self_target) {
    		
    		if (!sender.hasPermission("walkfixer.wfix.self")) {
    			sender.sendMessage("Insufficent permission"); // well.. they most probably used the command properly
        		return true;
    		}
    		
    	} else {
    		
    		if (!sender.hasPermission("walkfixer.wfix.others")) {
    			sender.sendMessage("Insufficent permission"); // well.. they most probably used the command properly
        		return true;    			
    		}    		
    		
    	}
    	    	
    	// Check for player validity
    	
    	if (ply == null) {
    		sender.sendMessage("There is no such player");
    		return true;
    	}
    	
    	//  If everything is right with the command we can do stuff
    	
    	
		if (!isAffected(ply)) { // the target is fine apparently
			
			if (sender.hasPermission("walkfixer.wfix.allow_good")) { // allowed to fix even if it looks good
	
				if (!self_target) { // otherwise they will receive the target message anyways
					sender.sendMessage("The walkspeed of " + ply.getName() + " seems fine, but fixing anyways");
				}
				
			
			} else { // not allowed to force-fix a good value
				
				if (self_target) {
					sender.sendMessage("Your walkspeed seems fine, no need for fixing");
				} else {
					sender.sendMessage("The walkspeed of " + ply.getName() + " seems fine, no need for fixing");
				}
				
				return true;
				
			}
			
			
		} else { // the target needs to be fixed
			
			if (!self_target) {
				sender.sendMessage("The walkspeed of " + ply.getName() + " is being fixed");
			} // otherwise receiving the target-message
			
		}
		
		
		// Notify the target about the action
		if (this.getConfig().getBoolean("tell-target") || self_target) {
			ply.sendMessage(this.getConfig().getString("target-message"));
		}
		
		
		// and now the actual fix
		
		attemptFix(ply);
	
    	return true;
    }
    
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) { // This won't run if the config is disabled this listener 
    	
		 Player ply = event.getPlayer();
		 float wspd = ply.getWalkSpeed();
		 
		 if (isAffected(ply)) {
			 getLogger().info(ply.getName() + " appears to have wrong walk speed values set (" + Float.toString(wspd) + "), attempting auto-fix!");
			 attemptFix(ply);
		 }
			 
	}

    
}
