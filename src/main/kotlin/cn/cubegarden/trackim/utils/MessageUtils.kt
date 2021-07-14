package cn.cubegarden.trackim.utils

import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player

object MessageUtils {
    fun Player.sendPrefixMessage(message: String) {
        sendMessage(Config.prefix + message)
    }

    fun Player.sendPrefixActionBar(actionBar: String) {
        sendActionBar(actionBar)
    }

    fun Player.sendReplacedActionBar(actionBar: String, replace: String) {
        sendActionBar(Config.prefix + actionBar.replace("%player", replace))
    }

    fun HumanEntity.sendPrefixMessage(message: String) {
        sendMessage(Config.prefix + message)
    }

    fun HumanEntity.sendReplacedMessage(message: String, replace: String) {
        sendMessage(Config.prefix + message.replace("%player%", replace))
    }
}