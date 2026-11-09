package dev.swarm.skyfrontswarm.registry;

import dev.swarm.skyfrontswarm.SkyfrontSwarm;
import dev.swarm.skyfrontswarm.entity.FpvVariantEntity;
import dev.swarm.skyfrontswarm.entity.PilotedShahedEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SwarmEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, SkyfrontSwarm.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<PilotedShahedEntity.Shahed131>> SHAHED_131 =
            ENTITIES.register("shahed_131", () -> plane(PilotedShahedEntity.Shahed131::new, "shahed_131", 3.2f));
    public static final DeferredHolder<EntityType<?>, EntityType<PilotedShahedEntity.Shahed136>> SHAHED_136_PILOT =
            ENTITIES.register("shahed_136_pilot", () -> plane(PilotedShahedEntity.Shahed136::new, "shahed_136_pilot", 4.2f));
    public static final DeferredHolder<EntityType<?>, EntityType<PilotedShahedEntity.Shahed238>> SHAHED_238_PILOT =
            ENTITIES.register("shahed_238_pilot", () -> plane(PilotedShahedEntity.Shahed238::new, "shahed_238_pilot", 3.4f));
    public static final DeferredHolder<EntityType<?>, EntityType<PilotedShahedEntity.Shahed149>> SHAHED_149 =
            ENTITIES.register("shahed_149", () -> plane(PilotedShahedEntity.Shahed149::new, "shahed_149", 5.2f));

    public static final DeferredHolder<EntityType<?>, EntityType<FpvVariantEntity.Heavy>> FPV_HEAVY =
            ENTITIES.register("fpv_heavy", () -> quad(FpvVariantEntity.Heavy::new, "fpv_heavy"));
    public static final DeferredHolder<EntityType<?>, EntityType<FpvVariantEntity.Racer>> FPV_RACER =
            ENTITIES.register("fpv_racer", () -> quad(FpvVariantEntity.Racer::new, "fpv_racer"));
    public static final DeferredHolder<EntityType<?>, EntityType<FpvVariantEntity.LongRange>> FPV_LONGRANGE =
            ENTITIES.register("fpv_longrange", () -> quad(FpvVariantEntity.LongRange::new, "fpv_longrange"));

    private SwarmEntities() {}

    /** Same shape as Skyfront's Molniya registration: wide, 1 block tall, updated every tick. */
    private static <T extends Entity> EntityType<T> plane(EntityType.EntityFactory<T> factory, String name, float width) {
        return EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(width, 1.0f)
                .clientTrackingRange(32)
                .updateInterval(1)
                .build(name);
    }

    /** Same shape as Skyfront's FPV registration. */
    private static <T extends Entity> EntityType<T> quad(EntityType.EntityFactory<T> factory, String name) {
        return EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(0.85f, 0.5f)
                .clientTrackingRange(24)
                .updateInterval(1)
                .build(name);
    }
}
