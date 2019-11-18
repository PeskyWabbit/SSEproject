package net.runelite.client.plugins.clientSideNameChange;

import net.runelite.api.*;
import net.runelite.client.account.SessionManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.ColorUtil;

@PluginDescriptor(
        name = "Name Change",
        description = "Change the Player Name and Icon (Client side only)",
        enabledByDefault = false,
        tags = {"name", "change", "namechange"}
)
public class NameChangePlugin extends Plugin {

    @Inject
    private Client client;
    @Inject
    private NameChangeConfig config;
    @Inject
    private SessionManager session;

    @Provides
    NameChangeConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(NameChangeConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        Widget chatboxInput = client.getWidget(WidgetInfo.CHATBOX_INPUT);
        chatboxInput.setText(getPlayerNameWithIcon() + ": " + ColorUtil.wrapWithColorTag(client.getVar(VarClientStr.CHATBOX_TYPED_TEXT) + "*", JagexColors.CHAT_TYPED_TEXT_OPAQUE_BACKGROUND));
        client.refreshChat();
    }

    @Override
    protected void shutDown() throws Exception
    {
        //Widget chatboxInput = client.getWidget(WidgetInfo.CHATBOX_INPUT);
        //final boolean isChatboxTransparent = client.isResized() && client.getVar(Varbits.TRANSPARENT_CHATBOX) == 1;
        //final Color textColor = isChatboxTransparent ? JagexColors.CHAT_TYPED_TEXT_TRANSPARENT_BACKGROUND : JagexColors.CHAT_TYPED_TEXT_OPAQUE_BACKGROUND;
        //chatboxInput.setText(client.getLocalPlayer().getName() + ": " + ColorUtil.wrapWithColorTag(client.getVar(VarClientStr.CHATBOX_TYPED_TEXT) + "*", textColor));
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (!"namechange".equals(event.getGroup()))
        {
            return;
        }
        session.getAccountSession().setUsername(getPlayerNameWithIcon());
        Widget chatboxInput = client.getWidget(WidgetInfo.CHATBOX_INPUT);
        chatboxInput.setText(getPlayerNameWithIcon() + ": " + ColorUtil.wrapWithColorTag(client.getVar(VarClientStr.CHATBOX_TYPED_TEXT) + "*", JagexColors.CHAT_TYPED_TEXT_OPAQUE_BACKGROUND));
        client.refreshChat();
    }

    private String getPlayerNameWithIcon()
    {
        IconID icon;
        switch (config.playerIcon())
        {
            case IRONMAN:
                icon = IconID.IRONMAN;
                break;
            case ULTIMATE_IRONMAN:
                icon = IconID.ULTIMATE_IRONMAN;
                break;
            case HARDCORE_IRONMAN:
                icon = IconID.HARDCORE_IRONMAN;
                break;
            case PLAYER_MOD:
                icon = IconID.PLAYER_MODERATOR;
                break;
            case J_MOD:
                icon = IconID.JAGEX_MODERATOR;
                break;
            default:
                return config.playerName();
        }
        return icon + config.playerName();
    }
}
