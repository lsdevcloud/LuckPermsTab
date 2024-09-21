package com.lsdevcloud.luckpermstab.listener;

import com.lsdevcloud.luckpermstab.LuckPermsTab;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatListener implements Listener {

    @EventHandler
    public void on(final AsyncPlayerChatEvent event) {

        final Player player = event.getPlayer();
        String message = event.getMessage();

        if (player.hasPermission("chat.color")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        final LuckPermsTab plugin = LuckPermsTab.getInstance();
        final User user = plugin.getLuckPermsInstance().getUserManager().getUser(player.getUniqueId());

        if (user != null) {
            final Group group = plugin.getLuckPermsInstance().getGroupManager().getGroup(user.getPrimaryGroup());

            if (group == null) return;
            event.setFormat(plugin.getGroupPrefix(group.getName()) + player.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + message);
        }
    }
}
