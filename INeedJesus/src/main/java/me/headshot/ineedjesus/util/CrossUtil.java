package me.headshot.ineedjesus.util;

import me.headshot.ineedjesus.nms.Cross;
import me.headshot.ineedjesus.nms.CrossDetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrossUtil {

    private static CrossDetector crossDetector;
    private static List<Cross> crossList = new ArrayList<>();

    public static void setCrossDetector(CrossDetector crossDetector) {
        CrossUtil.crossDetector = crossDetector;
    }

    public static CrossDetector getCrossDetector() {
        return crossDetector;
    }

    public static List<Cross> getCrosses() {
        return crossList;
    }

    public static void addCrosses(Cross... crosses) {
        crossList.addAll(Arrays.asList(crosses));
    }

    public static void removeCross(Cross cross) {
        crossList.remove(cross);
    }
}
