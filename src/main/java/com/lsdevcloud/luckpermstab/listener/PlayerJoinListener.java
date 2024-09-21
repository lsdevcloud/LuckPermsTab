package com.lsdevcloud.luckpermstab.listener;

import com.lsdevcloud.luckpermstab.LuckPermsTab;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LuckPermsTab plugin = LuckPermsTab.getInstance();
        User user = plugin.getLuckPermsInstance().getUserManager().getUser(player.getUniqueId());

        if (user != null) {
            Group group = plugin.getLuckPermsInstance().getGroupManager().getGroup(user.getPrimaryGroup());

            if (group == null) return;
            setPlayerDisplayName(group.getName(), player);
        }
    }

    public static void setPlayerDisplayName(final String group, final Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("LuckPermsTab-group-" + group);

        if (team == null) {
            team = scoreboard.registerNewTeam("LuckPermsTab-group-" + group);
            team.setPrefix(LuckPermsTab.getInstance().getGroupPrefix(group));
            team.setColor(ChatColor.GRAY);
        }

        team.addEntry(player.getName());
        player.setScoreboard(scoreboard);
    }
}
