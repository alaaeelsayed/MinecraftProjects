package me.headshot.ineedjesus.nms.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import me.headshot.ineedjesus.INeedJesus;
import me.headshot.ineedjesus.nms.Jesus;
import me.headshot.ineedjesus.util.GlassColor;
import me.headshot.ineedjesus.util.JesusUtil;
import me.headshot.ineedjesus.util.ReflectionUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.ArmorStand;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Jesus_1_8_R3 extends EntityVillager implements Jesus {

    List<Location> changedLocations = new ArrayList<>();

    public Jesus_1_8_R3(org.bukkit.World world) {
        this(((CraftWorld) world).getHandle());
    }

    public Jesus_1_8_R3(World world) {
        super(world);
        emptyPathfinders();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalAvoidTarget<>(this, EntitySheep.class, entity -> entity.isAlive() && entity.getColor() == EnumColor.RED, 8.0F, 0.6D, 1D));
        this.goalSelector.a(1, new PathfinderGoalAvoidCross(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityPigZombie.class, 1.0D, true));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityWither.class, 1.0D, true));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityMagmaCube.class, 1.0D, true));
        this.goalSelector.a(3, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 0.6D));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityPigZombie.class, true));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityWither.class, true));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(this, EntityMagmaCube.class, true));
    }

    @Override
    public void move(double x, double y, double z) {
        super.move(x, y, z);
        if (!(((int) this.lastX == (int) locX) && ((int) this.lastY == (int) locY) && ((int) this.lastZ == (int) locZ))) {
            List<Location> newLocations = getWaterUnderGod(new Location(Bukkit.getWorld(this.world.getWorld().getName()), locX, locY, locZ), 50);
            for (Location changedLocation : changedLocations) {
                if (!newLocations.contains(changedLocation))
                    changedLocation.getBlock().setType(org.bukkit.Material.WATER);
            }
            changedLocations.clear();
            for (Location location : newLocations) {
                Block block = location.getBlock();
                changedLocations.add(block.getLocation());
                block.setTypeIdAndData(org.bukkit.Material.STAINED_GLASS.getId(), GlassColor.LIGHT_PURPLE, false);
            }
        }
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.33000000417232513D);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100000D);
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
        this.makeSound("mob.villager.idle", 1F, bC());
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
        move(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void spawn() {
        world.addEntity(this);
        ArmorStand am = getLocation().getWorld().spawn(getLocation(), ArmorStand.class);
        am.setVisible(false);
        am.setCustomName(ChatColor.DARK_PURPLE + "Jesus Christ");
        am.setCustomNameVisible(true);
        am.setGravity(false);
        this.getBukkitEntity().setPassenger(am);
    }

    @Override
    public boolean a(EntityHuman human) {
        return false;
    }

    @Override
    public void collide(Entity entity) {
        if (entity instanceof EntityPigZombie || entity instanceof EntityWither || entity instanceof EntityMagmaCube)
            entity.damageEntity(DamageSource.MAGIC, 3F);
    }

    public List<Location> getWaterUnderGod(Location mainLoc, int radius) {
        List<Location> blocks = new ArrayList<>();
        Location loc = mainLoc.clone();
        int godX = (loc.getBlockX() - (radius / 2));
        int y = loc.getBlockY() - 1;
        int godZ = (loc.getBlockZ() - (radius / 2));
        for (int x = godX; x <= godX + radius; x++) {
            for (int z = godZ; z <= godZ + radius; z++) {
                Location location = new Location(loc.getWorld(), x, y, z);
                if (mainLoc.distanceSquared(location) > radius * 2) continue;
                Block block = location.getBlock();
                if (block.isLiquid() || changedLocations.contains(location))
                    blocks.add(block.getLocation());
            }
        }
        return blocks;
    }

    public static void addToMaps() {
        try {
            ((Map) ReflectionUtil.getPrivateField("c", EntityTypes.class, null)).put("Jesus", Jesus_1_8_R3.class);
            ((Map) ReflectionUtil.getPrivateField("d", EntityTypes.class, null)).put(Jesus_1_8_R3.class, "Jesus");
            ((Map) ReflectionUtil.getPrivateField("f", EntityTypes.class, null)).put(Jesus_1_8_R3.class, 120);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public class PathfinderGoalAvoidCross extends PathfinderGoal {
        protected EntityCreature a;
        private double d;
        private double e;
        protected EntityArmorStand b;
        private float f;
        private PathEntity g;
        private NavigationAbstract h;
        private Predicate<? super EntityArmorStand> j;

        public PathfinderGoalAvoidCross(EntityCreature var1) {
            this.a = var1;
            this.j = entity -> entity.isAlive() && entity.getName().toLowerCase().contains("mighty cross");
            this.f = 8.0F;
            this.d = 0.6D;
            this.e = 1D;
            this.h = var1.getNavigation();
            this.a(1);
        }

        public boolean a() {
            List var1 = this.a.world.a(EntityArmorStand.class, this.a.getBoundingBox().grow((double) this.f, 3.0D, (double) this.f), Predicates.and(new Predicate[]{IEntitySelector.d, this.j}));
            if (var1.isEmpty()) {
                return false;
            } else {
                this.b = (EntityArmorStand) var1.get(0);
                Vec3D var2 = RandomPositionGenerator.b(this.a, 16, 7, new Vec3D(this.b.locX, this.b.locY, this.b.locZ));
                if (var2 == null) {
                    return false;
                } else if (this.b.e(var2.a, var2.b, var2.c) < this.b.h(this.a)) {
                    return false;
                } else {
                    this.g = this.h.a(var2.a, var2.b, var2.c);
                    return this.g == null ? false : this.g.b(var2);
                }
            }
        }

        public boolean b() {
            return !this.h.m();
        }

        public void c() {
            this.h.a(this.g, this.d);
        }

        public void d() {
            this.b = null;
        }

        public void e() {
            if (this.a.h(this.b) < 49.0D) {
                this.a.getNavigation().a(this.e);
            } else {
                this.a.getNavigation().a(this.d);
            }

        }
    }
}
