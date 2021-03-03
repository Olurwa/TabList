package fr.doritanh.olurwa.tablist.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import fr.doritanh.olurwa.tablist.TabList;

public class BungeeMessageListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("PlayerList")) {
			String serverName = in.readUTF();
			String[] playerList = in.readUTF().split(", ");
			TabList.get().updateExternal(serverName, playerList);
			for (Player p : Bukkit.getOnlinePlayers()) {
				TabList.get().send(p);
			}
		}
	}

}
