package me.orbitium.command;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.regions.Region;
import me.orbitium.Main;
import me.orbitium.protect.Manager;
import me.orbitium.protect.Protector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ProtectedAreaCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.notEnoughPermission")));
            return true;
        }

        if (args.length == 5) {
            if (args[0].equals("add")) {
                String areaName = args[1];

                if (!Manager.isNameAvailable(areaName)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaNameIsNotAvailable")));
                    return true;
                }

                int x = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);
                int range = Integer.parseInt(args[4]);

                new Protector().createNewProtector(((Player) sender).getWorld().getName(), areaName, x, z, range);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaAdded")));
            }
        } else if (args.length == 2) {
            if (args[0].equals("tp")) {
                Protector protector = Manager.getByName(args[1]);
                if (protector == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaIsNotAvailable")));
                    return true;
                }
                ((Player) sender).teleport(Manager.getByName(args[1]).center.getLocation().add(0, 100, 0));
            } else if (args[0].equals("remove")) {
                boolean remove = Manager.remove(args[1]);
                if (remove)
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaRemoved")));
                else
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaIsNotAvailable")));
            } else if (args[0].equals("info")) {
                Protector protector = Manager.getByName(args[1]);
                if (protector == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaIsNotAvailable")));
                    return true;
                }

                Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST,
                        ChatColor.DARK_GRAY + "Area '" + protector.name + "' information");

                for (int i = 0; i < 27; i++) {
                    inventory.setItem(i, Main.emptyItem);
                }

                ItemStack locationInfo = new ItemStack(Material.ARROW);
                ItemMeta locationIM = locationInfo.getItemMeta();
                locationIM.setDisplayName(ChatColor.GOLD + "Location info");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.LIGHT_PURPLE + "X: " + ChatColor.GRAY + protector.x);
                lore.add(ChatColor.LIGHT_PURPLE + "Z: " + ChatColor.GRAY + protector.z);
                locationIM.setLore(lore);
                locationInfo.setItemMeta(locationIM);

                ItemStack generalInfo = new ItemStack(Material.BOOK);
                ItemMeta generalInfoIM = generalInfo.getItemMeta();
                generalInfoIM.setDisplayName(ChatColor.GOLD + "Blocked entities");
                List<String> generalInfoLore = new ArrayList<>();
                for (String blockedMob : Main.blockedMobs) {
                    generalInfoLore.add(ChatColor.GRAY + "> " + ChatColor.RED + blockedMob);
                }

                generalInfoIM.setLore(generalInfoLore);
                generalInfo.setItemMeta(generalInfoIM);

                ItemStack rangeInfo = new ItemStack(Material.OAK_FENCE);
                ItemMeta rangeInfoIM = rangeInfo.getItemMeta();
                rangeInfoIM.setDisplayName(ChatColor.GREEN + "Range is " + protector.range +
                        " block from " + protector.x + " " + protector.z + " for each side");
                rangeInfo.setItemMeta(rangeInfoIM);


                inventory.setItem(10, locationInfo);
                inventory.setItem(13, generalInfo);
                inventory.setItem(16, rangeInfo);

                ((Player) sender).openInventory(inventory);
            } else if (args[0].equals("add")) {
                if (!Manager.isNameAvailable(args[1])) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaNameIsNotAvailable")));
                    return true;
                }
                try {

                    Region region = Main.getWorldEditPlugin().getSession(((Player) sender)).getSelection();
                    if (region == null) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("areaPosIncorrect")));
                    }
                    int range = (int) region.getBoundingBox().getPos1().distance(region.getBoundingBox().getPos2()) / 2;
                    double x = region.getCenter().getX();
                    double z = region.getCenter().getZ();
                    new Protector().createNewProtector(((Player) sender).getWorld().getName(), args[1], x, z, range);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaAdded")));

                } catch (IncompleteRegionException e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("messages.areaPosIncorrect")));
                }
            }
            return false;
        }
        return false;
    }
}
