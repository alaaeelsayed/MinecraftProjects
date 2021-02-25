package me.headshot.headtop.util;

import com.google.common.collect.Lists;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.zcore.persist.MemoryBoard;
import me.headshot.headtop.HeadTop;
import me.headshot.headtop.handler.PlacedBlockHandler;
import me.headshot.headtop.handler.PlacedSpawnerHandler;
import me.headshot.headtop.struct.FTopFaction;
import me.headshot.headtop.struct.PlacedBlock;
import me.headshot.headtop.struct.PlacedSpawner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FTopUtil {
    private static DecimalFormat format = new DecimalFormat("#,###");
    static boolean loadingData = false;
    private static LinkedHashMap<Integer, FTopFaction> topFactions = new LinkedHashMap<>();
    private static LinkedHashMap<String, FTopFaction> fastFactions = new LinkedHashMap<>();
    private static int totalSpawnersFound = 0;
    private static int refreshTimer = 5;
    private static long lastUpdate = -1L;
    private static long lastWealthUpdate = -1L;
    private static Map<Integer, String> previousFTops = new HashMap<>();

    public static FTopFaction getTopFaction(String id) {
        return fastFactions.get(id);
    }

    public static void loadTopFactions(Runnable callback, int delay) {
        long last;
        long l = last = lastUpdate;
        if (delay != -1 && last != -1L && System.currentTimeMillis() - last < TimeUnit.MINUTES.toMillis((long) refreshTimer)) {
            return;
        }
        lastUpdate = System.currentTimeMillis();
        HashMap<String, List<PlacedSpawner>> spawners = new HashMap<>();
        HashMap<String, List<PlacedBlock>> blocks = new HashMap<>();
        HashMap<String, FTopFaction> storedFactions = new HashMap<>();
        System.out.println("Loading spawners..");
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    MemoryBoard.MemoryBoardMap factionLocations = ((MemoryBoard) Board.getInstance()).flocationIds;
                    loadingData = true;
                    long start = System.currentTimeMillis();
                    long firstStart = System.currentTimeMillis();
                    int chunksChecked = 0;
                    String previousPlace;
                    int spawnersChecked = 0;
                    for (Map.Entry<String, PlacedSpawner> entry : PlacedSpawnerHandler.getPlacedSpawners().entrySet()) {
                        ++spawnersChecked;
                        if (entry.getValue().getSpawnerType() != EntityType.UNKNOWN) {
                            String chunkCoords = entry.getValue().getWorldName() + ":" + entry.getValue().getChunkX() + "," + entry.getValue().getChunkZ();
                            List<PlacedSpawner> spawner = spawners.get(chunkCoords);
                            if (spawner == null) {
                                spawner = Lists.newArrayList();
                            }
                            spawner.add(entry.getValue());
                            totalSpawnersFound++;
                            spawners.put(chunkCoords, spawner);
                        }
                    }

                    System.out.println("Loaded " + spawnersChecked + " spawners in " + spawners.size() + " chunks.");
                    AtomicInteger index = new AtomicInteger(0);
                    long trackStart = System.currentTimeMillis();
                    if (PlacedBlockHandler.getTrackedPlacedBlocks() != null) {
                        (new HashMap<>(PlacedBlockHandler.getTrackedPlacedBlocks())).forEach((coordString, blockValue) -> {
                            String chunkCoords = blockValue.getWorldName() + ":" + blockValue.getChunkX() + "," + blockValue.getChunkZ();
                            List<PlacedBlock> currentBlocksInChunk = blocks.computeIfAbsent(chunkCoords, s -> Lists.newArrayList());
                            currentBlocksInChunk.add(blockValue);
                            index.addAndGet(1);
                        });
                    }

                    System.out.println("Took " + (System.currentTimeMillis() - trackStart) + " ms to load " + index.get() + " trackedBlockValues into chunk map.");
                    System.out.println("Scanning " + factionLocations.size() + " Faction Claims..");

                    for (Map.Entry<FLocation, String> entry : factionLocations.entrySet()) {
                        FLocation chunkLoc = entry.getKey();
                        previousPlace = entry.getValue();
                        int factionID = Integer.parseInt(entry.getValue());
                        World world = chunkLoc != null ? chunkLoc.getWorld() : null;
                        if (factionID > 0 && world != null) {
                            String chunkCoords = world.getName() + ":" + chunkLoc.getX() + "," + chunkLoc.getZ();
                            List<PlacedSpawner> chunkSpawners = spawners.get(chunkCoords);
                            if (chunkSpawners != null) {
                                String finalPreviousPlace = previousPlace;
                                FTopFaction fTopFaction = storedFactions.computeIfAbsent(previousPlace, (facx) -> new FTopFaction(finalPreviousPlace, Lists.newArrayList(), Lists.newArrayList()));
                                fTopFaction.getFactionSpawners().addAll(chunkSpawners);
                            }

                            List<PlacedBlock> locations = blocks.get(chunkCoords);
                            if (locations != null) {
                                String finalPreviousPlace1 = previousPlace;
                                FTopFaction faction = storedFactions.computeIfAbsent(previousPlace, (e) -> new FTopFaction(finalPreviousPlace1, Lists.newArrayList(), Lists.newArrayList()));
                                faction.getFactionBlocks().addAll(locations);
                            }

                            ++chunksChecked;
                            if (chunksChecked % 10000 == 0) {
                                Bukkit.getLogger().info("Checked " + chunksChecked + " Chunks..");
                            }
                        }
                    }

                    start = System.currentTimeMillis();
                    Bukkit.getLogger().info("Starting faction check..");
                    for (Faction faction : Factions.getInstance().getAllFactions()) {
                        try {
                            if (faction.isNormal()) {
                                FTopFaction fTopFaction = storedFactions.get(faction.getId());
                                if (fTopFaction == null) {
                                    fTopFaction = new FTopFaction(faction.getId(), Lists.newArrayList(), Lists.newArrayList());
                                    storedFactions.put(faction.getId(), fTopFaction);
                                } else {
                                    fTopFaction.calculateSpawnerWorth();
                                }

                                fTopFaction.calculateTotalBalance();
                            }
                        } catch (Exception var23) {
                            var23.printStackTrace();
                        }
                    }

                    Bukkit.getLogger().info("Finished faction check in " + (System.currentTimeMillis() - start) + "ms");
                    System.out.println("Scanned " + chunksChecked + " Chunks in " + (System.currentTimeMillis() - firstStart) + "ms found " + totalSpawnersFound + " spawners.");
                    LinkedHashMap<String, FTopFaction> topSortedFactions = sortByComparator(storedFactions, false);

                    fastFactions.clear();

                    int place = 1;
                    for (Map.Entry<String, FTopFaction> entry : topSortedFactions.entrySet()) {
                        Faction topFaction = entry.getValue().getFaction();
                        if (topFaction == null) continue;
                        FTopFaction storedFaction = entry.getValue();

                        if (place < 50) {
                            System.out.println("Top Faction " + topFaction.getTag() + " is #" + place + ", found " + storedFaction.getFactionSpawners().size() + " Spawners worth " + storedFaction.getTotalSpawnerWorth() + " with top player balance: " + storedFaction.getTotalBalance() + " owner: " + storedFaction.getOwner());
                        }

                        if (place <= 3) {
                            previousPlace = previousFTops.get(place);
                            previousFTops.put(place, topFaction.getId());

                            if (previousPlace == null || !previousPlace.equals(topFaction.getId())) {
                                String worthString = "$" + format.format(storedFaction.getTotalWorth());

                                int finalPlace = place;
                                Bukkit.getScheduler().runTask(HeadTop.get(), () -> {
                                    Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.UNDERLINE.toString() + topFaction.getTag() + ChatColor.GREEN + " has taken " + ChatColor.GREEN + ChatColor.BOLD.toString() + "#" + finalPlace + ChatColor.GREEN + " in /f top with " + worthString);
                                });
                            }
                        }
                        storedFaction.setRank(place);
                        topFactions.put(place++, storedFaction);
                        fastFactions.put(storedFaction.getFactionID(), storedFaction);
                    }

                topSortedFactions.clear();
                loadingData = false;
                if (callback != null) {
                    callback.run();
                }
            }catch (Exception ex){
                    ex.printStackTrace();
                }
        }
    }.runTaskLaterAsynchronously(HeadTop.get(), delay == -1 ? 0L : (long) delay);
}

    public static LinkedHashMap<String, FTopFaction> sortByComparator(HashMap<String, FTopFaction> unsortMap, boolean order) {
        if (unsortMap == null) {
            return null;
        }
        LinkedList<Map.Entry<String, FTopFaction>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort((o1, o2) -> {
            if (!order) return (o2.getValue()).compareTo(o1.getValue());
            return (o1.getValue()).compareTo(o2.getValue());
        });
        LinkedHashMap<String, FTopFaction> sortedMap = new LinkedHashMap<>();
        for(Map.Entry<String, FTopFaction> entry : list){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static LinkedHashMap<Integer, FTopFaction> getTopFactions() {
        return topFactions;
    }
    public static LinkedHashMap<String, FTopFaction> getFastFactions() {
        return fastFactions;
    }

    public static int getRefreshTimer() {
        return refreshTimer;
    }

    public static boolean isLoadingData() {
        return loadingData;
    }

    public static long getLastUpdate() {
        return lastUpdate;
    }

    public static long getLastWealthUpdate() {
        return lastWealthUpdate;
    }
}
