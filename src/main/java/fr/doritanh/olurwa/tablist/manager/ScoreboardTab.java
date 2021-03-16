package fr.doritanh.olurwa.tablist.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import fr.doritanh.olurwa.tablist.TabList;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.EntityPlayer;

public class ScoreboardTab {

	private final String localServer;
	private final Scoreboard sb;
	private final HashMap<String, TeamTab> teams;

	public ScoreboardTab() {
		this.localServer = TabList.get().getServerName();
		this.sb = Bukkit.getScoreboardManager().getNewScoreboard();
		this.teams = new HashMap<String, TeamTab>();
		this.teams.put("lobby", new TeamTab(sb, 0, "§lLobby"));
		this.teams.put("creative", new TeamTab(sb, 1, "§lCréatif"));
		this.teams.put("survival", new TeamTab(sb, 2, "§lSurvie"));
		this.teams.put("others", new TeamTab(sb, 3, "§lAutre"));
	}

	public Scoreboard getScoreboard() {
		return this.sb;
	}

	public ArrayList<EntityPlayerTab> getPlayers() {
		ArrayList<EntityPlayerTab> ept = new ArrayList<EntityPlayerTab>();
		this.teams.forEach((name, team) -> {
			ept.addAll(team.getPlayers());
		});
		return ept;
	}

	public EntityPlayer[] getEntityPlayers() {
		ArrayList<EntityPlayerTab> ept = this.getPlayers();
		EntityPlayer[] teamPlayers = new EntityPlayer[ept.size()];
		for (int i = 0; i < ept.size(); i++) {
			teamPlayers[i] = ept.get(i).getPlayer();
		}
		return teamPlayers;
	}

	public ArrayList<EntityPlayerTab> getRemoved() {
		ArrayList<EntityPlayerTab> removed = new ArrayList<EntityPlayerTab>();
		this.teams.forEach((name, team) -> {
			removed.addAll(team.getRemoved());
		});
		return removed;
	}

	public EntityPlayer[] getEntityRemoved() {
		ArrayList<EntityPlayerTab> removed = this.getRemoved();
		EntityPlayer[] removedPlayers = new EntityPlayer[removed.size()];
		for (int i = 0; i < removed.size(); i++) {
			removedPlayers[i] = removed.get(i).getPlayer();
		}
		return removedPlayers;
	}

	@SuppressWarnings("deprecation") // Can't manage to translate &c to Component
	public void updateLocal() {
		this.teams.get(this.localServer).clear();

		for (Player p : Bukkit.getOnlinePlayers()) {
			User user = TabList.get().getLuckPerms().getPlayerAdapter(Player.class).getUser(p);
			String name = user.getCachedData().getMetaData().getPrefix() + ChatColor.WHITE + p.getName();
			name = ChatColor.translateAlternateColorCodes('&', name);
			p.playerListName(Component.text(name));
			this.teams.get(this.localServer).add(new EntityPlayerTab(p));
		}
	}

	public void updateExternal(String serverName, String[] playersNames) {
		if (!this.teams.containsKey(serverName))
			return;
		this.teams.get(serverName).clear();

		for (String name : playersNames) {
			if (name.equalsIgnoreCase(""))
				continue;
			this.teams.get(serverName).add(new EntityPlayerTab(name, name));
		}
	}

}
