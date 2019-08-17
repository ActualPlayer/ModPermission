package com.actualplayer.modpermissions.config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Configuration {

    public static TypeToken<Configuration> TYPE = TypeToken.of(Configuration.class);

    @Setting(value = "informationMessage", comment = "What message to send to the user when he attempts to use a mod that he does not have permission to use. Leave this empty to disable messaging. %m is the mod id.")
    private String informationMessage = "You do not have access to %m.";

    @Setting(value = "allowBreakingOfBlocks", comment = "Allows breaking of blocks disregarding any permission to prevent griefing.")
    private boolean allowBreakingOfBlocks = true;

    @Setting(value = "allowItemsToDrop", comment = "If allowBreakingOfBlocks is true, this will also drop the item to the ground. The user will not be able to pick up the item if he has the modpermissions.blacklist.{mod id}.pickup permission")
    private boolean allowItemsToDrop = true;

    @Setting(value = "allowItemMove", comment = "Allows the moving of mod items between inventories. This value is ignored if the user attempts to move items from his inventory.")
    private boolean allowItemMove = true;

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

    public boolean isAllowItemMove() {
        return allowItemMove;
    }

    public void setAllowItemMove(boolean allowItemMove) {
        this.allowItemMove = allowItemMove;
    }

    public static Configuration generateDefault() {
        return new Configuration();
    }
}
