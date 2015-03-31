package subside.plugins.spectatorwarp;

import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpectatorWarp extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new EventListener(), this);
	}

	@Override
	public void onDisable() {
		Set<Entry<Player, PlayerData>> set = EventListener.players.entrySet();
		for (Entry<Player, PlayerData> entry : set) {
			entry.getValue().teleportBack();
			entry.getKey().sendMessage(ChatColor.DARK_AQUA+"You have been forced out of spectator mode!");
		}
	}
}
