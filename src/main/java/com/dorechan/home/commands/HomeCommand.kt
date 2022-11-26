package com.dorechan.home.commands

import com.dorechan.home.HomePlugin
import org.bukkit.*
import org.bukkit.command.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class HomeCommand(private val config: FileConfiguration, private val homePlugin: HomePlugin): CommandExecutor,
                                                                                              TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        
        when (args.getOrNull(0)) {
            "set"   ->set(args, sender)
            "tp"    ->tp(args, sender)
            "remove"->remove(args, sender)
            else    ->return false
        }
        return true
    }
    
    private fun remove(args: Array<out String>, sender: Player) {
        val homeName = args.getOrNull(1) ?: "Home"
        config.set("HomePlugin.${sender.uniqueId}.$homeName", null)
        homePlugin.saveConfig()
        sender.sendMessage("${ChatColor.WHITE}Your ${ChatColor.AQUA}$homeName ${ChatColor.WHITE}is removed")
        sender.playSound(sender.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
    }
    
    private fun tp(args: Array<out String>, sender: Player) {
        if (args.getOrNull(1) != null) {
            val teleportName = args[1]
            val homeLocation = config.getLocation("HomePlugin.${sender.uniqueId}.$teleportName")
            if (homeLocation == null)
                sender.sendMessage("${ChatColor.WHITE}$teleportName is not set")
            else
                sender.teleport(homeLocation)
            sender.sendMessage("${ChatColor.WHITE}Teleporting to ${ChatColor.AQUA}$teleportName")
            sender.spawnParticle(Particle.DRAGON_BREATH, sender.location, 60, 1.0, 2.0, 1.0, 0.2)
        } else {
            val homeLocation = config.getLocation("HomePlugin.${sender.uniqueId}.Home")
            if (homeLocation == null)
                sender.sendMessage("${ChatColor.WHITE}Home is not set")
            else
                sender.teleport(homeLocation)
            sender.sendMessage("${ChatColor.WHITE}Teleporting to ${ChatColor.AQUA}Home")
            sender.spawnParticle(Particle.DRAGON_BREATH, sender.location, 60, 1.0, 2.0, 1.0, 0.2)
        }
    }
    
    private fun set(args: Array<out String>, sender: Player) {
        if (args.getOrNull(1) != null) {
            val playerLocation = sender.location
            val homeName = args[1]
            config.set("HomePlugin.${sender.uniqueId}.$homeName", playerLocation)
            homePlugin.saveConfig()
            sender.sendMessage("${ChatColor.WHITE}Home Set at ${ChatColor.AQUA}X: ${playerLocation.blockX} Y: ${playerLocation.blockY} Z: ${playerLocation.blockZ} ${ChatColor.WHITE}in ${ChatColor.AQUA}$homeName")
            sender.playSound(sender.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
        } else {
            val playerLocation = sender.location
            config.set("HomePlugin.${sender.uniqueId}.Home", playerLocation)
            homePlugin.saveConfig()
            sender.sendMessage("${ChatColor.WHITE}Home Set at ${ChatColor.AQUA}X: ${playerLocation.blockX} Y: ${playerLocation.blockY} Z: ${playerLocation.blockZ} ${ChatColor.WHITE}in ${ChatColor.AQUA}Home")
            sender.playSound(sender.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
        }
    }
    
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): MutableList<String> {
        if (sender !is Player) return mutableListOf()
        if (args.size == 1) return mutableListOf("tp", "set", "remove")
        if (args.size == 2) {
            if (args[0] == "set") return mutableListOf()
            
            return config.getConfigurationSection("HomePlugin.${sender.uniqueId}")!!.getKeys(false)
                .toMutableList()
        }
        return mutableListOf()
    }
}
