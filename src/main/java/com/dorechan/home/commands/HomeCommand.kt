package com.dorechan.home.commands

import com.dorechan.home.HomePlugin
import org.bukkit.ChatColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class HomeCommand(val config: FileConfiguration, val mainclass: HomePlugin) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            when (args.getOrNull(0)) {
                "set" -> {
                    if (args.getOrNull(1) != null) {
                        val playerLocation = sender.location
                        val homeName = args[1]
                        config.set("HomePlugin.${sender.uniqueId}.$homeName", playerLocation)
                        mainclass.saveConfig()
                        sender.sendMessage("${ChatColor.WHITE}Home Set at ${ChatColor.AQUA}X: ${playerLocation.blockX} Y: ${playerLocation.blockY} Z: ${playerLocation.blockZ} ${ChatColor.WHITE}in ${ChatColor.AQUA}$homeName")
                        sender.playSound(sender.location , Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1F , 1F )
                    } else {
                        val playerLocation = sender.location
                        config.set("HomePlugin.${sender.uniqueId}.Home", playerLocation)
                        mainclass.saveConfig()
                        sender.sendMessage("${ChatColor.WHITE}Home Set at ${ChatColor.AQUA}X: ${playerLocation.blockX} Y: ${playerLocation.blockY} Z: ${playerLocation.blockZ} ${ChatColor.WHITE}in ${ChatColor.AQUA}Home")
                        sender.playSound(sender.location , Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1F , 1F )
                    }
                }

                "tp" -> {
                    if (args.getOrNull(1) != null) {
                        val teleportName = args[1]
                        val homeLocation = config.getLocation("HomePlugin.${sender.uniqueId}.$teleportName")
                        if (homeLocation == null)
                            sender.sendMessage("${ChatColor.WHITE}$teleportName is not set")
                        else
                            sender.teleport(homeLocation)
                        sender.sendMessage("${ChatColor.WHITE}Teleporting to ${ChatColor.AQUA}$teleportName")
                        sender.spawnParticle(Particle.DRAGON_BREATH, sender.location, 60 , 1.0 , 2.0 , 1.0 , 0.2)
                    } else {
                        val homeLocation = config.getLocation("HomePlugin.${sender.uniqueId}.Home")
                        if (homeLocation == null)
                            sender.sendMessage("${ChatColor.WHITE}Home is not set")
                        else
                            sender.teleport(homeLocation)
                        sender.sendMessage("${ChatColor.WHITE}Teleporting to ${ChatColor.AQUA}Home")
                        sender.spawnParticle(Particle.DRAGON_BREATH, sender.location, 60 , 1.0 , 2.0 , 1.0 , 0.2,)
                    }
                }

                "remove" -> {
                    if (args.getOrNull(1) != null) {
                        val homeName = args[1]
                        config.set("HomePlugin.${sender.uniqueId}.$homeName", null)
                        mainclass.saveConfig()
                        sender.sendMessage("${ChatColor.WHITE}Your ${ChatColor.AQUA}$homeName ${ChatColor.WHITE}is removed")
                        sender.playSound(sender.location , Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1F , 1F )
                    } else {
                        config.set("HomePlugin.${sender.uniqueId}.Home", null)
                        mainclass.saveConfig()
                        sender.sendMessage("${ChatColor.WHITE}Your ${ChatColor.AQUA}Home ${ChatColor.WHITE}is removed")
                        sender.playSound(sender.location , Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1F , 1F )
                    }
                }

                else -> {
                    sender.sendMessage("/home <set/tp> <name>")
                }
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        if (args.size == 2) {
            if ((args.getOrNull(0) == "tp")) {
                if (sender is Player) {
                    return config.getConfigurationSection("HomePlugin.${sender.uniqueId}")!!.getKeys(false)
                        .toMutableList()
                }
            } else if ((args.getOrNull(0) == "remove")) {
                if (sender is Player) {
                    return config.getConfigurationSection("HomePlugin.${sender.uniqueId}")!!.getKeys(false)
                        .toMutableList()
                }
            } else {
                return mutableListOf()
            }
        }
        return mutableListOf("tp", "set", "remove")
    }
}
