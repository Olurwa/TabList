package fr.doritanh.olurwa.tablist.manager;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import net.minecraft.server.v1_16_R3.EntityPlayer;

public class ScoreboardTab {

	private final Scoreboard sb;
	private final TeamTab lobby;
	private final TeamTab creative;
	private final TeamTab survival;
	private final TeamTab others;

	public ScoreboardTab() {
		this.sb = Bukkit.getScoreboardManager().getNewScoreboard();
		this.lobby = new TeamTab(sb, 0, "§lLobby");
		this.creative = new TeamTab(sb, 1, "§lCréatif");
		this.survival = new TeamTab(sb, 2, "§lSurvie");
		this.others = new TeamTab(sb, 3, "§lAutre");
	}

	public Scoreboard getScoreboard() {
		return this.sb;
	}

	public ArrayList<EntityPlayerTab> getPlayers() {
		ArrayList<EntityPlayerTab> ept = new ArrayList<EntityPlayerTab>();
		ept.addAll(this.lobby.getPlayers());
		ept.addAll(this.creative.getPlayers());
		ept.addAll(this.survival.getPlayers());
		ept.addAll(this.others.getPlayers());
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
		removed.addAll(this.lobby.getRemoved());
		removed.addAll(this.creative.getRemoved());
		removed.addAll(this.survival.getRemoved());
		removed.addAll(this.others.getRemoved());
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

	public void updateLobby() {
		this.lobby.clear();

		for (Player p : Bukkit.getOnlinePlayers()) {
			this.lobby.add(new EntityPlayerTab(p));
		}
	}

	public void updateCreative(String[] playersNames) {
		this.creative.clear();

		for (String name : playersNames) {
			if (name.equalsIgnoreCase(""))
				continue;
			this.creative.add(new EntityPlayerTab(name, name));
		}
	}

}
