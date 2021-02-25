package me.headshot.ineedjesus.nms.v1_8_R3;

import me.headshot.ineedjesus.util.ReflectionUtil;
import net.minecraft.server.v1_8_R3.*;

import java.util.Map;

public class JesusLover_1_8_R3 extends EntityVillager {

    public JesusLover_1_8_R3(World world) {
        super(world);
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, Jesus_1_8_R3.class, 0.8, true));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, Jesus_1_8_R3.class, true));
    }

    public static void addToMaps() {
        try {
            ((Map) ReflectionUtil.getPrivateField("c", EntityTypes.class, null)).put("Villager", JesusLover_1_8_R3.class);
            ((Map) ReflectionUtil.getPrivateField("d", EntityTypes.class, null)).put(JesusLover_1_8_R3.class, "Villager");
            ((Map) ReflectionUtil.getPrivateField("e", EntityTypes.class, null)).put(120, JesusLover_1_8_R3.class);
            ((Map) ReflectionUtil.getPrivateField("f", EntityTypes.class, null)).put(JesusLover_1_8_R3.class, 120);
            ((Map) ReflectionUtil.getPrivateField("g", EntityTypes.class, null)).put("Villager", 120);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
