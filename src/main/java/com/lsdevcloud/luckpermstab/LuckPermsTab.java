package com.lsdevcloud.luckpermstab;

import com.lsdevcloud.luckpermstab.listener.ChatListener;
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

        final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            getLogger().severe("LuckPerms is not available, disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        luckPermsInstance = provider.getProvider();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    public String getGroupPrefix(String groupName) {
        if (groupName == null) {
            return null;
        }

        return groupPrefixCache.computeIfAbsent(groupName, name -> {
            Group group = luckPermsInstance.getGroupManager().getGroup(name);
            if (group == null) {
                return null;
            }
            return ChatColor.translateAlternateColorCodes('&', group.getNodes(NodeType.PREFIX).stream()
                    .map(PrefixNode::getMetaValue)
                    .findFirst()
                    .orElse(""));
        });
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
