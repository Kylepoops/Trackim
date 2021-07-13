package cn.cubegarden.trackim.utils

import cn.cubegarden.trackim.Main
import org.bukkit.configuration.file.FileConfiguration

object Config {
    lateinit var prefix: String
    private val config = Main.INSTANCE.config
    lateinit var actionbar: String
    var maxDistance = -1
    lateinit var startTracking: String
    lateinit var stopTracking: String
    lateinit var noTracking: String
    lateinit var title: String

    fun load() {
        Main.INSTANCE.saveDefaultConfig()
        prefix = config.getStringColored("prefix")?: "[Trackim]"
        actionbar = prefix + (config.getStringColored("actionbar")?: "你正在追踪%player%")
        maxDistance = config.getInt("max-distance")
        startTracking = prefix + (config.getStringColored("start-tracking")?: "正在追踪%player%")
        stopTracking = prefix + (config.getStringColored("stop-tracking")?: "已停止追踪%player%")
        noTracking = prefix + (config.getStringColored("no-tracking")?: "你不在追踪任何玩家")
        title = config.getStringColored("title")?: "请选择你要追踪的玩家"
    }

    private fun FileConfiguration.getStringColored(path: String): String? {
        return this.getString(path)?.replace('&', '§')
    }
}