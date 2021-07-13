package cn.cubegarden.trackim.listener

import cn.cubegarden.trackim.compass.Updater
import cn.cubegarden.trackim.compass.Updater.trackMap
import cn.cubegarden.trackim.utils.Config
import cn.cubegarden.trackim.utils.TrackimHolder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class InventoryListener: Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory == null || event.inventory.holder !is TrackimHolder) {
            return
        }
        Bukkit.getLogger().warning("监听到InventoryCLick")

        event.isCancelled = true
        val item = event.currentItem

        if (item?.type == Material.PLAYER_HEAD) {


            val meta = event.currentItem!!.itemMeta as SkullMeta
            if (meta.owningPlayer?.isOnline == true) {
                val target = meta.owningPlayer?.player ?: return
                trackMap[event.whoClicked as Player] = target
                event.whoClicked.sendMessage(Config.startTracking.replace(
                    "%player%",
                    target.name
                ))
            } else {
                event.whoClicked.sendMessage(Config.prefix + "对方不在线")
            }

            event.inventory.close()
            return
        } else if (item?.type == Material.BARRIER) {
            Updater.restoreCompass(event.whoClicked as Player)
            if (trackMap.contains(event.whoClicked)) {
                event.inventory.close()
                event.whoClicked.sendMessage(
                    Config.stopTracking.replace(
                        "%player%",
                        trackMap[event.whoClicked]?.name?: "目标玩家"
                    )
                )
                trackMap.remove(event.whoClicked)
                event.inventory.close()
            } else {
                event.inventory.close()
                event.whoClicked.sendMessage(Config.noTracking)
            }
        }
    }
}