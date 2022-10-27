package me.orbitium.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void checkInventory(InventoryClickEvent event) {
        if (event.getView().getTitle().contains(ChatColor.DARK_GRAY + "Area '"))
            event.setCancelled(true);
    }

}
