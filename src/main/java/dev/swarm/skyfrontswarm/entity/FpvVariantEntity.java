package dev.swarm.skyfrontswarm.entity;

import dev.krutlexik.skyfront.config.SkyfrontConfig;
import dev.krutlexik.skyfront.drone.GrenadeAttachments;
import dev.krutlexik.skyfront.drone.entity.FpvDroneEntity;
import dev.swarm.skyfrontswarm.SwarmConfig;
import dev.swarm.skyfrontswarm.registry.SwarmItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * FPV quads with a built-in charge. Skyfront's stock FPV only explodes if you strap a grenade on it;
 * these also carry their own warhead (and still honour an attached grenade).
 */
public abstract class FpvVariantEntity extends FpvDroneEntity {
    private static final double RENDER_DISTANCE = 1024.0;

    protected FpvVariantEntity(EntityType<? extends FpvVariantEntity> type, Level level) {
        super(type, level);
    }

    /** Must return a constant: it is called from the super constructor. */
    protected abstract DroneStats stats();

    @Override
    protected double maxHitpoints() {
        return SkyfrontConfig.MOLNIYA_HITPOINTS.get() * stats().hitpointMul() * SwarmConfig.hitpointMultiplier();
    }

    @Override
    public double topSpeedKmh() {
        return stats().topSpeedKmh();
    }

    @Override
    public double controlRange() {
        return stats().controlRange();
    }

    @Override
    public boolean needsLongRangeLink() {
        return stats().longLink();
    }

    @Override
    public int fullFlightTicks() {
        return stats().flightSeconds() * 20;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distanceSqr) {
        return distanceSqr < RENDER_DISTANCE * RENDER_DISTANCE;
    }

    @Override
    public ItemStack packedItem() {
        return new ItemStack(stats().item().get());
    }

    @Override
    protected void goOff(ServerLevel level, Entity cause) {
        float power = (float) (SkyfrontConfig.MOLNIYA_BLAST.get() * stats().blastMul() * SwarmConfig.blastMultiplier());
        float vehicle = (float) (SkyfrontConfig.MOLNIYA_VEHICLE_DAMAGE.get() * stats().vehicleMul() * SwarmConfig.blastMultiplier());
        float radius = (float) (SkyfrontConfig.MOLNIYA_VEHICLE_RADIUS.get() * Math.max(1.0, stats().blastMul()));
        GrenadeAttachments.detonateWarhead(level, position(), this, cause, power, vehicle, radius, 1);

        if (SwarmConfig.forceBlockDamage() && !SkyfrontConfig.PILOTED_BREAKS_BLOCKS.get()) {
            level.explode(this, getX(), getY(), getZ(), Math.min(power, 24.0F), Level.ExplosionInteraction.TNT);
        }
        // Stock behaviour: breaks up (or sets off the attached grenade).
        super.goOff(level, cause);
    }

    // ------------------------------------------------------------------ variants

    /** Bomber: slow, tough, the biggest charge of the three. */
    public static final class Heavy extends FpvVariantEntity {
        private static final DroneStats STATS = new DroneStats(1.0, 0.9, 0.9, 100.0, 512.0, 480, false,
                () -> SwarmItems.FPV_HEAVY.get(), null);

        public Heavy(EntityType<? extends FpvVariantEntity> type, Level level) { super(type, level); }

        @Override protected DroneStats stats() { return STATS; }
    }

    /** Racer: fragile and very fast, a small charge. */
    public static final class Racer extends FpvVariantEntity {
        private static final DroneStats STATS = new DroneStats(0.5, 0.3, 0.3, 180.0, 640.0, 240, false,
                () -> SwarmItems.FPV_RACER.get(), null);

        public Racer(EntityType<? extends FpvVariantEntity> type, Level level) { super(type, level); }

        @Override protected DroneStats stats() { return STATS; }
    }

    /** Long-range: a big battery and a link that streams far-away chunks. */
    public static final class LongRange extends FpvVariantEntity {
        private static final DroneStats STATS = new DroneStats(0.7, 0.5, 0.5, 120.0, 2048.0, 900, true,
                () -> SwarmItems.FPV_LONGRANGE.get(), null);

        public LongRange(EntityType<? extends FpvVariantEntity> type, Level level) { super(type, level); }

        @Override protected DroneStats stats() { return STATS; }
    }
}
