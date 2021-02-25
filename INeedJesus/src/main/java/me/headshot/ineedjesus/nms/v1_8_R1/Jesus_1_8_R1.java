package me.headshot.ineedjesus.nms.v1_8_R1;

import me.headshot.ineedjesus.INeedJesus;
import me.headshot.ineedjesus.nms.Jesus;
import me.headshot.ineedjesus.util.JesusUtil;
import me.headshot.ineedjesus.util.ReflectionUtil;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.util.UnsafeList;

import java.lang.reflect.Field;
import java.util.Map;

public class Jesus_1_8_R1 extends EntityVillager implements Jesus {

    public Jesus_1_8_R1(org.bukkit.World world) {
        this(((CraftWorld) world).getHandle());
    }

    public Jesus_1_8_R1(World world) {
        super(world);
        emptyPathfinders();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalAvoidTarget(this, entity -> entity instanceof EntitySheep && ((EntitySheep) entity).isAlive() && ((EntitySheep) entity).getColor() == EnumColor.RED, 8.0F, 0.6D, 1D));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityPigZombie.class, 0.6D, true));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityWither.class, 0.6D, true));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityMagmaCube.class, 0.6D, true));
        this.goalSelector.a(3, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 0.6D));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityPigZombie.class, true));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityWither.class, true));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityMagmaCube.class, true));
    }

    @Override
    protected void aW() {
        super.aW();
        this.getAttributeInstance(GenericAttributes.d).setValue(0.33000000417232513D);
    }

    private void emptyPathfinders() {
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void sendRandomMessageWithVoice(INeedJesus plugin) {
        JesusUtil.sayRandomVerse(plugin);
        this.makeSound("mob.villager.idle", 1F, bB());
    }

    @Override
    public Location getLocation() {
        return new Location(world.getWorld(), locX, locY, locZ, yaw, pitch);
    }

    @Override
    public void setLocation(Location loc) {
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        setLocation(x, y, z, yaw, pitch);
    }

    @Override
    public void move(Location location) {

    }

    @Override
    public void spawn() {
        world.addEntity(this);
    }

    @Override
    public void collide(Entity entity) {
        if(entity instanceof EntityPigZombie || entity instanceof EntityWither || entity instanceof EntityMagmaCube)
            entity.damageEntity(DamageSource.MAGIC, 3F);
    }

    public static void addToMaps() {
        try {
            ((Map) ReflectionUtil.getPrivateField("c", EntityTypes.class, null)).put("Jesus", Jesus_1_8_R1.class);
            ((Map) ReflectionUtil.getPrivateField("d", EntityTypes.class, null)).put(Jesus_1_8_R1.class, "Jesus");
            ((Map) ReflectionUtil.getPrivateField("f", EntityTypes.class, null)).put(Jesus_1_8_R1.class, 120);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
