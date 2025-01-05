package com.lsdevcloud.luckpermstab.listener;

import com.lsdevcloud.luckpermstab.LuckPermsTab;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * Created: 05.01.25
 *
 * @author lsdevcloud (leo@lsdevcloud.com)
 */
public final class LuckPermsListeners
{

    public LuckPermsListeners(LuckPerms luckPerms)
    {
        EventBus eventBus = luckPerms.getEventBus();

        eventBus.subscribe(LuckPermsTab.getInstance(), UserDataRecalculateEvent.class, e -> {

            final var playerId = e.getUser().getUniqueId();
            if(Bukkit.getOnlinePlayers().stream().noneMatch(player -> player.getUniqueId().equals(playerId))) return;
            LuckPermsTab plugin = LuckPermsTab.getInstance();

            Group group = plugin.getLuckPermsInstance().getGroupManager().getGroup(e.getUser().getPrimaryGroup());

            if (group == null) {
                plugin.getLogger().warning("Group not found for user: " + e.getUser().getUsername());
                return;
            }

            // update player name
            PlayerJoinListener.setPlayerDisplayName(group, plugin.getGroupPrefix(group.getName()), Bukkit.getPlayer(playerId));
        });
    }


}
