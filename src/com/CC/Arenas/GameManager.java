package com.CC.Arenas;

import com.CC.General.onStartup;
import com.CC.WorldGeneration.WorldGeneration;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import static org.bukkit.ChatColor.*;
import org.bukkit.entity.Player;

public class GameManager
{

    private HashMap<String, String> players;
    private HashMap<String, Game> games;
    private onStartup plugin;
    private WorldGeneration worldgen;

    public GameManager(onStartup instance)
    {
    	plugin = instance;
    	
    	//worldgen = plugin.getWorldGenerator();
        this.games = new HashMap<String, Game>();
        this.players =  new HashMap<String, String>();
    }
    
    public void setWorldGenerator(WorldGeneration world){
    	worldgen = world;
    }
    
    public Game getGame(String name)
    {
        return this.games.get(name);
    }
    public boolean isGame(String name)
    {
        return this.games.containsKey(name);
    }
    
    public boolean createGame(String name)
    {
        if(this.games.containsKey(name))
        {
            return false;
        }
        Game g = new Game(name, plugin);
        this.games.put(name, g);
        return true;
    }
    
    public void createMap(String name, Game g){
    	worldgen.newMap(name, g);
    }
    
    public boolean removeGame(String name)
    {
        if(!this.games.containsKey(name))
        {
            return false;
        }
        this.games.remove(name);
        return true;
    }
    
    public Game getGameByPlayer(Player p)
    {
        return getGameByPlayer(p.getName());
    }
    
    /*
    *   Can return null!
    **/
    public Game getGameByPlayer(String pname)
    {
        if(players.containsKey(pname))
        {
            return games.get(players.get(pname));
        }
        return null;
    }
    
    public boolean isInGame(Player peter){
    	if(players.containsKey(peter.getName())) 
        {
            if(games.containsKey(players.get(peter.getName())))
            {
    		return true;
            }
    	}
        return false;
    }
    
    public boolean isInGame(String string){
    	if(players.containsKey(string))
        { 
            if(games.containsKey(players.get(string)))
            {
    		return true;
            }
    	}
        return false;
    }
    
    private boolean endGame(Game game, Team team){
    	for(String p : game.getPlayers()){
    		Player player = Bukkit.getPlayer(p);
    		//Put in method to teleport to lobby world 
    	}
    	if(team.equals(Team.BLUE)){
    		teleportToSpawn(game.getBlueTeamPlayers());
    		teleportToSpawn(game.getRedTeamPlayers());
    		game.sendMessageRed(ChatColor.RED + "Sorry you have lost");
    		game.sendMessageBlue(ChatColor.GREEN + "Congratulations! You have won");
    		for(Player p : game.getBlueTeamPlayers()){
    			game.removePlayer(p);
    		}
    		for(Player p : game.getRedTeamPlayers()){
    			game.removePlayer(p);
    		}
    		
    		return true;
    	}else if(team.equals(Team.RED)){
    		teleportToSpawn(game.getBlueTeamPlayers());
    		teleportToSpawn(game.getRedTeamPlayers());
    		game.sendMessageBlue(ChatColor.RED + "Sorry you have lost");
    		game.sendMessageRed(ChatColor.GREEN + "Congratulations! You have won");
    		for(Player p : game.getBlueTeamPlayers()){
    			game.removePlayer(p);
    		}
    		for(Player p : game.getRedTeamPlayers()){
    			game.removePlayer(p);
    		}
    		return true;
    		
    	}else{
    		teleportToSpawn(game.getBlueTeamPlayers());
    		teleportToSpawn(game.getRedTeamPlayers());
    		game.sendMessageAll(ChatColor.GRAY + "The game has ended in a draw");
    		for(Player p : game.getBlueTeamPlayers()){
    			game.removePlayer(p);
    		}
    		for(Player p : game.getRedTeamPlayers()){
    			game.removePlayer(p);
    		}
    		return true;
    	}
    }
    
    private boolean endGame(Game game, String reason){
    	
    		teleportToSpawn(game.getBlueTeamPlayers());
    		teleportToSpawn(game.getRedTeamPlayers());
    		for(Player p : game.getBlueTeamPlayers()){
    			game.removePlayer(p);
    			p.sendMessage(new StringBuilder("Game has been ended by an administrator for the following reason: ").append(reason).toString());
    		}
    		
    		for(Player p : game.getRedTeamPlayers()){
    			game.removePlayer(p);
    			p.sendMessage(new StringBuilder("Game has been ended by an administrator for the following reason: ").append(reason).toString());
    		}
    		return true;
    		
    	}
    
    
    
    
    //Also regenerates arena :D 
    public boolean endGame(String gameName, Team winningTeam){
    	if(!games.containsKey(gameName)) return false;
    		if(endGame(getGame(gameName), winningTeam)){
    			Bukkit.getServer().broadcastMessage(new StringBuilder(GRAY.toString()).append(gameName).append(gameName).append(GREEN).append(" is being regenerated. You may experience lag").toString());
    			worldgen.newMap(gameName, getGame(gameName));
    			return true;
    		}else{
    			return false;
    	}
    }
    
    public boolean endGame(String gameName, String reason){
    	if(!games.containsKey(gameName)) return false;
    		if(endGame(getGame(gameName), reason)){
    			Bukkit.getServer().broadcastMessage(new StringBuilder(GRAY.toString()).append(gameName).append(GREEN).append(" is being regenerated. You may experience lag").toString());
    			worldgen.newMap(gameName, getGame(gameName));
    			return true;
    		}else{
    			return false;
    	}
    }
    
    public ArrayList<Game> getOpenGames(){
    	ArrayList<Game> opengames = new ArrayList<Game>();
    	for(Game g : games.values()){
    		if(g.getPlayers().isEmpty()){
    			if(g.regenerated){
    				opengames.add(g);
    				g.setRegenerated(false);
    			}
    		}
    	}
    	return opengames;
    }
    
    public HashMap<String, Game> getGames(){
    	return games;
    }
    
    public void playerJoinGame(String string, String name){
    	players.put(string, name);
    }
    
    public void removePlayerFromGame(String string){
    	players.remove(string);
    }
    
    public void startGameCount(Game game){ 
    	game.startGameCountdown();
    }
    
    public void teleportToSpawn(ArrayList<Player> players){
    	for(Player p : players){
    		p.teleport(new Location(Bukkit.getWorld("lobby"), 330, 231, 332));
    	}
    }
    
}