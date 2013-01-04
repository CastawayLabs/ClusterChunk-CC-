package com.CC.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.CC.Arenas.GameManager;
import com.CC.Commands.Staff.EndGame;
import com.CC.General.onStartup;
import com.CC.Messages.PlayerMessages;

public class StaffCommands implements CommandExecutor 
{
	private final EndGame endgame;
	private final onStartup plugin;
	private final GameManager gamemanager;
	private final PlayerMessages messages;
	
	public StaffCommands(onStartup instance)
	{
		plugin = instance;
		endgame = new EndGame(plugin);
		gamemanager  = plugin.getGameManager();
		messages = plugin.getMessages();
	}
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Sorry but this command can only be used in game");
			return false;
		}
		Player player = (Player)sender;
		if(!player.hasPermission("ClusterChunk.Admin")){
			player.sendMessage(messages.noPermissionCommand(player));
			return false;
		}
		
		if(string.equalsIgnoreCase("endgame"))
		{
			if(args.length == 0)
			{
				player.sendMessage(ChatColor.GREEN + "Correct Usage: /endgame <Game> <Reason> || /endgame <Game> <Reason>");
				return false;
			}
			else if(args.length == 1)
			{
				if(gamemanager.isGame(args[0]))
				{
					return endgame.endGame(player, gamemanager.getGame(args[0]), "No specified reason");
					
				}
				else
				{
					player.sendMessage(ChatColor.RED + "The game you have specified does not exist");
					return false;
				}
			}
			else if(args.length >= 2)
			{
				System.out.println(glue(args, " "));
				if(gamemanager.isGame(args[0]))
				{
					return endgame.endGame(player, gamemanager.getGame(args[0]), glue(args, " "));
					
				}
				else
				{
					player.sendMessage(ChatColor.RED + "The game you have specified does not exist");
					return false;
				}
				
			}
		}
		return false;
		
	}
	
	public String glue(String[] array, String glue) {
		String glued = "";
	for(int i = 1; i < array.length;i++) {
		glued += array[i];
	if(i != array.length-1)
		glued += glue;
		}
		return glued;
		}

}
