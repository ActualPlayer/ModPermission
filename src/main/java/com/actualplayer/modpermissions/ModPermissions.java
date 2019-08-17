package com.actualplayer.modpermissions;

import com.actualplayer.modpermissions.config.Configuration;
import com.actualplayer.modpermissions.model.MessageTimestamp;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.item.inventory.*;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;

@Plugin(
        id = "modpermissions",
        name = "Mod Permissions",
        description = "Use permissions to disable/enable mods",
        url = "https://github.com/ActualPlayer/ModPermissions",
        authors = {
                "ActualPlayer"
        },
        version = "1.0.0"
)
public class ModPermissions {

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    private Configuration configuration;

    private Map<UUID, MessageTimestamp> usersLastMessages = new HashMap<>();

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException {
        try {
            this.configuration = this.configManager.load().<Configuration>getValue(Configuration.TYPE, Configuration::generateDefault);
            this.configManager.save(this.configManager.createEmptyNode().setValue(Configuration.TYPE, this.configuration));
        } catch (ObjectMappingException | IOException e) {
            this.logger.error("Failed to load the config - Using a default", e);
            this.configuration = Configuration.generateDefault();
        }

        logger.info("Mod Permissions has loaded");
    }

    @Listener
    public void onItemPreview(CraftItemEvent.Preview event) {
        Optional<User> causeOpt = event.getContext().get(EventContextKeys.OWNER);
        if(causeOpt.isPresent()) {
            String itemId = event.getPreview().getDefault().getType().getId();
            User user = causeOpt.get();
            if (!hasPermission(user, itemId, "craft")) {
                event.setCancelled(true);
            }
        }
    }

    @Listener
    public void onItemCraft(CraftItemEvent.Craft event) {
        Optional<User> causeOpt = event.getContext().get(EventContextKeys.OWNER);
        if(causeOpt.isPresent()) {
            String itemId = event.getCrafted().getType().getId();
            User user = causeOpt.get();
            if (!hasPermission(user, itemId, "craft")) {
                event.setCancelled(true);
            }
        }
    }

    @Listener
    public void onBlockInteract(InteractBlockEvent event) {
        if (!((event instanceof InteractBlockEvent.Primary) && configuration.isAllowBreakingOfBlocks())) {
            Optional<User> causeOpt = event.getContext().get(EventContextKeys.OWNER);
            if (causeOpt.isPresent()) {
                String blockId = event.getTargetBlock().getState().getId();
                User user = causeOpt.get();
                if (!hasPermission(user, blockId, "interact")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Listener
    public void onBlockBreakDrop(DropItemEvent.Destruct event) {
        if (!configuration.isAllowItemsToDrop()) {
            Optional<User> causeOpt = event.getContext().get(EventContextKeys.OWNER);
            if (causeOpt.isPresent()) {
                for (Entity entity : event.getEntities()) {
                    try {
                        String itemId = ((Item) entity).getItemType().getId();
                        User user = causeOpt.get();
                        if (!hasPermission(user, itemId, "interact")) {
                            event.setCancelled(true);
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
        }
    }

    @Listener
    public void onItemInteract(InteractItemEvent event) {
        Optional<User> causeOpt = event.getContext().get(EventContextKeys.OWNER);
        if(causeOpt.isPresent()) {
            String itemId = event.getItemStack().getType().getId();
            User user = causeOpt.get();
            if (!hasPermission(user, itemId, "interact")) {
                event.setCancelled(true);
            }
        }
    }

    @Listener
    public void onEntityInteract(InteractEntityEvent event) {
        Optional<User> causeOpt = event.getContext().get(EventContextKeys.OWNER);
        if(causeOpt.isPresent()) {
            String itemId = event.getTargetEntity().getType().getId();
            User user = causeOpt.get();
            if (!hasPermission(user, itemId, "interact")) {
                event.setCancelled(true);
            }
        }
    }

    @Listener
    public void onPreItemPickup(ChangeInventoryEvent.Pickup.Pre event) {
        if (configuration.isBlacklistItemPickup()) {
            try {
                Player player = ((Player) event.getCause().all().get(0));
                String itemId = event.getFinal().get(0).getType().getId();

                if (!hasPermission(player, itemId, "pickup")) {
                    event.setCancelled(true);
                }
            } catch (Exception ignored) {

            }
        }
    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event) {
        if (configuration.isBlacklistItemPickup()) {
            try {
                Player player = ((Player) event.getCause().all().get(0));
                String itemId = event.getTransactions().get(0).getDefault().getState().getId();

                if (!hasPermission(player, itemId, "pickup")) {
                    event.setCancelled(true);
                }
            } catch (Exception ignored) {

            }
        }
    }

    @Listener
    public void onItemMove(ClickInventoryEvent event) {
        try {
            if (!configuration.isAllowItemMove()) {
                Player player = ((Player) event.getCause().all().get(0));
                SlotTransaction fromInventory = null;
                if (event instanceof ClickInventoryEvent.Shift) {
                    Optional<SlotTransaction> fromTransactionOpt = event.getTransactions().stream().filter(st -> st.getFinal().getType().getId().equals("minecraft:air")).findFirst();
                    if (fromTransactionOpt.isPresent()) {
                        fromInventory = fromTransactionOpt.get();
                    }
                } else {
                    fromInventory = event.getTransactions().get(0);
                }

                if (fromInventory != null) {
                    if (!(fromInventory.getSlot().transform().parent() instanceof PlayerInventory)) {
                        for (SlotTransaction transaction : event.getTransactions()) {
                            String itemId = transaction.getOriginal().getType().getId();

                            if (!hasPermission(player, itemId, "move")) {
                                event.setCancelled(true);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }

    private boolean hasPermission(User user, String itemId, String action) {
        String modId = itemId.substring(0, itemId.indexOf(':'));

        if (!modId.equals("minecraft")) {
            boolean blacklisted = user.hasPermission("modpermissions.blacklist." + modId + "." + action);
            Optional<Player> playerOpt = user.getPlayer();
            if (playerOpt.isPresent() && blacklisted) {
                Player player = playerOpt.get();
                String messageText = configuration.getInformationMessage().replace("%m", modId);
                MessageTimestamp lastMessage = usersLastMessages.get(player.getUniqueId());
                boolean canSend = true;
                if(lastMessage != null) {
                    if (lastMessage.getMessage().equals(messageText) && lastMessage.getTimestamp().until(LocalTime.now(), SECONDS) < 5) {
                        canSend = false;
                    }
                }

                if (canSend) {
                    Text message = Text.builder(messageText).color(TextColors.RED).build();
                    player.sendMessage(message);
                    usersLastMessages.put(player.getUniqueId(), new MessageTimestamp(messageText, LocalTime.now()));
                }

            }

            return !blacklisted;
        }

        return true;
    }
}
