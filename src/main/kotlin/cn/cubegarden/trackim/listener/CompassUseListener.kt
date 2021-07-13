package cn.cubegarden.trackim.listener

import cn.cubegarden.trackim.utils.TrackimHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class CompassUseListener: Listener {

    private val closeItem = ItemStack(Material.BARRIER)

    init {
        val mate = closeItem.itemMeta
        mate.displayName(Component.text("停止追踪", NamedTextColor.RED))
        closeItem.itemMeta = mate
    }

    @EventHandler
    fun onCompassUse(event: PlayerInteractEvent) {
        if (event.item?.type != Material.COMPASS || event.action != Action.RIGHT_CLICK_AIR || !event.player.isSneaking) {
            return
        }

        val inventory = Bukkit.createInventory(
            TrackimHolder(),
            54,
            Component.text("请选择你要追踪的目标")
                .color(NamedTextColor.RED)
        )

        fillWithHead(inventory, Bukkit.getOnlinePlayers(), event.player)
        inventory.setItem(
            53,
            closeItem
        )

        event.player.openInventory(inventory)
    }


    fun fillWithHead(inventory: Inventory, playerList: Collection<Player>, exclude: Player) {
        playerList.toSet()
            .filter { it != exclude }
            .map {
                val head = ItemStack(Material.PLAYER_HEAD, 1)
                val meta = head.itemMeta as SkullMeta
                meta.owningPlayer = it
                head.itemMeta = meta
                return@map head
            }
            .take(53)
            .forEach {
                inventory.addItem(it)
            }
    }
}