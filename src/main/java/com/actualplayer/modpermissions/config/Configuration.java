package com.actualplayer.modpermissions.config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Configuration {

    public static TypeToken<Configuration> TYPE = TypeToken.of(Configuration.class);

    @Setting(value = "blacklistItemPickup", comment = "When a player does not have permission to a mod, also disable picking up of items on the floor.")
    private boolean blacklistItemPickup = false;

    @Setting(value = "informationMessage", comment = "What message to send to the user when he attempts to use a mod that he does not have permission to use. Leave this empty to disable messaging. %m is the mod id.")
    private String informationMessage = "You do not have access to %m.";

    @Setting(value = "allowBreakingOfBlocks", comment = "Allows breaking of blocks even with the permission to prevent griefing.")
    private boolean allowBreakingOfBlocks = true;

    @Setting(value = "allowItemsToDrop", comment = "If allowBreakingOfBlocks is true, this will also drop the item to the ground. The user will not be able to pick up the item if he has the modpermissions.blacklist.{mod id}.pickup permission")
    private boolean allowItemsToDrop = false;

    public boolean isBlacklistItemPickup() {
        return blacklistItemPickup;
    }

    public void setBlacklistItemPickup(boolean blacklistItemPickup) {
        this.blacklistItemPickup = blacklistItemPickup;
    }

    public String getInformationMessage() {
        return informationMessage;
    }

    public void setInformationMessage(String informationMessage) {
        this.informationMessage = informationMessage;
    }

    public boolean isAllowBreakingOfBlocks() {
        return allowBreakingOfBlocks;
    }

    public void setAllowBreakingOfBlocks(boolean allowBreakingOfBlocks) {
        this.allowBreakingOfBlocks = allowBreakingOfBlocks;
    }

    public boolean isAllowItemsToDrop() {
        return allowItemsToDrop;
    }

    public void setAllowItemsToDrop(boolean allowItemsToDrop) {
        this.allowItemsToDrop = allowItemsToDrop;
    }

    public static Configuration generateDefault() {
        return new Configuration();
    }
}
