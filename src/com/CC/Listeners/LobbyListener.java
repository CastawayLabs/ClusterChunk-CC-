package com.CC.Listeners;

import com.CC.Arenas.Game;
import com.CC.Arenas.GameManager;
import com.CC.Arenas.Team;
import com.CC.General.User;
import com.CC.General.UserManager;
import com.CC.General.onStartup;
import java.util.ArrayList;
import java.util.HashMap;
import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


public class LobbyListener implements Listener, Runnable
{
	
	public static HashMap<Player, Team> quedplayers = new HashMap<Player, Team>();
	
	private onStartup plugin;
	private GameManager gamemanager;
	private UserManager usermanager;
    
    public LobbyListener(onStartup instance) {
    	plugin = instance;
    	plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin,  this, 0, 80);
    	gamemanager = plugin.getGameManager();
    	usermanager = plugin.getUserManager();
    	//System.out.println("LobbyListener created.");
	}
	
	@EventHandler
	public void onQueue(PlayerMoveEvent event)
    {
		//System.out.println("onQueue called.");
		Player player = event.getPlayer();
	if(gamemanager.isInGame(player)) return;
		

		if(player.getLocation().getWorld().getName().equalsIgnoreCase("lobby"))
        {
			//player.sendMessage("Step 1");
			if(quedplayers.containsKey(event.getPlayer()))
            {
				//player.sendMessage("Step 2");
				if(!onLobby(player))
                {
					//player.sendMessage("Step 3");
						quedplayers.remove(player);
						player.sendMessage(new StringBuilder(BOLD.toString()).append("You've been removed from the waiting list").toString());
						player.getInventory().setHelmet(null);
                }
            }
            else
            {
            	//player.sendMessage("Step 1");
                if(onLobby(player))
                {
                	//player.sendMessage("Step 2");
                	Block block = event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation().getBlockX(), event.getPlayer().getLocation().getBlockY() - 3, event.getPlayer().getLocation().getBlockZ());
                    if(block.getType() == Material.WOOL)
                    {
                    	//player.sendMessage("Step 3");
                    	byte red  = DyeColor.RED.getData();
                        byte blue = DyeColor.BLUE.getData();
                        if(block.getData() == blue)
                        {
                        	//player.sendMessage("Step 4");
                            player.sendMessage(new StringBuilder(BLUE.toString()).append("You have been added to the blue team waiting list").toString());
                            quedplayers.put(player, Team.BLUE);
                            player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
                            //System.out.println("" + quedplayers);
                        }
                        else if (block.getData() == red)
                        {
                        	if(quedplayers.containsKey(player)) return; //This didn't even work :P
                        	//player.sendMessage("Step 4");
                            player.sendMessage(new StringBuilder(RED.toString()).append("You have been added to the red team waiting list").toString());
                            quedplayers.put(player, Team.RED);
                            player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
                            //System.out.println("" + quedplayers);
                        }
                        else 
                        {
                        	if(randomTeam(player) == Team.BLUE){
                        		if(quedplayers.containsKey(player)) return;
                        		player.sendMessage(new StringBuilder(BLUE.toString()).append("You have been added to the blue team waiting list").toString());
                        		quedplayers.put(player, Team.BLUE);
                        		player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
                        		//System.out.println("1" + quedplayers);
                        	}
                        	else
                        	{
                        		if(quedplayers.containsKey(player)) return;
                        		player.sendMessage(new StringBuilder(RED.toString()).append("You have been added to the red team waiting list").toString());
                                quedplayers.put(player, Team.RED);
                                player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
                               // System.out.println("1" + quedplayers);
                        	}
                        	
                        }
                    }
                }
            }
        }
   }
	
	
	public boolean onLobby(Player player)
    {
		Block block = player.getWorld().getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 3, player.getLocation().getBlockZ());
		if(block.getType() == Material.WOOL)
        {
            byte red = DyeColor.RED.getData();
            byte blue = DyeColor.BLUE.getData();
            byte white = DyeColor.WHITE.getData();
            if(block.getData() == red || block.getData() == blue || block.getData() == white)
            {
                return true;
            }    
        }
        return false;
    }
    
    @EventHandler(priority=EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if(quedplayers.containsKey(player))
        {
            quedplayers.remove(player);
        }
    }
    
    
    /*@EventHandler
    public void noJump(PlayerMoveEvent event){
    	
    Player player = event.getPlayer();
   // player.sendMessage("Called!");
    if(quedplayers.containsKey(player)){
    	//player.sendMessage("1");
    	if(event.getFrom().getY() < event.getTo().getY()){
    				event.setCancelled(true);
    				player.getVelocity().setY(0);
    				//player.sendMessage("2");
    			}
    		}
    		
    	}*/
    
    private Team randomTeam(Player player)
    {
    		if(Math.random() >= .50)
    		{
    			return Team.RED;
    		}
    		else
    		{
    			return Team.BLUE;
    		}
    	
    	}
    
    /**
     * This will block the redstone lamps in the lobby world from
     * turning off :D 
     * 
     */
    
    @EventHandler
    public void lampAlwaysOn(BlockRedstoneEvent event){
    if(event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("lobby"))
    {
    	if (event.getBlock().getType() == Material.REDSTONE_LAMP_ON || event.getBlock().getType() == Material.REDSTONE_LAMP_OFF)
        {
    		event.setNewCurrent(100);
        }
    }	
  }
    
    private boolean countTeams(){
    	int blue = 0;
    	int red = 0;
    	for(Player p : quedplayers.keySet()){
    		if(quedplayers.get(p).equals(Team.BLUE)){
    			int newnumber = blue + 1;
    			blue = newnumber;
    		}else{
    			int newnumber = red + 1;
    			red = newnumber;
    		}
    	}
    	if(blue >= 1 && red >=1){
    		return true;
    	}else{
    		return false;
    	}
    
    }
    
    private ArrayList<Player> blueTeam(){
    	int i = 0;
    	ArrayList<Player> remove = new ArrayList<Player>();
    	ArrayList<Player> players = new ArrayList<Player>();
    	for(Player p : quedplayers.keySet()){
    		if(!(i >= 1)){
    			if(!quedplayers.get(p).equals(Team.RED)) 
    			{
    			players.add(p);
    			int newnumber = i + 1;
    			i = newnumber;
    			remove.add(p);
    			}
    		}
    	}
    	return players;
    }
    
    private ArrayList<Player> redTeam(){
    	int i = 0;
    	ArrayList<Player> remove = new ArrayList<Player>();
    	ArrayList<Player> players = new ArrayList<Player>();
    	for(Player p : quedplayers.keySet()){
    		if(!(i >= 1)){
    			if(!quedplayers.get(p).equals(Team.BLUE)) 
    			{
    			players.add(p);
    			int newnumber = i + 1;
    			i = newnumber;
    			remove.add(p);
    			}
    		}
    	}
    	return players;
    }
    
    private Game gameToJoin(){
    	
    	return gamemanager.getOpenGames().get(1);
    	
    		
    }
    
    private void removePlayers(ArrayList<Player> players){
    	for(Player p : players){
    		if(quedplayers.containsKey(p)){
    			if(onStartup.debugmode){
    				System.out.println("players removed!");
    			}
    			quedplayers.remove(p);
    		}
    	}
    }
    
//Currently this will not stop regenerating worlds till a certain amount because the players are not teleported
	public void run() {
	if(!countTeams()) return;
		if(gamemanager.getOpenGames().size() > 0 || gamemanager.getGames().keySet().size() < 20){
			if((redTeam() == null || blueTeam() == null) || (redTeam().size() == 0 || blueTeam().size() == 0)) return;
			if(gamemanager.getOpenGames().size() > 0 && gamemanager.getGames().keySet().size() < 20){
				Game game = gameToJoin();
				for(Player p : redTeam()){
					game.addRedPlayer(p.getName());
					quedplayers.remove(p);
					p.teleport(game.getRedSpawn(game.getName()));
					User player = usermanager.getUser(p);
					player.addTimeOnRed();
					player.changeLatestGame(game.getName());
					usermanager.updatePlayer(p, "stats");
				}
				for(Player p : blueTeam()){
					game.addBluePlayer(p.getName());
					quedplayers.remove(p);
					p.teleport(game.getBlueSpawn(game.getName()));
					User player = usermanager.getUser(p);
					player.addTimeOnBlue();
					player.changeLatestGame(game.getName());
					usermanager.updatePlayer(p, "stats");
				}
			}else if(gamemanager.getOpenGames().size() > 0 && !(gamemanager.getGames().keySet().size() < 20)){
				Game game = gameToJoin();
				for(Player p : redTeam()){
					game.addRedPlayer(p.getName());
					quedplayers.remove(p);
					p.teleport(game.getRedSpawn(game.getName()));
					User player = usermanager.getUser(p);
					player.addTimeOnRed();
					player.changeLatestGame(game.getName());
					usermanager.updatePlayer(p, "stats");
					//System.out.println("Adding " + p.getName() + " to red");
				}
				for(Player p : blueTeam()){
					game.addBluePlayer(p.getName());
					quedplayers.remove(p);
					p.teleport(game.getBlueSpawn(game.getName()));
					User player = usermanager.getUser(p);
					player.addTimeOnBlue();
					player.changeLatestGame(game.getName());
					usermanager.updatePlayer(p, "stats");
					//System.out.println("Adding " + p.getName() + " to blue");
				}
				//System.out.println(game.getPlayers());
				gamemanager.startGameCount(game);
				//If there are no open games and there aren't already 20 games
			}else if (gamemanager.getOpenGames().size() <= 0 && gamemanager.getGames().keySet().size() < 20){
				ArrayList<Player> redTeam = redTeam();
				ArrayList<Player> blueTeam = blueTeam();
				for(Player p : redTeam()){
					quedplayers.remove(p);
				}
				for(Player p : blueTeam()){
					quedplayers.remove(p);
				}
				int amount = gamemanager.getGames().size() + 1;
				String arenaName = "Arena" + amount;
				gamemanager.createGame(arenaName);
				Game game = gamemanager.getGame(arenaName);
				for(Player p : redTeam){
					game.addRedPlayer(p.getName());
					User player = usermanager.getUser(p);
					player.addTimeOnRed();
					player.changeLatestGame(game.getName());
					usermanager.updatePlayer(p, "stats");
					//System.out.println("Adding " + p.getName() + " to red");
				}
				for(Player p : blueTeam){
					User player = usermanager.getUser(p);
					player.addTimeOnBlue();
					game.addBluePlayer(p.getName());
					player.changeLatestGame(game.getName());
					usermanager.updatePlayer(p, "stats");
					//System.out.println("Adding " + p.getName() + " to blue");
				}
				gamemanager.createMap(arenaName, game);
				//System.out.println(game.getPlayers());
				
			}
		}
		
		
	}
}
	


