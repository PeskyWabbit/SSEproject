package net.runelite.client.plugins.clientSideNameChange;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.api.Client;

@ConfigGroup("namechange")
public interface NameChangeConfig extends Config{

    @ConfigItem(
            keyName = "playerName",
            name = "Player Name",
            description = "Change the players name to be displayed",
            position = 1
    )

    default String playerName() {return "";}

    @ConfigItem(
            keyName = "playerIcon",
            name = "Player Icon",
            description = "Change the icon to be displayed next to the players name in the chat box",
            position = 2
    )
    default NameIcons playerIcon() {return NameIcons.NONE;}
}
