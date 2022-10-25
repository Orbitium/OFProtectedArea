package me.orbitium.command;

import me.orbitium.protect.Manager;
import me.orbitium.protect.Protector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProtectedAreaCommandComplater implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                return new ArrayList<>(Arrays.asList("add", "remove", "tp", "info"));
            case 2:
                if (args[0].equals("tp") || args[0].equals("remove") || args[0].equals("info")) {
                    List<String> names = new ArrayList<>();
                    for (Protector protector : Manager.protectors)
                        names.add(protector.name);
                    return names;
                }
                return new ArrayList<>(Collections.singletonList("AreaName"));
            case 3:
                return new ArrayList<>(Collections.singletonList("x"));
            case 4:
                return new ArrayList<>(Collections.singletonList("z"));
            case 5:
                return new ArrayList<>(Collections.singletonList("range"));
        }
        return new ArrayList<>();
    }
}
