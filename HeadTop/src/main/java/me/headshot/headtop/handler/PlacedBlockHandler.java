package me.headshot.headtop.handler;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import me.headshot.headtop.config.HeadTopConfig;
import me.headshot.headtop.struct.PlacedBlock;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PlacedBlockHandler {
    private static HashMap<String, PlacedBlock> trackedPlacedBlocks;
    private static Thread placedBlocksSaveThread;
    private static boolean saved;

    public static void init() {
        placedBlocksSaveThread = new Thread(() -> {
            savePlacedBlocks();
            saved = true;
        });
        placedBlocksSaveThread.setName("Block Place Save Thread");
        loadPlacedBlocks();
        scanLoadedChunks();
    }

    public static HashMap<String, PlacedBlock> getTrackedPlacedBlocks(){
        return trackedPlacedBlocks;
    }

    public static Set<Material> getTypesToTrack(){
        return HeadTopConfig.blockPrices.keySet();
    }

    private static void loadPlacedBlocks() {
        trackedPlacedBlocks = new HashMap<>();
        int count = 0;
        File placedBlocksFile = new File("placed-blocks.csv");
        if (!placedBlocksFile.exists()) {
            try {
                placedBlocksFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(placedBlocksFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() <= 0) continue;
                try {
                    String[] data = line.split(",");
                    if (data.length != 6) {
                        Bukkit.getLogger().info("Invalid stored placed block: " + line);
                        continue;
                    }
                    long placeTime = Long.parseLong(data[4]);
                    Material blockType = Material.valueOf(data[5]);
                    if (blockType == Material.AIR) continue;
                    trackedPlacedBlocks.put((data[0] + "," + data[1] + "," + data[2] + "," + data[3]), new PlacedBlock(Integer.valueOf(data[1]), Integer.valueOf(data[2]), Integer.valueOf(data[3]), data[0], placeTime, blockType));
                    ++count;
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Bukkit.getLogger().info("Successfully Loaded " + count + " placed blocks.");
    }

    public static synchronized void startSaveThread() {
        placedBlocksSaveThread.start();
        while (!saved) {
            try {
                Bukkit.getLogger().info("Saving " + trackedPlacedBlocks.size() + " blocks.");
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
        placedBlocksSaveThread.interrupt();
    }

    private static void savePlacedBlocks() {
        try {
            File placedBlocksFile = new File("placed-blocks.csv");
            placedBlocksFile.delete();
            BufferedWriter out = new BufferedWriter(new FileWriter(placedBlocksFile));
            for (PlacedBlock block : trackedPlacedBlocks.values()) {
                if (block.getBlockType() == Material.AIR) continue;
                try {
                    out.write(block.toString() + "\n");
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
            out.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static boolean isTracked(Block block) {
        try {
            return trackedPlacedBlocks.containsKey(block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ());
        } catch (Exception err) {
            return false;
        }
    }

    private static void scanLoadedChunks() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                FLocation fLocation = new FLocation(world.getName(), chunk.getX(), chunk.getZ());
                if (Board.getInstance().getFactionAt(fLocation).isNormal()) {
                    for (BlockState blockState : chunk.getTileEntities()) {
                        Block block = blockState.getBlock();
                        String blockString = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
                        if (!getTypesToTrack().contains(blockState.getBlock().getType()) || isTracked(block))
                            continue;
                        trackedPlacedBlocks.put(blockString, new PlacedBlock(block, System.currentTimeMillis()));
                    }
                }
            }
        }
    }
}
