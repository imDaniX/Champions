package me.limeglass.champions.managers;

import me.limeglass.champions.objects.ChampionsPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerManager {

	private static final Set<ChampionsPlayer> players = new HashSet<>();
	
	public static void addPlayer(ChampionsPlayer player) {
		players.add(player);
	}
	
	public static void removePlayer(ChampionsPlayer player) {
		player.setConnected(false);
		player.setIngame(false);
		players.remove(player);
	}
	
	public static Boolean containsPlayer(Player player) {
		return players.parallelStream()
				.map(ChampionsPlayer::getPlayer)
				.anyMatch(p -> p.getUniqueId() == player.getUniqueId());
	}
	
	public static Set<ChampionsPlayer> getPlayers() {
		return players;
	}
	
	public static Set<ChampionsPlayer> getPlayersIngame() {
		return players.parallelStream()
				.filter(ChampionsPlayer::isIngame)
				.collect(Collectors.toSet());
	}
	
	public static Boolean isIngame(Player player) {
		return getPlayersIngame().parallelStream()
				.anyMatch(p -> p.getPlayer() == player && p.isIngame());
	}
	
	public static Optional<ChampionsPlayer> getChampionsPlayer(Player player) {
		return players.parallelStream()
				.filter(p -> p.getPlayer() == player)
				.findFirst();
	}
	
	public static Set<ChampionsPlayer> getIdlePlayers() {
		return players.parallelStream()
				.filter(player -> !player.isIngame())
				.collect(Collectors.toSet());
	}

}
