package de.lonzbonz.codeexamples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lonzbonz on 07.01.2015
 */
public class Enderchest {
	
	/**
	 * Opens the enderchest for a certain player
	 * @param player	The player who should get the enderchest
	 * @param lines		The amount of lines for the enderchest
	 */
	public static void open(Player player, int lines) {
		Inventory inventory = Bukkit.createInventory(null, lines*9, "Big Ender Chest");
		
		List<ItemStack> items = loadInventory(player);
		if(items == null | items.size() == 0) {
			player.openInventory(inventory);
			return;
		}
		for(int i = 0; i < items.size(); i++) {
			ItemStack item = items.get(i);
			if(item == null) {
				continue;
			}
			if(i > inventory.getSize()-1) {
				break;
			}
			inventory.setItem(i, item);
		}
		
		player.openInventory(inventory);
	}
	
	/**
	 * Loads the ItemStacks from the the .yml file into a List
	 * @param player	The player who will get the items
	 * @return			the items from .yml file
	 */
	private static List<ItemStack> loadInventory(Player player) {
		List<ItemStack> list = new ArrayList<>();
		FileConfiguration cfg = getFileConfiguration(player);
		int counter = cfg.getInt("counter");
		for(int i = 0; i < counter; i++) {
			ItemStack item = cfg.getItemStack(i + ".item");
			list.add(item);
		}
		return list;
	}
	
	/**
	 * Saves a players inventory. Call this method on the 'InventoryCloseEvent', else all items will be losed
	 * @param player	The player the items should save for
	 * @param inventory	The inventory that should get stored
	 */
	public static void save(Player player, Inventory inventory) {
		FileConfiguration cfg = getFileConfiguration(player);
		int size = inventory.getSize();
		for(int i = 0; i < size; i++) {
			ItemStack item = inventory.getContents()[i];
			cfg.set(i + ".item", item);
		}
		cfg.set("counter", size);
		try {
			cfg.save(getSaveFile(player));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static File getSaveFile(Player player) {
		return new File(Main.getInstance().getDataFolder().getPath(), player.getName() + ".yml");
	}

	public static FileConfiguration getFileConfiguration(Player player) {
		return YamlConfiguration.loadConfiguration(Enderchest.getSaveFile(player));
	}

}
