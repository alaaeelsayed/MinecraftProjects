package me.headshot.headtop.config;

import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Relation;
import me.headshot.headtop.HeadTop;
import me.headshot.headtop.struct.FTopFaction;
import me.headshot.headtop.util.FTopUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HeadTopConfig {
    static DecimalFormat format = new DecimalFormat("#,##0");

    public static Map<Relation, String> relationColors = new HashMap<>();
    public static String ftopLine = "&6%rank%. %relationcolor%%factionname% &a$ %value%";
    public static List<String> ftopHeader = Collections.singletonList("&4&l&m-------------&e&lFactionsTop Page &c&l%page%&e&l/&c&l%maxpage%&4&l&m-------------");
    public static List<String> ftopHover = Arrays.asList("&4&l&m<------&e&l%faction%&4&l&m------>",
            "&eLeader: &c%leader%",
            "",
            "&eTotal Wealth: &6$ %total%",
            "&eBlocks: &6$ %blocks%",
            "&eMoney: &6$ %money%",
            "&eSpawners: &6$ %spawners%",
            "",
            "&eRichest Member : &a%richestmember%",
            "&eRichest Member Balance : &6%richestmemberbalance%",
            "",
            "&4&l&m<------&b&lBlocks&4&l&m------>",
            "&b{blocks:BEACON} &cx &eBeacon",
            "&b{blocks:DISPENSER} &cx &eDispenser",
            "&b{blocks:DROPPER} &cx &eDropper",
            "&b{blocks:HOPPER} &cx &eHopper",
            "",
            "&4&l&m<------&b&lSpawners&4&l&m------>",
            "&b{spawners:SILVERFISH} &cx &eSilverFish",
            "&b{spawners:IRON_GOLEM} &cx &eIronGolem",
            "&b{spawners:CREEPER} &cx &eCreeper",
            "&b{spawners:BLAZE} &cx &eBlaze",
            "&b{spawners:ENDERMAN} &cx &eEnderman",
            "&b{spawners:SKELETON} &cx &eSkeleton",
            "&b{spawners:ZOMBIE} &cx &eZombie");
    public static boolean graduallyIncreaseSpawnerValue = true;
    public static boolean graduallyIncreaseBlockValue = true;
    public static Map<EntityType, Double> spawnerPrices = new HashMap<>();
    public static Map<Material, Double> blockPrices = new HashMap<>();
    public static int baseSpawnerPercentage = 20;
    public static int spawnerPercentageIncreasePerDay = 10;
    public static int baseBlockPercentage = 20;
    public static int blockPercentageIncreasePerDay = 10;
    public static int factionsPerPage = 10;
    public static boolean usePlayerBalance = true;
    public static boolean useFactionBank = true;

    public static void load(FileConfiguration config) {
        Map<String, Object> relationColorsMap = config.isConfigurationSection("relation-colors") ? config.getConfigurationSection("relation-colors").getValues(false) : null;
        if (relationColorsMap == null || relationColorsMap.isEmpty()) {
            relationColorsMap = new HashMap<>();
            relationColorsMap.put("enemy", "&c");
            relationColorsMap.put("truce", "&d");
            relationColorsMap.put("ally", "&2");
            relationColorsMap.put("member", "&a");
            relationColorsMap.put("neutral", "&f");
        }
        relationColors = relationColorsMap.entrySet().stream()
                .collect(Collectors.toMap(e ->  Relation.fromString(e.getKey()), e -> (String)e.getValue()));
        ftopLine = config.getString("ftop-line", "&6%rank%. %relationcolor%%factionname% &a$ %value%");
        ftopHeader = (List<String>) config.getList("ftop-header", Collections.singletonList("&4&l&m-------------&e&lFactionsTop Page &c&l%page%&4&l&m-------------"));
        ftopHover = (List<String>) config.getList("ftop-hover", Arrays.asList("&4&l&m<------&e&l%faction%&4&l&m------>",
                "&eLeader: &c%leader%",
                "",
                "&eTotal Wealth: &6$ %total%",
                "&eBlocks: &6$ %blocks%",
                "&eMoney: &6$ %money%",
                "&eSpawners: &6$ %spawners%",
                "",
                "&eRichest Member : &a%richestmember%",
                "&eRichest Member Balance : &6%richestmemberbalance%",
                "",
                "&4&l&m<------&b&lBlocks&4&l&m------>",
                "&b(blocks:BEACON:0) &cx &eBeacon",
                "&b(blocks:DISPENSER:0) &cx &eDispenser",
                "&b(blocks:DROPPER:0) &cx &eDropper",
                "&b(blocks:HOPPER:0) &cx &eHopper",
                "",
                "&4&l&m<------&b&lSpawners&4&l&m------>",
                "&b(spawners:SILVER_FISH) &cx &eSilverFish",
                "&b(spawners:IRON_GOLEM) &cx &eIronGolem",
                "&b(spawners:CREEPER) &cx &eCreeper",
                "&b(spawners:BLAZE) &cx &eBlaze",
                "&b(spawners:ENDERMAN) &cx &eEnderman",
                "&b(spawners:SKELETON) &cx &eSkeleton",
                "&b(spawners:ZOMBIE) &cx &eZombie"));
        Bukkit.getLogger().info(ftopHover.toString());
        graduallyIncreaseSpawnerValue = config.getBoolean("gradually-increase-spawner-value", true);
        graduallyIncreaseBlockValue = config.getBoolean("gradually-increase-block-value", true);
        Map<String, Object> spawnerPricesMap = config.isConfigurationSection("spawner-prices") ? config.getConfigurationSection("spawner-prices").getValues(false) : null;
        if (spawnerPricesMap == null || spawnerPricesMap.isEmpty()) {
            spawnerPricesMap = new HashMap<>();
            spawnerPricesMap.put("SILVERFISH", 100000d);
            spawnerPricesMap.put("IRON_GOLEM", 100000d);
            spawnerPricesMap.put("CREEPER", 100000d);
            spawnerPricesMap.put("BLAZE", 100000d);
            spawnerPricesMap.put("ENDERMAN", 100000d);
            spawnerPricesMap.put("SKELETON", 100000d);
            spawnerPricesMap.put("ZOMBIE", 100000d);
        }
        spawnerPrices = spawnerPricesMap.entrySet().stream()
                .collect(Collectors.toMap(e ->  EntityType.valueOf(e.getKey().toUpperCase()), e -> (double)e.getValue()));
        Map<String, Object> blockPricesMap = config.isConfigurationSection("block-prices") ? config.getConfigurationSection("block-prices").getValues(false) : null;
        if (blockPricesMap == null || blockPricesMap.isEmpty()) {
            blockPricesMap = new HashMap<>();
            blockPricesMap.put("BEACON", 10000d);
            blockPricesMap.put("DISPENSER", 10000d);
            blockPricesMap.put("DROPPER", 10000d);
            blockPricesMap.put("HOPPER", 10000d);
        }
        blockPrices = blockPricesMap.entrySet().stream()
                .collect(Collectors.toMap(e ->  Material.valueOf(e.getKey().toUpperCase()), e -> (double)e.getValue()));
        factionsPerPage = config.getInt("factions-per-page", 10);
        usePlayerBalance = config.getBoolean("use-player-balance", true);
        useFactionBank = config.getBoolean("use-faction-bank", true);
    }

    public static void save(FileConfiguration config) {
        if (!config.contains("relation-colors")) {
            config.set("relation-colors", relationColors.entrySet().stream()
                    .collect(Collectors.toMap(e ->  e.getKey().toString(), Map.Entry::getValue)));
        }
        if (!config.contains("ftop-line")) {
            config.set("ftop-line", ftopLine);
        }
        if (!config.contains("ftop-header")) {
            config.set("ftop-header", ftopHeader);
        }
        if (!config.contains("ftop-hover")) {
            config.set("ftop-hover", ftopHover);
        }
        if (!config.contains("gradually-increase-spawner-value")) {
            config.set("gradually-increase-spawner-value", graduallyIncreaseSpawnerValue);
        }
        if (!config.contains("gradually-increase-block-value")) {
            config.set("gradually-increase-block-value", graduallyIncreaseBlockValue);
        }
        if (!config.contains("spawner-prices")) {
            config.set("spawner-prices", spawnerPrices.entrySet().stream()
                    .collect(Collectors.toMap(e ->  e.getKey().toString(), Map.Entry::getValue)));
        }
        if (!config.contains("block-prices")) {
            config.set("block-prices", blockPrices.entrySet().stream()
                    .collect(Collectors.toMap(e ->  e.getKey().toString(), Map.Entry::getValue)));
        }

        if (!config.contains("factions-per-page")) {
            config.set("factions-per-page", factionsPerPage);
        }
        if (!config.contains("use-player-balance")) {
            config.set("use-player-balance", usePlayerBalance);
        }
        if (!config.contains("use-faction-bank")) {
            config.set("use-faction-bank", useFactionBank);
        }
    }

    public static void sendFTopMessage(Player player, int page){
        if (FTopUtil.isLoadingData()) {
            player.sendMessage(HeadTopLang.CALCULATING.format());
            return;
        }
        int perPage = HeadTopConfig.factionsPerPage;
        int totalPages = FTopUtil.getTopFactions().size() / perPage;
        page = Math.min(totalPages, page);
        if (page <= 0) {
            page = 1;
        }

        if (System.currentTimeMillis() - FTopUtil.getLastUpdate() > TimeUnit.MINUTES.toMillis(FTopUtil.getRefreshTimer())) {
            player.sendMessage(HeadTopLang.CALCULATING.format());
            int finalPage = page;
            FTopUtil.loadTopFactions(() -> {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(HeadTop.get(), () ->
                                player.performCommand("ftop " + finalPage));
                    }
                    , 0);
            return;
        }
        int startPosition = (page - 1) * perPage;
        player.sendMessage("");
        List<String> header = HeadTopConfig.ftopHeader;
        List<String> headers = new ArrayList<>();
        for(String val : header){
            val = ChatColor.translateAlternateColorCodes('&', val);
            val = val.replace("%page%", "" + page);
            val = val.replace("%maxpage%", "" + totalPages);
            headers.add(val);
        }
        player.sendMessage(headers.toArray(new String[]{}));
        FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
        Faction current = fplayer.getFaction();
        int place = 0;
        ArrayList<FTopFaction> topFactions = Lists.newArrayList(FTopUtil.getTopFactions().values());
        for (int i = startPosition; i < startPosition + perPage && place < perPage && i < topFactions.size() && i <= 50; ++i) {
            FTopFaction faction = topFactions.get(i);
            if (faction.getFaction() == null) continue;
            String relationColor = "&c";
            for(Map.Entry<Relation, String> relation : HeadTopConfig.relationColors.entrySet()){
                if(relation.getKey() == faction.getFaction().getRelationTo(current)){
                    relationColor = relation.getValue();
                }
            }
            String worth = HeadTopConfig.ftopLine;
            worth = worth.replace("%relationcolor%", relationColor);
            worth = worth.replace("%rank%", "" + faction.getRank());
            worth = worth.replace("%factionname%", "" + faction.getFaction().getTag());
            worth = worth.replace("%value%", "" + format.format(faction.getTotalWorth()));
            worth = ChatColor.translateAlternateColorCodes('&', worth);
            TextComponent textComponent = new TextComponent(worth);
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f who " + faction.getFaction().getTag()));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(faction.generateFTopHover()).create()));
            player.spigot().sendMessage(textComponent);
            ++place;
        }
        player.sendMessage("");
    }
}
