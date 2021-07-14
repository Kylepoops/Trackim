package cn.cubegarden.trackim.listener

import cn.cubegarden.trackim.utils.Config
import cn.cubegarden.trackim.utils.MessageUtils.sendPrefixMessage
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

        if (!event.player.hasPermission("trackim.gui")) {
            event.player.sendPrefixMessage(Config.noPerm)
            return
        }

        val inventory = Bukkit.createInventory(
            TrackimHolder(),
            54,
            Config.title
        )

        initInventory(inventory, event.player, 54)

        event.player.openInventory(inventory)
    }


    fun fillWithHead(inventory: Inventory, playerList: Collection<Player>, exclude: Player, max: Int) {
        playerList.asSequence()
            .filter { it != exclude }
            .map {
                val head = ItemStack(Material.PLAYER_HEAD, 1)
                val meta = head.itemMeta as SkullMeta
                meta.displayName(Component.text(it.name, NamedTextColor.RED))
                meta.owningPlayer = it
                head.itemMeta = meta
                return@map head
            }
            .take(((max/9)-2)*8)
            .forEach {
                inventory.addItem(it)
            }
    }

    fun initInventory(inventory: Inventory, player: Player, max: Int) {
        for (slot in 0 until max) {
            if (slot < 9 || (max - slot) < 9 || slot % 9 == 0 || slot % 9 == 8) {
                inventory.setItem(slot, ItemStack(Material.ORANGE_STAINED_GLASS_PANE))
            }
        }
        fillWithHead(inventory, Bukkit.getOnlinePlayers(), player, 54)
        inventory.setItem(
            max - 1,
            closeItem
        )


    }
}