package subside.plugins.spectatorwarp;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerData {
	private Player player;
	private GameMode gamemode;
	private Location location;
	public PlayerData(Player player, GameMode gamemode, Location location){
		this.player = player;
		this.gamemode = gamemode;
		this.location = location;
	}
	
	public void teleportBack(){
		player.setGameMode(gamemode);
		player.teleport(location);
	}
}
