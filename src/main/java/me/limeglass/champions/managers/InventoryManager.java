package me.limeglass.champions.managers;

import me.limeglass.champions.abstracts.Menu;
import me.limeglass.champions.utils.Utils;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

	private static final Map<String, Menu> menus = new HashMap<>();
	
	public static void addMenu(Menu menu) {
		if (!menus.containsKey(menu.getName()))
			menus.put(menu.getName(), menu);
	}
	
	public static void removeMenu(String name) {
		menus.remove(name);
	}
	
	public static Map<String, Menu> getMenus() {
		return menus;
	}
	
	public static boolean isMenu(InventoryView inventory) {
		for (Menu menu : menus.values()) {
			if (inventory.getTitle().equals(Utils.cc(menu.getHeader())) && InventoryType.CHEST == inventory.getType())
				return true;
		}
		return false;
	}
	
	public static boolean isMenu(InventoryView inventory, String name) {
		for (Menu menu : menus.values()) {
			if (inventory.getTitle().equals(Utils.cc(menu.getHeader())) && InventoryType.CHEST == inventory.getType() && menu.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static Menu getMenu(InventoryView inventory) {
		if (isMenu(inventory)) {
			for (Menu menu : menus.values()) {
				if (inventory.getTitle().equals(Utils.cc(menu.getHeader())) && InventoryType.CHEST == inventory.getType())
					return menu;
			}
		}
		return null;
	}
	
	public static Inventory get(String name) {
		if (menus.containsKey(name)) {
			return menus.get(name).build();
		}
		return null;
	}

}
