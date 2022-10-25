package me.orbitium.protect;

import me.orbitium.Main;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.List;

public class Protector {

    public ArmorStand center;
    public int range;
    public String name;
    public int x;
    public int z;

    public void createNewProtector(String worldName, String name, double x, double z, int range) {
        Location location = new Location(Bukkit.getWorld(worldName), x, 0, z);
        center = location.getWorld().spawn(location, ArmorStand.class);
        center.setVisible(true);
        center.setGravity(false);
        center.setInvulnerable(true);
        Manager.protectors.add(this);
        this.range = range;
        this.name = name;
    }

    public void tick() {
        List<Entity> entities = center.getNearbyEntities(range, 256, range);
        for (Entity entity : entities) {
            if (!(entity instanceof Player) && entity instanceof LivingEntity) {
                if (Main.blockedMobs.contains(entity.getName()))
                    entity.remove();
            }
        }
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(center.getWorld().getName()).append(" ");
        sb.append(name).append(" ");
        sb.append(center.getLocation().getX()).append(" ");
        sb.append(center.getLocation().getZ()).append(" ");
        sb.append(range);
        return sb.toString();
    }

}
