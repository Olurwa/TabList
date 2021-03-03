package fr.doritanh.olurwa.tablist.manager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.doritanh.olurwa.tablist.TabList;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;

public class EntityPlayerTab {
	public final static String baseTexture = "ewogICJ0aW1lc3RhbXAiIDogMTYx"
			+ "Mzk0NTE0NTgyNCwKICAicHJvZmlsZUlkIiA6ICJmYzUwMjkzYTVkMGI0NzV"
			+ "iYWYwNDJhNzIwMWJhMzBkMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTD"
			+ "E3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZ"
			+ "XMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8v"
			+ "dGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2RlM2ZiODA5MmI3ZWE"
			+ "xZmJlOTBhODE5NzVhYWVkYmE2NWVlZGUwNGZjNGY3ODg0MTk3ODY0ZGI3Yz"
			+ "ZmYTgxNyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
	public final static String baseSignature = "WkELIZgnBpj9LjubOPJKWvlnel"
			+ "rf85R8QA38H8aJN3r7i1fHdSZgC+5Zq9ej/ZMxv/2iWhJLdf05MpHTEIfbP"
			+ "iHlb4kV+kN0y3S7b0wdUwc6HqsETTclNneDLT0tDBm7IcqOwQw/IP2eRJF/"
			+ "xIPvuX3DzRkJUme6ygRAuS2YIEXSEd1gz8JzYAYvpPuNEfD0IL8lrZ1h5LP"
			+ "OEAtPUxnKbd8HIj8hx6kcwjLrz42Dqqyh6yzPBkGAEXA+1zff2kmSNjA4JJ"
			+ "KrirdgoAsaA9EXh2FTman84c4DLpIARJyIuKdbkD1XozPGipzzv9AvJT8yz"
			+ "f311bgH4J8m9mt7IY4Reaqh8ktwRZLsBlnLsjK4/ai/UnZ5FgWfVefw1pHA"
			+ "GfLu0s2Z3fPCgwJBxv1a7Pwjefzm+6SdzgoFn1j5Ocx+3TIjEwrGJtOc07v"
			+ "dHJyVaBs57iEXHe3yTDoWZf8NgsaO7vq/tLOugWnSNtTH+C+FRNTLMoK1DI"
			+ "HheCvJV1eEFeJBTPOehv2NQeXZWnmv5TR0KLf7/z5NcMD7yyoIjDxWYETp+"
			+ "RcsRYEymA/6LiBcwzDdmGGEs8C/qwYqfvko0hn8eXdZvfvKltjVQSqqo5GX"
			+ "5jfhbnS0hnBzJ3DlrwVN2kYvldnZqdstngG6d1b9YxtBUa+DkQhZdDLTalePc+OfJ0g=";

	private final MinecraftServer server;
	private final WorldServer worldserver;
	private final PlayerInteractManager playerinteractmanager;

	private final EntityPlayer player;

	public EntityPlayerTab(String name, String listName) {
		this.server = ((CraftServer) Bukkit.getServer()).getServer();
		this.worldserver = ((CraftWorld) TabList.get().getWorld()).getHandle();
		this.playerinteractmanager = new PlayerInteractManager(worldserver);

		GameProfile profile = new GameProfile(UUID.randomUUID(), name);
		profile.getProperties().put("textures", new Property("textures", baseTexture, baseSignature));
		this.player = new EntityPlayer(server, worldserver, profile, playerinteractmanager);
		this.player.listName = new ChatComponentText(listName);
		this.player.ping = 1000;
	}

	public EntityPlayerTab(Player p) {
		this.server = ((CraftServer) Bukkit.getServer()).getServer();
		this.worldserver = ((CraftWorld) TabList.get().getWorld()).getHandle();
		this.playerinteractmanager = new PlayerInteractManager(worldserver);

		this.player = ((CraftPlayer) p).getHandle();
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

}
