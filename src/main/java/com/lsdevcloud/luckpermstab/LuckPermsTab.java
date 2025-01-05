package com.lsdevcloud.luckpermstab;

import com.lsdevcloud.luckpermstab.listener.ChatListener;
import com.lsdevcloud.luckpermstab.listener.LuckPermsListeners;
import com.lsdevcloud.luckpermstab.listener.PlayerJoinListener;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class LuckPermsTab extends JavaPlugin {

    @Getter
    private LuckPerms luckPermsInstance;

    @Getter
    private static LuckPermsTab instance;

    private final Map<String, String> groupPrefixCache = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        // check if LuckPerms is available
        final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            getLogger().severe(ChatColor.RED + "LuckPerms is not available, disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        luckPermsInstance = provider.getProvider();

        // register listeners
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);

        new LuckPermsListeners(luckPermsInstance);

        getLogger().info(ChatColor.GREEN + "LuckPermsTab has been enabled successfully!");
    }

    /**
     * Returns the prefix for a group and caches it
     * @param groupName Name of the LuckPerms group
     * @return Group or default prefix
     */
    public String getGroupPrefix(String groupName) {
        if (groupName == null || groupName.isEmpty()) {
            getLogger().warning("Requested prefix for a null or empty group name.");
            return ChatColor.GRAY + ""; // default prefix
        }

        return groupPrefixCache.computeIfAbsent(groupName, name -> {
            Group group = luckPermsInstance.getGroupManager().getGroup(name);
            if (group == null) {
                getLogger().warning("Group not found: " + name);
                return ChatColor.GRAY + ""; // default prefix
            }
            return ChatColor.translateAlternateColorCodes('&', group.getNodes(NodeType.PREFIX).stream()
                    .map(PrefixNode::getMetaValue)
                    .findFirst()
                    .orElse(ChatColor.GRAY + ""));
        });
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.YELLOW + "Shutting down plugin...");
        groupPrefixCache.clear();
    }
}
