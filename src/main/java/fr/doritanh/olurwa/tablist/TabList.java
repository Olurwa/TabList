package fr.doritanh.olurwa.tablist;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.doritanh.olurwa.tablist.listeners.BungeeMessageListener;
import fr.doritanh.olurwa.tablist.listeners.CoreMessageListener;
import fr.doritanh.olurwa.tablist.listeners.PlayerListener;
import fr.doritanh.olurwa.tablist.manager.ScoreboardTab;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class TabList extends JavaPlugin {

	private static TabList instance;

	private String header;
	private String footer;
	private String localServer;

	private ScoreboardTab sTab;
	private World world;

	public TabList() {
		instance = this;

		this.header = "§bHeader\\n ";
		this.footer = " \\n§cFooter";
		this.localServer = "lobby";
	}

	@Override
	public void onEnable() {
		// Config file
		this.saveDefaultConfig();
		this.header = this.getConfig().getString("header");
		this.footer = this.getConfig().getString("footer");
		this.localServer = this.getConfig().getString("server.local");

		// Register events
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

		// Register channels
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeMessageListener());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "olurwa:core");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "olurwa:core", new CoreMessageListener());

		// Save world
		for (World w : this.getServer().getWorlds()) {
			if (w.getEnvironment() == Environment.NORMAL) {
				this.world = w;
			}
		}

		// Register scoreboard
		this.sTab = new ScoreboardTab();
	}

	/**
	 * Get a TabList instance.
	 * 
	 * @return instance of TabList
	 */
	public static TabList get() {
		return instance;
	}

	/**
	 * Get the first normal world
	 * 
	 * @return A world
	 */
	public World getWorld() {
		return this.world;
	}

	public String getServerName() {
		return this.localServer;
	}

	/**
	 * Send the tablist header and footer to player.
	 * 
	 * @param p - The player that will receive the packet.
	 */
	public void sendHeaderFooter(Player p) {
		IChatBaseComponent titleHeader = ChatSerializer.a("{\"text\": \"" + this.header + "\"}");
		IChatBaseComponent titleFooter = ChatSerializer.a("{\"text\": \"" + this.footer + "\"}");
		PacketPlayOutPlayerListHeaderFooter pHeaderFooter = new PacketPlayOutPlayerListHeaderFooter();
		try {
			pHeaderFooter.header = titleHeader;
			pHeaderFooter.footer = titleFooter;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(pHeaderFooter);
		}
	}

	/**
	 * Ask bungeecord to send a player list for each servers
	 */
	public void requestUpdateServers() {
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		if (player == null)
			return;

		// Lobby
		ByteArrayDataOutput lobby = ByteStreams.newDataOutput();
		lobby.writeUTF("PlayerList");
		lobby.writeUTF("lobby");
		player.sendPluginMessage(this, "BungeeCord", lobby.toByteArray());
		// Creative
		ByteArrayDataOutput creative = ByteStreams.newDataOutput();
		creative.writeUTF("PlayerList");
		creative.writeUTF("creative");
		player.sendPluginMessage(this, "BungeeCord", creative.toByteArray());
		// Survival
		ByteArrayDataOutput survival = ByteStreams.newDataOutput();
		survival.writeUTF("PlayerList");
		survival.writeUTF("survival");
		player.sendPluginMessage(this, "BungeeCord", survival.toByteArray());
	}

	/**
	 * Update the local player list
	 */
	public void updateLocal() {
		this.sTab.updateLocal();
	}

	/**
	 * Update the creative player list
	 * 
	 * @param playersNames - List of player names
	 */
	public void updateExternal(String serverName, String[] playersNames) {
		if (serverName.equalsIgnoreCase(this.localServer))
			return;
		this.sTab.updateExternal(serverName, playersNames);
	}

	/**
	 * Send the player list
	 * 
	 * @param packetReceiver - The player who will receive the packet
	 */
	public void send(Player packetReceiver) {
		PlayerConnection pc = ((CraftPlayer) packetReceiver).getHandle().playerConnection;
		// Remove old players
		pc.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this.sTab.getEntityRemoved()));
		// Set scoreboard to the packetreceiver
		packetReceiver.setScoreboard(this.sTab.getScoreboard());
		// Get all players and send them
		pc.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this.sTab.getEntityPlayers()));
	}
}
