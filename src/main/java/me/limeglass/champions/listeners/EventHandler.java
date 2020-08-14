package me.limeglass.champions.listeners;

import me.limeglass.champions.Champions;
import me.limeglass.champions.abstracts.Ability;
import me.limeglass.champions.managers.PlayerManager;
import me.limeglass.champions.objects.ChampionsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EventHandler {
	
	private static final Map<Ability, Class<? extends Event>[]> abilities = new HashMap<>();
	private static final Set<Class<? extends Event>> registeredEvents = new HashSet<>();
	private final static Listener listener = new Listener(){};
	private static Event last;
	
	public static void remove(Ability ability) {
		abilities.remove(ability);
	}
	
	public static Map<Ability, Class<? extends Event>[]> getEventAbilities() {
		return abilities;
	}
	
	final static EventExecutor executor = new EventExecutor() {
		@Override
		public void execute(Listener listner, Event event) {
			if (event == null || last == event)
				return; // an event is received multiple times if multiple superclasses of it are registered.
			last = event;
			abilities.forEach((key, value) -> {
				Player player = key.check(event);
				if (player != null) {
					Optional<ChampionsPlayer> optional = PlayerManager.getChampionsPlayer(player);
					if (optional.isPresent()) {
						ChampionsPlayer championsPlayer = optional.get();
						if (championsPlayer.hasKit() && championsPlayer.getKit().getAbilities().contains(key)) {
							key.onAbilityExecute(championsPlayer);
						}
					}
				}
			});
		}
	};
	
	@SafeVarargs
	public static void register(Ability ability, Class<? extends Event>... events) {
		if (!abilities.containsKey(ability)) {
			abilities.put(ability, events);
			for (Class<? extends Event> event : events) {
				Bukkit.getPluginManager().registerEvent(event, listener, EventPriority.HIGHEST, executor, Champions.getInstance());
				registeredEvents.add(event);
			}
		}
	}

}
