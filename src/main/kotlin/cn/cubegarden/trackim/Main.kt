package cn.cubegarden.trackim

import cn.cubegarden.trackim.compass.Updater
import cn.cubegarden.trackim.listener.CompassUseListener
import cn.cubegarden.trackim.listener.InventoryListener
import cn.cubegarden.trackim.utils.Config
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    override fun onLoad() {
        logger.info("loaded")
    }

    override fun onEnable() {
        INSTANCE = getPlugin(Main::class.java)

        Config.load()
        Bukkit.getPluginManager().run {
            registerEvents(CompassUseListener(), this@Main)
            registerEvents(InventoryListener(), this@Main)
        }

        Updater.start()

    }

    override fun onDisable() {
        logger.info("disabled")
    }

    companion object {
        lateinit var INSTANCE: Plugin
    }
}