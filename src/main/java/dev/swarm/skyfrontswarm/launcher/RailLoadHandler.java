package dev.swarm.skyfrontswarm.launcher;

import dev.krutlexik.skyfront.drone.entity.InterceptorDroneEntity;
import dev.krutlexik.skyfront.entity.LauncherEntity;
import dev.krutlexik.skyfront.entity.Placement;
import dev.swarm.skyfrontswarm.SkyfrontSwarm;
import dev.swarm.skyfrontswarm.item.SwarmDroneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Skyfront's launch rail only accepts its own Shahed and Molniya items. This lets it take the
 * Swarm planes too: right-click the rail with one and it is seated exactly like a Molniya
 * (same rail point, same 12.5 degree climb angle), then you link a monitor and fly it off.
 */
@EventBusSubscriber(modid = SkyfrontSwarm.MODID)
public final class RailLoadHandler {
    private static Method occupiedMethod;
    private static Field occupantField;
    private static boolean reflectionTried;

    private RailLoadHandler() {}

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof LauncherEntity launcher)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof SwarmDroneItem item) || !item.railOnly()) {
            return;
        }
        Player player = event.getEntity();
        if (player.isShiftKeyDown()) {
            return; // sneaking picks the rail up, as normal
        }
        Level level = event.getLevel();
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        if (!(level instanceof ServerLevel server)) {
            return;
        }

        if (occupied(launcher, server)) {
            player.displayClientMessage(
                    Component.translatable("msg.skyfront.launcher.occupied").withStyle(ChatFormatting.RED), true);
            return;
        }
        Entity created = item.droneType().create(server);
        if (!(created instanceof InterceptorDroneEntity drone)) {
            return;
        }
        drone.seatOnRail(launcher.railPoint(0.5).add(0.0, 0.02, 0.0), launcher.getYRot(), 12.5F);
        if (!Placement.fits(server, drone, launcher)) {
            Placement.noRoom(player);
            return;
        }
        server.addFreshEntity(drone);
        setOccupant(launcher, drone.getUUID());
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    // -- the rail keeps its seated plane in private members; reach them without a hard dependency --

    private static void prepareReflection() {
        if (reflectionTried) {
            return;
        }
        reflectionTried = true;
        try {
            occupiedMethod = LauncherEntity.class.getDeclaredMethod("occupied", ServerLevel.class);
            occupiedMethod.setAccessible(true);
            occupantField = LauncherEntity.class.getDeclaredField("occupant");
            occupantField.setAccessible(true);
        } catch (ReflectiveOperationException | RuntimeException e) {
            SkyfrontSwarm.LOGGER.warn("Could not reach LauncherEntity internals, falling back to a proximity check", e);
            occupiedMethod = null;
            occupantField = null;
        }
    }

    private static boolean occupied(LauncherEntity launcher, ServerLevel level) {
        prepareReflection();
        if (occupiedMethod != null) {
            try {
                return (Boolean) occupiedMethod.invoke(launcher, level);
            } catch (ReflectiveOperationException | RuntimeException ignored) {
                // fall through to the proximity check
            }
        }
        return !level.getEntitiesOfClass(InterceptorDroneEntity.class,
                launcher.getBoundingBox().inflate(3.0), InterceptorDroneEntity::isRailSeated).isEmpty();
    }

    private static void setOccupant(LauncherEntity launcher, UUID id) {
        prepareReflection();
        if (occupantField != null) {
            try {
                occupantField.set(launcher, id);
            } catch (ReflectiveOperationException | RuntimeException ignored) {
                // the plane is still seated; the rail just will not track it
            }
        }
    }
}
