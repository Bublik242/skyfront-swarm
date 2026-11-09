package dev.swarm.skyfrontswarm;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Server-side tuning for the Swarm drones. Everything here is read on the server only. */
public final class SwarmConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue FORCE_BLOCK_DAMAGE;
    public static final ModConfigSpec.DoubleValue BLAST_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue HITPOINT_MULTIPLIER;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        b.push("explosions");
        FORCE_BLOCK_DAMAGE = b.comment(
                "Swarm drones always destroy blocks when they detonate, even if Skyfront's own",
                "'piloted drones break blocks' option is switched off.")
                .define("forceBlockDamage", true);
        BLAST_MULTIPLIER = b.comment("Multiplier for every Swarm warhead (on top of the built-in per-drone value).")
                .defineInRange("blastMultiplier", 1.0, 0.1, 5.0);
        b.pop();
        b.push("durability");
        HITPOINT_MULTIPLIER = b.comment("Multiplier for every Swarm drone's hitpoints.")
                .defineInRange("hitpointMultiplier", 1.0, 0.1, 10.0);
        b.pop();
        SPEC = b.build();
    }

    private SwarmConfig() {}

    public static boolean forceBlockDamage() {
        try { return FORCE_BLOCK_DAMAGE.get(); } catch (IllegalStateException e) { return true; }
    }

    public static double blastMultiplier() {
        try { return BLAST_MULTIPLIER.get(); } catch (IllegalStateException e) { return 1.0; }
    }

    public static double hitpointMultiplier() {
        try { return HITPOINT_MULTIPLIER.get(); } catch (IllegalStateException e) { return 1.0; }
    }
}
