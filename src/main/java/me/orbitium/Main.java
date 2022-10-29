package me.orbitium;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.orbitium.command.ProtectedAreaCommandComplater;
import me.orbitium.command.ProtectedAreaCommandExecutor;
import me.orbitium.listener.InventoryListener;
import me.orbitium.protect.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    public static List<String> blockedMobs = new ArrayList<>();

    private static Main instance;

    public static ItemStack emptyItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    public static WorldEditPlugin worldEditPlugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        saveDefaultConfig();
        getCommand("protectarea").setExecutor(new ProtectedAreaCommandExecutor());
        getCommand("protectarea").setTabCompleter(new ProtectedAreaCommandComplater());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, Manager::tick, 10L, 1L);
        blockedMobs = getConfig().getStringList("unwantedMobs");
        Manager.load();

        ItemMeta emptyItemMeta = emptyItem.getItemMeta();
        emptyItemMeta.setDisplayName(" ");
        emptyItem.setItemMeta(emptyItemMeta);

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Manager.save();
    }

    public static Main getInstance() {
        return instance;
    }

    public static WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }
}
