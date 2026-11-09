package dev.swarm.skyfrontswarm.item;

import dev.krutlexik.skyfront.drone.entity.DroneEntity;
import dev.krutlexik.skyfront.drone.item.DroneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

/**
 * A Skyfront {@link DroneItem} that also knows how to be loaded onto the launch rail
 * (see {@code RailLoadHandler}) and shows a short stat tooltip.
 */
public class SwarmDroneItem extends DroneItem {
    private final Supplier<? extends EntityType<? extends DroneEntity>> droneType;
    private final boolean railOnly;
    private final String tooltipKey;

    public SwarmDroneItem(Properties properties,
                          Supplier<? extends EntityType<? extends DroneEntity>> droneType,
                          boolean railOnly, String tooltipKey) {
        super(properties, droneType, railOnly);
        this.droneType = droneType;
        this.railOnly = railOnly;
        this.tooltipKey = tooltipKey;
    }

    /** True for the planes: they only go on a launch rail. */
    public boolean railOnly() {
        return railOnly;
    }

    public EntityType<? extends DroneEntity> droneType() {
        return droneType.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(tooltipKey + ".1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(tooltipKey + ".2").withStyle(ChatFormatting.DARK_GRAY));
        if (railOnly) {
            tooltip.add(Component.translatable("tooltip.skyfront_swarm.rail").withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
