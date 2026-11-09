package dev.swarm.skyfrontswarm.entity;

import dev.krutlexik.skyfront.entity.DebrisEntity;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Per-variant numbers. Hitpoints / blast / vehicle damage are multipliers on Skyfront's own Molniya
 * values, so the addon follows whatever balance the server already configured.
 */
public record DroneStats(
        double hitpointMul,
        double blastMul,
        double vehicleMul,
        double topSpeedKmh,
        double controlRange,
        int flightSeconds,
        boolean longLink,
        Supplier<? extends Item> item,
        DebrisEntity.Airframe debris) {
}
