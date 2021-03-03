package fr.doritanh.olurwa.tablist.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.doritanh.olurwa.tablist.TabList;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e) {
		TabList.get().sendHeaderFooter(e.getPlayer());
		TabList.get().updateLobby();

		for (Player p : Bukkit.getOnlinePlayers()) {
			TabList.get().send(p);
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				TabList.get().requestUpdateServers();

			}
		}.runTaskLater(TabList.get(), 20);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				TabList.get().updateLobby();

			}
		}.runTaskLater(TabList.get(), 20);

		for (Player p : Bukkit.getOnlinePlayers()) {
			TabList.get().send(p);
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				TabList.get().requestUpdateServers();

			}
		}.runTaskLater(TabList.get(), 20);
	}

}
