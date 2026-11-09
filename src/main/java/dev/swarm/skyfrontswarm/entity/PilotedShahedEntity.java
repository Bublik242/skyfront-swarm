package dev.swarm.skyfrontswarm.entity;

import dev.krutlexik.skyfront.config.SkyfrontConfig;
import dev.krutlexik.skyfront.drone.GrenadeAttachments;
import dev.krutlexik.skyfront.drone.entity.MolniyaDroneEntity;
import dev.krutlexik.skyfront.entity.DebrisEntity;
import dev.swarm.skyfrontswarm.SwarmConfig;
import dev.swarm.skyfrontswarm.registry.SwarmItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

/**
 * A Shahed you fly yourself: it is a Molniya-class fixed-wing drone (so it launches from Skyfront's launch
 * rail and is piloted through the Superb Warfare monitor), but with a much longer link, a longer
 * flight time, a bigger warhead and its own Shahed debris.
 */
public abstract class PilotedShahedEntity extends MolniyaDroneEntity {
    /** How far away (blocks) other players still see these planes. */
    private static final double RENDER_DISTANCE = 2048.0;

    protected PilotedShahedEntity(EntityType<? extends PilotedShahedEntity> type, Level level) {
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
    protected float warheadPower() {
        return (float) (SkyfrontConfig.MOLNIYA_BLAST.get() * stats().blastMul() * SwarmConfig.blastMultiplier());
    }

    @Override
    protected float warheadVehicleDamage() {
        return (float) (SkyfrontConfig.MOLNIYA_VEHICLE_DAMAGE.get() * stats().vehicleMul() * SwarmConfig.blastMultiplier());
    }

    @Override
    protected float warheadVehicleRadius() {
        return (float) (SkyfrontConfig.MOLNIYA_VEHICLE_RADIUS.get() * Math.max(1.0, stats().blastMul()));
    }

    @Override
    public ItemStack packedItem() {
        return new ItemStack(stats().item().get());
    }

    @Override
    protected void goOff(ServerLevel level, Entity cause) {
        // Same warhead call Skyfront's interceptor class makes (we cannot call it through Molniya
        // without also getting Molniya's debris, and we want Shahed wreckage instead).
        float power = warheadPower();
        GrenadeAttachments.detonateWarhead(level, position(), this, cause, power,
                warheadVehicleDamage(), warheadVehicleRadius(), warheadEffect(), againstAirframes());

        // Skyfront has a switch for piloted drones breaking blocks. Swarm drones should always crater.
        if (SwarmConfig.forceBlockDamage() && !SkyfrontConfig.PILOTED_BREAKS_BLOCKS.get()) {
            level.explode(this, getX(), getY(), getZ(), Math.min(power, 24.0F), Level.ExplosionInteraction.TNT);
        }

        Quaternionf attitude = renderRotation(1.0F);
        if (attitude == null) {
            attitude = new Quaternionf().rotationY((180.0F - getYRot()) * 0.017453292F);
        }
        Vec3 velocity = getDeltaMovement();
        DebrisEntity.burst(level, stats().debris(), position(), attitude, velocity);
    }

    // ------------------------------------------------------------------ variants

    /** Light and quick to build, short legs. */
    public static final class Shahed131 extends PilotedShahedEntity {
        private static final DroneStats STATS = new DroneStats(0.8, 0.8, 0.8, 150.0, 1536.0, 420, true,
                () -> SwarmItems.SHAHED_131.get(), DebrisEntity.Airframe.SHAHED);

        public Shahed131(EntityType<? extends PilotedShahedEntity> type, Level level) { super(type, level); }

        @Override protected DroneStats stats() { return STATS; }
    }

    /** The standard long-range delta. */
    public static final class Shahed136 extends PilotedShahedEntity {
        private static final DroneStats STATS = new DroneStats(1.0, 1.6, 1.6, 190.0, 4096.0, 900, true,
                () -> SwarmItems.SHAHED_136_PILOT.get(), DebrisEntity.Airframe.SHAHED);

        public Shahed136(EntityType<? extends PilotedShahedEntity> type, Level level) { super(type, level); }

        @Override protected DroneStats stats() { return STATS; }
    }

    /** Jet-powered, very fast. */
    public static final class Shahed238 extends PilotedShahedEntity {
        private static final DroneStats STATS = new DroneStats(1.2, 1.4, 1.4, 420.0, 3072.0, 600, true,
                () -> SwarmItems.SHAHED_238_PILOT.get(), DebrisEntity.Airframe.JET_SHAHED);

        public Shahed238(EntityType<? extends PilotedShahedEntity> type, Level level) { super(type, level); }

        @Override protected DroneStats stats() { return STATS; }
    }

    /** Slow, tough, enormous warhead. */
    public static final class Shahed149 extends PilotedShahedEntity {
        private static final DroneStats STATS = new DroneStats(1.8, 2.6, 2.6, 160.0, 2560.0, 600, true,
                () -> SwarmItems.SHAHED_149.get(), DebrisEntity.Airframe.SHAHED);

        public Shahed149(EntityType<? extends PilotedShahedEntity> type, Level level) { super(type, level); }

        @Override protected DroneStats stats() { return STATS; }
    }
}
