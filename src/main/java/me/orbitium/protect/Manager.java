package me.orbitium.protect;

import me.orbitium.Main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager {

    public static List<Protector> protectors = new ArrayList<>();

    public static void tick() {
        for (Protector protector : protectors) {
            protector.tick();
        }
    }

    public static boolean remove(String name) {
        for (Protector protector : protectors) {
            if (protector.name.equals(name)) {
                protector.center.remove();
                protectors.remove(protector);
                return true;
            }
        }
        return false;
    }

    public static Protector getByName(String name) {
        for (Protector protector : protectors) {
            if (protector.name.equals(name))
                return protector;
        }
        return null;
    }

    public static boolean isNameAvailable(String name) {
        for (Protector protector : protectors) {
            if (protector.name.equals(name))
                return false;
        }
        return true;
    }

    public static void save() {
        try {
            File file = new File(Main.getInstance().getDataFolder() + "/areas.json");

            StringBuilder sb = new StringBuilder();

            for (Protector protector : protectors) {
                sb.append(protector.serialize()).append("\n");
            }

            FileWriter fw = new FileWriter(file);
            fw.write(sb.toString());
            fw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void load() {
        try {
            File file = new File(Main.getInstance().getDataFolder() + "/areas.json");
            if (!file.exists())
                file.createNewFile();
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] sLine = line.split(" ");
                String worldName = sLine[0];
                String name = sLine[1];
                double x = Double.parseDouble(sLine[2]);
                double z = Double.parseDouble(sLine[3]);
                int range = Integer.parseInt(sLine[4]);

                new Protector().createNewProtector(worldName, name, x, z, range);
                System.out.println(name);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
