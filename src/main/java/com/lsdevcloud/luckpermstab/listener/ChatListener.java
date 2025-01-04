package com.lsdevcloud.luckpermstab.listener;

import com.lsdevcloud.luckpermstab.LuckPermsTab;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Translate color codes if the player has permission
        if (player.hasPermission("chat.color")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        LuckPermsTab plugin = LuckPermsTab.getInstance();
        User user = plugin.getLuckPermsInstance().getUserManager().getUser(player.getUniqueId());

        if (user == null) {
            plugin.getLogger().warning("LuckPerms User not found for player: " + player.getName());
            return;
        }

        String prefix = plugin.getGroupPrefix(user.getPrimaryGroup());
        if (prefix == null) {
            prefix = ""; // default prefix
        }

        String chatFormat = prefix + player.getName() + ChatColor.DARK_GRAY + " ➜ " + ChatColor.GRAY + message;
        event.setFormat(chatFormat);
    }
}
