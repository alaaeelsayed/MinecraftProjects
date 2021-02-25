package me.headshot.headtop.struct;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Role;
import me.headshot.headtop.HeadTop;
import me.headshot.headtop.config.HeadTopConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FTopFaction {
    private int rank;
    private String factionID;
    private List<PlacedSpawner> placedSpawners;
    private List<PlacedBlock> placedBlocks;
    private double totalSpawnerWorth = 0.0;
    private double totalBlockWorth = 0.0;
    private double totalBalance = 0.0;
    private double bankBalance = 0.0;
    private double highestPlayerBalance = 0.0;
    private String highestPlayer = "None";
    private String owner;
    private DecimalFormat format = new DecimalFormat("#,##0");
    private long lastBalanceUpdate = -1L;

    public FTopFaction(String factionId, List<PlacedSpawner> spawners, List<PlacedBlock> blocks) {
        this.factionID = factionId;
        this.placedSpawners = spawners;
        this.placedBlocks = blocks;
    }

    public void calculateSpawnerWorth() {
        totalSpawnerWorth = 0.0;
        long day = TimeUnit.DAYS.toMillis(1L);
        for (PlacedSpawner spawner : placedSpawners) {
            double price = HeadTopConfig.spawnerPrices.get(spawner.getSpawnerType());
            if (HeadTopConfig.graduallyIncreaseSpawnerValue) {
                long daysPlaced = spawner.getTimePlaced() / day;
                double placedPercent = HeadTopConfig.baseSpawnerPercentage + HeadTopConfig.spawnerPercentageIncreasePerDay * daysPlaced;
                placedPercent = Math.min(100, Math.max(HeadTopConfig.baseSpawnerPercentage, placedPercent));
                price *= placedPercent * 0.01;
            }
            totalSpawnerWorth += price;
        }
    }

    public void calculateBlockWorth() {
        totalBlockWorth = 0.0;
        long day = TimeUnit.DAYS.toMillis(1L);
        for (PlacedBlock block : placedBlocks) {
            double price = HeadTopConfig.blockPrices.get(block.getBlockType());
            if (HeadTopConfig.graduallyIncreaseBlockValue) {
                long daysPlaced = block.getTimePlaced() / day;
                double placedPercent = HeadTopConfig.baseBlockPercentage + HeadTopConfig.blockPercentageIncreasePerDay * daysPlaced;
                placedPercent = Math.min(100, Math.max(20, placedPercent));
                price *= placedPercent * 0.01;
            }
            totalBlockWorth += price;
        }
    }

    public void calculateTotalBalance() {
        this.calculateBlockWorth();
        Faction faction = this.getFaction();
        if (faction == null) {
            return;
        }
        if (HeadTopConfig.usePlayerBalance) {
            this.totalBalance = 0.0;
            double currentHighest = 0.0;
            FPlayer highest = null;
            this.lastBalanceUpdate = System.currentTimeMillis();
            for (FPlayer player : this.getFaction().getFPlayers()) {
                if (player == null) continue;
                String name = player.getName();
                if (player.getRole() == Role.ADMIN) {
                    this.owner = name;
                }
                try {
                    double bal = HeadTop.getEconomy().getBalance(name);
                    this.totalBalance += bal;
                    if (bal <= currentHighest) continue;
                    currentHighest = bal;
                    highest = player;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (highest != null) {
                this.highestPlayer = highest.getName();
                this.highestPlayerBalance = currentHighest;
            }
        }
        if (HeadTopConfig.useFactionBank) {
            this.bankBalance = HeadTop.getEconomy().getBalance(faction.getAccountId());
        }
    }

    public double getTotalWorth() {
        return this.totalSpawnerWorth + this.totalBlockWorth + totalBalance + this.bankBalance;
    }

    public Faction getFaction() {
        return Factions.getInstance().getFactionById(factionID);
    }

    public String generateFTopHover() {
        Faction faction = this.getFaction();
        List<String> hover = HeadTopConfig.ftopHover;
        List<String> hovers = new ArrayList<>();
        for(String val : hover){
            val = val.replace("%faction%", faction.getTag());
            val = val.replace("%leader%", faction.getFPlayerAdmin().getName());
            val = val.replace("%total%", format.format(getTotalWorth()) + "");
            val = val.replace("%blocks%", getFactionBlocks().size() + "");
            val = val.replace("%money%", format.format(getTotalBalance()) + "");
            val = val.replace("%spawners%", getFactionSpawners().size() + "");
            val = val.replace("%richestmember%", getHighestPlayer());
            val = val.replace("%richestmemberbalance%", format.format(getHighestPlayerBalance()) + "");
            for(Material material : Material.values()){
                val = val.replace("{blocks:" + material.toString() + "}", "" + placedBlocks.stream().filter(placedBlock -> placedBlock.getBlockType() == material).count());
            }
            for(EntityType entityType : EntityType.values()){
                val = val.replace("{spawners:" + entityType.toString() + "}", "" + placedSpawners.stream().filter(placedSpawner -> placedSpawner.getSpawnerType() == entityType).count());
            }
            val = ChatColor.translateAlternateColorCodes('&', val);
            hovers.add(val);
        }
        StringBuilder builder = new StringBuilder();
        for(String val : hovers){
            builder.append(val);
            builder.append("\n");
        }
        return builder.toString();
    }

    public int compareTo(FTopFaction other) {
        double totalWorth = this.getTotalWorth();
        double otherWorth = other.getTotalWorth();
        if (otherWorth == totalWorth) {
            return 0;
        }
        if (otherWorth >= totalWorth) return -1;
        return 1;
    }

    public String getFactionID() {
        return this.factionID;
    }

    public List<PlacedSpawner> getFactionSpawners() {
        return placedSpawners;
    }

    public List<PlacedBlock> getFactionBlocks() {
        return placedBlocks;
    }

    public double getTotalSpawnerWorth() {
        return this.totalSpawnerWorth;
    }

    public double getTotalBalance() {
        return this.totalBalance;
    }

    public double getTotalBlockWorth() {
        return this.totalBlockWorth;
    }

    public double getBankBalance() {
        return this.bankBalance;
    }

    public double getHighestPlayerBalance() {
        return this.highestPlayerBalance;
    }

    public String getHighestPlayer() {
        return this.highestPlayer;
    }

    public String getOwner() {
        return this.owner;
    }


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
