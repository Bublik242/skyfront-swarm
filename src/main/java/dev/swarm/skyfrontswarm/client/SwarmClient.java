package dev.swarm.skyfrontswarm.client;

import dev.krutlexik.skyfront.drone.client.render.DroneItemRenderer;
import dev.krutlexik.skyfront.drone.client.render.DroneRenderer;
import dev.krutlexik.skyfront.drone.entity.DroneEntity;
import dev.swarm.skyfrontswarm.SkyfrontSwarm;
import dev.swarm.skyfrontswarm.registry.SwarmEntities;
import dev.swarm.skyfrontswarm.registry.SwarmItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/** Draws the Swarm drones with Skyfront's own GLB renderer, using this addon's models. */
@EventBusSubscriber(modid = SkyfrontSwarm.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SwarmClient {
    private SwarmClient() {}

    private static ResourceLocation model(String name) {
        return ResourceLocation.fromNamespaceAndPath(SkyfrontSwarm.MODID, "models/entity/" + name + ".glb");
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // planes: model as authored, no extra pitch trim
        plane(event, SwarmEntities.SHAHED_131.get(), "shahed_131", 1.4f);
        plane(event, SwarmEntities.SHAHED_136_PILOT.get(), "shahed_136_pilot", 1.5f);
        plane(event, SwarmEntities.SHAHED_238_PILOT.get(), "shahed_238_pilot", 1.5f);
        plane(event, SwarmEntities.SHAHED_149.get(), "shahed_149", 1.6f);
        // quads: same size and lean as Skyfront's FPV
        quad(event, SwarmEntities.FPV_HEAVY.get(), "fpv_heavy", 1.3f);
        quad(event, SwarmEntities.FPV_RACER.get(), "fpv_racer", 1.0f);
        quad(event, SwarmEntities.FPV_LONGRANGE.get(), "fpv_longrange", 1.2f);
    }

    private static <T extends DroneEntity> void plane(EntityRenderersEvent.RegisterRenderers event,
                                                      EntityType<T> type, String name, float scale) {
        ResourceLocation model = model(name);
        event.registerEntityRenderer(type, ctx -> new DroneRenderer<T>(ctx, model, scale, true, DroneRenderer.AS_HELD));
    }

    private static <T extends DroneEntity> void quad(EntityRenderersEvent.RegisterRenderers event,
                                                     EntityType<T> type, String name, float scale) {
        ResourceLocation model = model(name);
        event.registerEntityRenderer(type,
                ctx -> new DroneRenderer<T>(ctx, model, scale, true, DroneRenderer.gentlerLean(0.45f)));
    }

    @SubscribeEvent
    public static void registerItemIcons(RegisterClientExtensionsEvent event) {
        icon(event, "shahed_131", SwarmItems.SHAHED_131.get());
        icon(event, "shahed_136_pilot", SwarmItems.SHAHED_136_PILOT.get());
        icon(event, "shahed_238_pilot", SwarmItems.SHAHED_238_PILOT.get());
        icon(event, "shahed_149", SwarmItems.SHAHED_149.get());
        icon(event, "fpv_heavy", SwarmItems.FPV_HEAVY.get());
        icon(event, "fpv_racer", SwarmItems.FPV_RACER.get());
        icon(event, "fpv_longrange", SwarmItems.FPV_LONGRANGE.get());
    }

    private static void icon(RegisterClientExtensionsEvent event, String name, Item item) {
        DroneItemRenderer renderer = new DroneItemRenderer(model(name));
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        }, item);
    }
}
