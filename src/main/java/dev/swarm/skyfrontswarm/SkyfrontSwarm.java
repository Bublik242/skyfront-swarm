package dev.swarm.skyfrontswarm;

import dev.krutlexik.skyfront.item.ModCreativeTabs;
import dev.swarm.skyfrontswarm.registry.SwarmEntities;
import dev.swarm.skyfrontswarm.registry.SwarmItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(SkyfrontSwarm.MODID)
public class SkyfrontSwarm {
    public static final String MODID = "skyfront_swarm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public SkyfrontSwarm(IEventBus modBus, ModContainer container) {
        SwarmEntities.ENTITIES.register(modBus);
        SwarmItems.ITEMS.register(modBus);
        modBus.addListener(SkyfrontSwarm::addToCreativeTabs);
        container.registerConfig(ModConfig.Type.COMMON, SwarmConfig.SPEC);
    }

    private static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ModCreativeTabs.SKYFRONT_TAB.getKey())
                || event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            SwarmItems.all().forEach(item -> event.accept(item.get()));
        }
    }
}
