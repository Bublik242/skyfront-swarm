package dev.swarm.skyfrontswarm.registry;

import dev.swarm.skyfrontswarm.SkyfrontSwarm;
import dev.swarm.skyfrontswarm.item.SwarmDroneItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class SwarmItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SkyfrontSwarm.MODID);

    public static final DeferredItem<SwarmDroneItem> SHAHED_131 = ITEMS.register("shahed_131",
            () -> new SwarmDroneItem(new Item.Properties().stacksTo(1), SwarmEntities.SHAHED_131::get, true,
                    "tooltip.skyfront_swarm.shahed_131"));
    public static final DeferredItem<SwarmDroneItem> SHAHED_136_PILOT = ITEMS.register("shahed_136_pilot",
            () -> new SwarmDroneItem(new Item.Properties().stacksTo(1), SwarmEntities.SHAHED_136_PILOT::get, true,
                    "tooltip.skyfront_swarm.shahed_136_pilot"));
    public static final DeferredItem<SwarmDroneItem> SHAHED_238_PILOT = ITEMS.register("shahed_238_pilot",
            () -> new SwarmDroneItem(new Item.Properties().stacksTo(1), SwarmEntities.SHAHED_238_PILOT::get, true,
                    "tooltip.skyfront_swarm.shahed_238_pilot"));
    public static final DeferredItem<SwarmDroneItem> SHAHED_149 = ITEMS.register("shahed_149",
            () -> new SwarmDroneItem(new Item.Properties().stacksTo(1), SwarmEntities.SHAHED_149::get, true,
                    "tooltip.skyfront_swarm.shahed_149"));

    public static final DeferredItem<SwarmDroneItem> FPV_HEAVY = ITEMS.register("fpv_heavy",
            () -> new SwarmDroneItem(new Item.Properties().stacksTo(1), SwarmEntities.FPV_HEAVY::get, false,
                    "tooltip.skyfront_swarm.fpv_heavy"));
    public static final DeferredItem<SwarmDroneItem> FPV_RACER = ITEMS.register("fpv_racer",
            () -> new SwarmDroneItem(new Item.Properties().stacksTo(1), SwarmEntities.FPV_RACER::get, false,
                    "tooltip.skyfront_swarm.fpv_racer"));
    public static final DeferredItem<SwarmDroneItem> FPV_LONGRANGE = ITEMS.register("fpv_longrange",
            () -> new SwarmDroneItem(new Item.Properties().stacksTo(1), SwarmEntities.FPV_LONGRANGE::get, false,
                    "tooltip.skyfront_swarm.fpv_longrange"));

    /** Creative-tab order. */
    public static List<DeferredItem<SwarmDroneItem>> all() {
        return List.of(SHAHED_131, SHAHED_136_PILOT, SHAHED_238_PILOT, SHAHED_149,
                FPV_HEAVY, FPV_RACER, FPV_LONGRANGE);
    }

    private SwarmItems() {}
}
