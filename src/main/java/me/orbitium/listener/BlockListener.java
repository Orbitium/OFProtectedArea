package me.orbitium.listener;

import me.orbitium.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

    public static Location pos1;
    public static Location pos2;
    public static boolean newPosX = false;
    public static boolean newPosZ = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WOODEN_AXE)) {
            if (event.getPlayer().isOp() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                pos1 = event.getBlock().getLocation();
                event.setCancelled(true);
                event.getPlayer().sendMessage(Main.getInstance().getConfig().getString("messages.pos1set"));
                newPosX = true;
            }
        }
    }

    @EventHandler
    public void onBlockRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if (event.getClickedBlock() != null && event.getItem() != null) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WOODEN_AXE)) {
                if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    pos2 = event.getClickedBlock().getLocation();
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Main.getInstance().getConfig().getString("messages.pos2set"));
                    newPosZ = true;
                }
            }
        }
    }

}
