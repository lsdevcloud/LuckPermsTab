package com.lsdevcloud.luckpermstab.listener;

import com.lsdevcloud.luckpermstab.LuckPermsTab;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LuckPermsTab plugin = LuckPermsTab.getInstance();

        // Retrieve LuckPerms user
        User user = plugin.getLuckPermsInstance().getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            plugin.getLogger().warning("LuckPerms User not found for player: " + player.getName());
            return;
        }

        // Retrieve group and prefix
        Group group = plugin.getLuckPermsInstance().getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) {
            plugin.getLogger().warning("Group not found for user: " + user.getUsername());
            return;
        }

        String prefix = plugin.getGroupPrefix(group.getName());
        setPlayerDisplayName(prefix, player);
    }

    public static void setPlayerDisplayName(final String prefix, final Player player) {
        if (prefix == null) {
            LuckPermsTab.getInstance().getLogger().warning("Prefix is null for player: " + player.getName());
            return;
        }

        String displayName = prefix + player.getName();
        player.setDisplayName(displayName);
        player.setPlayerListName(displayName);
    }
}
