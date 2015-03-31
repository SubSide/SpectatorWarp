package subside.plugins.spectatorwarp;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
	public static HashMap<Player, PlayerData> players = new HashMap<Player, PlayerData>();
	private static String title = "[WarpSpectate]";
	
	@EventHandler(ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event){
		if(event.getLine(0).toLowerCase().contains("[warpspectate]")){
			if(event.getPlayer().isOp()){
				if(!event.getLine(1).isEmpty()){
					event.setLine(2, ChatColor.DARK_BLUE+"Warp "+event.getLine(1).replaceAll("§[0-9a-zA-Z]", ""));
					event.setLine(1, ChatColor.AQUA+"Right click me");
					event.setLine(0, ChatColor.WHITE+title);
				}
				return;
			}
			event.setCancelled(false);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getClickedBlock() != null){
			if(event.getClickedBlock().getState() instanceof Sign){
				Sign sign = (Sign)event.getClickedBlock().getState();
				System.out.println(sign.getLine(0));
				if(sign.getLine(0).toLowerCase().contains(title.toLowerCase())){
					System.out.println(sign.getLine(2));
					if(sign.getLine(2).contains(" ")){
						if(!players.containsKey(event.getPlayer())){
							players.put(event.getPlayer(), new PlayerData(event.getPlayer(), event.getPlayer().getGameMode(), event.getPlayer().getLocation().clone()));
							event.getPlayer().setGameMode(GameMode.SPECTATOR);
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "warp "+sign.getLine(2).split(" ")[1]+" "+event.getPlayer().getName());
							event.getPlayer().sendMessage(ChatColor.DARK_AQUA+"To stop spectating do /spec leave");
						} else {
							event.getPlayer().sendMessage(ChatColor.DARK_AQUA+"You are already in spectator mode, use /spec leave to leave");
						}
					}
				}
			}
		}
		if(players.containsKey(event.getPlayer())){
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event){
		if(players.containsKey(event.getPlayer())){
			players.get(event.getPlayer()).teleportBack();
			players.remove(event.getPlayer());
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		if(players.containsKey(event.getPlayer())){
			if(event.getMessage().equalsIgnoreCase("/spec leave")){
				players.get(event.getPlayer()).teleportBack();
				players.remove(event.getPlayer());
				event.getPlayer().sendMessage(ChatColor.DARK_AQUA+"You have left spectator mode.");
				event.setCancelled(true);
				return;
			}
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.DARK_AQUA+"You can't execute commands in spectator mode! (/spec leave)");
		}
	}
}
