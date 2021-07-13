package cn.cubegarden.trackim.compass

import cn.cubegarden.trackim.Main
import cn.cubegarden.trackim.utils.Config
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World.Environment.THE_END
import org.bukkit.World.Environment.NETHER
import org.bukkit.World.Environment.NORMAL
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.NotNull

object Updater {
    val trackMap = HashMap<Player, Player>()
    val deceptionSet = HashSet<Player>()

    fun start() {
        object : BukkitRunnable() {
            override fun run() {
                for (entry in trackMap) {

                    val tracker = entry.key
                    val trackee = entry.value

                    if (tracker.isOnline && trackee.isOnline) {

                        if (!tracker.inventory.contains(Material.COMPASS)) return


                        val trackerEnv = tracker.world.environment
                        val trackeeEnv = trackee.world.environment
                        val trackerLoc = tracker.location
                        val trackeeLoc = trackee.location

                        if (
                            (trackerEnv == THE_END ||
                                    trackeeEnv == THE_END)
                            && trackerEnv != trackeeEnv
                        ) return

                        if (Config.maxDistance != -1 && trackerLoc.distance(trackeeLoc) > Config.maxDistance) return

                        val location = getRandom(tracker, getLocation(trackerLoc, trackeeLoc))
                        updateActionBar(tracker, trackee.name)
                        for (itemEntry in tracker.inventory.all(Material.COMPASS)) {
                            val compass = itemEntry.value

                            val meta = compass.itemMeta as CompassMeta
                            meta.isLodestoneTracked = false
                            meta.lodestone = location

                            compass.itemMeta = meta
                        }

                    }
                }
            }
        }.runTaskTimer(Main.INSTANCE, 600, 20)
    }

    fun getLocation(tracker: Location, target: Location): @NotNull Location {

        val trackerEnv = tracker.world.environment
        val trackeeEnv = target.world.environment

        if (trackeeEnv == NORMAL &&
            trackeeEnv == NETHER
        ) {
            target.multiply(0.125)
            target.world = tracker.world
        } else if (trackerEnv == NETHER &&
            trackeeEnv == NORMAL
        ) {
            target.multiply(8.0)
            target.world = tracker.world
        }

        return target

    }

    fun updateActionBar(tracker: Player, playerName: String) {
        val message = Config.actionbar
            .replace("%player%", playerName)
        tracker.sendActionBar(message)
    }

    fun getRandom(tracker: Player, location: Location): Location {
        if ((!deceptionSet.contains(tracker)) && Math.random() < 0.01) {
            deceptionSet.add(tracker)
            Bukkit.getLogger().info(Config.prefix + "玩家${tracker.name}已加入干扰列表")
            object : BukkitRunnable(){
                override fun run() {
                    deceptionSet.remove(tracker)
                    Bukkit.getLogger().info(Config.prefix + "玩家${tracker.name}已从干扰列表移除")
                }
            }.runTaskLater(Main.INSTANCE, 3600)
        }

        if (deceptionSet.contains(tracker)) {
            location.x *= Math.random()
            location.z *= Math.random()
        }

        return location

    }
}