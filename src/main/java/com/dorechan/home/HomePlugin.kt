package com.dorechan.home

import com.dorechan.home.commands.HomeCommand
import org.bukkit.plugin.java.JavaPlugin

class HomePlugin : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        var version = description.version
        logger.info("Enable $name $version")
        val homeCommand = HomeCommand(config ,this)
        getCommand("home")?.setExecutor((homeCommand))
        getCommand("home")?.setTabCompleter(homeCommand)
        saveDefaultConfig()
    }

    override fun onDisable() {
        logger.info("Disable $name")
    }
}