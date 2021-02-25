package me.headshot.headtop.handler;

import me.headshot.headtop.struct.PlacedBlock;
import me.headshot.headtop.struct.PlacedSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class PlacedSpawnerHandler {
    private static HashMap<String, PlacedSpawner> placedSpawners;
    private static Thread placedSpawnersSaveThread;
    private static boolean spawnersSaved;

    public static void init() {
        placedSpawnersSaveThread = new Thread(() -> {
            savePlacedSpawners();
            spawnersSaved = true;
        });
        placedSpawnersSaveThread.setName("Spawner Place Save Thread");
        loadPlacedSpawners();
    }

    public static HashMap<String, PlacedSpawner> getPlacedSpawners(){
        return placedSpawners;
    }

    private static void savePlacedSpawners() {
        try {
            File placedSpawnersFile = new File("placed-spawners.csv");
            placedSpawnersFile.delete();
            BufferedWriter out = new BufferedWriter(new FileWriter(placedSpawnersFile));
            for (PlacedSpawner spawner : placedSpawners.values()) {
                if (spawner.getSpawnerType() == EntityType.UNKNOWN) continue;
                try {
                    out.write(spawner.toString() + "\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            out.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static boolean isPlacedSpawner(Block block) {
        try {
            return placedSpawners.containsKey(block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ());
        } catch (Exception err) {
            return false;
        }
    }

    private static void loadPlacedSpawners() {
        placedSpawners = new HashMap<>();
        int count = 0;
        File placedSpawnersFile = new File("placed-spawners.csv");
        if (!placedSpawnersFile.exists()) {
            try {
                placedSpawnersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(placedSpawnersFile));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.length() <= 0) continue;
                try {
                    Block b;
                    String[] data = line.split(",");
                    EntityType spawnerType = EntityType.UNKNOWN;
                    long placeTime = 0L;
                    placeTime = Long.parseLong(data[4]);
                    spawnerType = EntityType.valueOf(data[5]);
                    if (spawnerType == EntityType.UNKNOWN) continue;
                    placedSpawners.put(data[0] + "," + data[1] + "," + data[2] + "," + data[3], new PlacedSpawner(Integer.valueOf(data[1]), Integer.valueOf(data[2]), Integer.valueOf(data[3]), data[0], placeTime, spawnerType));
                    ++count;
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Bukkit.getLogger().info("Successfully Loaded " + count + " placed spawners.");
    }

    public static synchronized void startSaveThread() {
        placedSpawnersSaveThread.start();
        while (!spawnersSaved) {
            try {
                Bukkit.getLogger().info("Saving " + placedSpawners.size() + " blocks.");
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
