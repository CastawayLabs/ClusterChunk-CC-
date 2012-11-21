package com.CC.Commands.Party;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.CC.General.onStartup;

public class Help 
{
	private onStartup plugin;
    
    public Help(onStartup p)
    {
        this.plugin = p;
    }

	public void help(Player player){
		
		ChatColor black = ChatColor.BLACK;
		ChatColor dgray = ChatColor.DARK_GRAY;
		ChatColor purple = ChatColor.DARK_PURPLE;
		ChatColor aqua = ChatColor.AQUA;
		ChatColor daqua = ChatColor.DARK_AQUA;
		ChatColor dred = ChatColor.DARK_RED;
		ChatColor red = ChatColor.RED;
		ChatColor gold = ChatColor.GOLD;
		ChatColor gray = ChatColor.GRAY;
		
		player.sendMessage("");
		player.sendMessage(dgray + "===========" + dred + "[" + daqua + "Party Commands" + dred + "]" + dgray + "===========");
		player.sendMessage("");
		player.sendMessage("/Party Create <party name>" + " - " + "Create a party with the specified name");
		player.sendMessage("/Party Help" + " - " + "Take a guess <3");
		player.sendMessage("/Party Invite <player name>" + " - " + "Invite a player to your closed party");
		player.sendMessage("/Party Join" + " - " + "Join a party you were invited to");
		player.sendMessage("/Party Leave" + " - " + "Leave the party you are currently in");
		player.sendMessage("/Party Start <red/blue>" + " - " + "Start a match with your party on red or blue team");
		player.sendMessage("/Party Status" + " - " + "Get the status of your party");
		player.sendMessage("");
		player.sendMessage(dgray + "===========" + dred + "[" + daqua + "Party Commands" + dred + "]" + dgray + "===========");
		player.sendMessage("");
		
	}
}
