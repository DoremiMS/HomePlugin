package com.dorechan.home

import com.dorechan.home.commands.HomeCommand
import org.bukkit.plugin.java.JavaPlugin

class HomePlugin: JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        val version = description.version
        logger.info("Enable $name $version")
        val homeCommand = HomeCommand(config, this)
        getCommand("home")?.setExecutor(homeCommand)
        getCommand("home")?.tabCompleter = homeCommand
        saveDefaultConfig()
    }
    
    override fun onDisable() {
        logger.info("Disable $name")
    }
}