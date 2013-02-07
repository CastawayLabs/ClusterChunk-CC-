package com.CC.Commands.Party;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.CC.General.onStartup;

public class Remove {
	
private onStartup plugin;
    
    public Remove(onStartup p)
    {
        this.plugin = p;
    }
    
    public void removePlayer(Player from, Player removed){
    	if(plugin.getParties().getParty(from) != null){
    		if(plugin.getParties().getParty(removed) != null){
    			if(plugin.getParties().getParty(from).equals(plugin.getParties().getParty(removed))){
    				plugin.getParties().getParty(from).removeMember(from, removed);
    				
    			}else{
    				from.sendMessage(ChatColor.DARK_RED + removed.getName() + ChatColor.RED + " is not in your party");
    			}
    		}else{
    			from.sendMessage(ChatColor.DARK_RED + removed.getName() + ChatColor.RED + " is not in your party");
    		}
    	}else{
    		from.sendMessage(ChatColor.RED + "You are currently not in any parties");
    	}
    }

}
