package com.mineinabyss.features.patreons

import com.mineinabyss.components.cosmetics.CosmeticVoucher
import com.mineinabyss.components.playerData
import com.mineinabyss.components.players.Patreon
import com.mineinabyss.features.helpers.CoinFactory
import com.mineinabyss.features.helpers.luckPerms
import com.mineinabyss.geary.annotations.optin.UnsafeAccessors
import com.mineinabyss.geary.papermc.tracking.entities.toGeary
import com.mineinabyss.geary.papermc.tracking.items.gearyItems
import com.mineinabyss.geary.papermc.tracking.items.helpers.GearyItemPrefabQuery
import com.mineinabyss.geary.papermc.tracking.items.inventory.toGeary
import com.mineinabyss.geary.prefabs.PrefabKey
import com.mineinabyss.idofront.commands.arguments.optionArg
import com.mineinabyss.idofront.commands.arguments.stringArg
import com.mineinabyss.idofront.commands.extensions.actions.playerAction
import com.mineinabyss.idofront.features.Feature
import com.mineinabyss.idofront.features.FeatureDSL
import com.mineinabyss.idofront.messaging.error
import com.mineinabyss.idofront.messaging.success
import com.mineinabyss.idofront.plugin.listeners
import com.mineinabyss.idofront.serialization.ItemStackSerializer
import kotlinx.serialization.Serializable
import net.luckperms.api.context.ImmutableContextSet
import net.luckperms.api.node.types.PrefixNode
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.time.Month
import java.util.*

class PatreonFeature(val config: Config) : Feature {
    override val dependsOn = setOf("LuckPerms")

    @Serializable
    class Config(
        val enabled: Boolean = false,
        val token: @Serializable(ItemStackSerializer::class) ItemStack? = CoinFactory.mittyToken
    )

    override fun FeatureDSL.enable() {
        plugin.listeners(PatreonListener())

        mainCommand {
            "patreon"(desc = "Patreon-supporter related commands") {
                ("token" / "kit")(desc = "Redeem kit") {
                    playerAction {
                        val player = sender as Player
                        if (config.token == null) return@playerAction
                        if (player.toGeary().get<Patreon>()?.tier == 0) return@playerAction player.error("This command is only for Patreon supporters!")

                        val patreon = player.toGeary().get<Patreon>() ?: return@playerAction
                        val month = Month.of(Calendar.getInstance().get(Calendar.MONTH) + 1)

                        if (patreon.kitUsedMonth == month) {
                            player.error("You can only redeem this once a month.")
                            return@playerAction
                        }

                        player.playerData.mittyTokensHeld += patreon.tier
                        patreon.kitUsedMonth = month
                    }
                }
                "prefix"(desc = "Change your prefix emote") {
                    "remove" {
                        playerAction {
                            val player = sender as Player
                            val console = Bukkit.getServer().consoleSender
                            Bukkit.dispatchCommand(console, "luckperms user ${player.name} meta clear prefix")
                        }
                    }
                    "set" {
                        val emote by stringArg()
                        val locs = listOf(
                            "global",
                            "orth",
                            "layerone",
                            "layertwo",
                            "layerthree",
                            "layerfour",
                            "layerfive",
                            "nazarick",
                            "camelot"
                        )
                        val loc by optionArg(locs) {
                            default = "global"
                            parseErrorMessage = { "No such enchantment: $passed. \nAvailable ones are: \n$locs" }
                        }
                        playerAction {
                            val player = sender as Player

                            if (loc == "global") {
                                listOf(
                                    PrefixNode.builder("§eOrth :$emote: §e-", 10)
                                        .context(ImmutableContextSet.of("worldguard:region", "orth")).build(),
                                    PrefixNode.builder("§cEdge :$emote: §c-", 11)
                                        .context(ImmutableContextSet.of("worldguard:region", "layerone")).build(),
                                    PrefixNode.builder("§9Forest :$emote: §9-", 12)
                                        .context(ImmutableContextSet.of("worldguard:region", "layertwo")).build(),
                                    PrefixNode.builder("§dFault :$emote: §d-", 13)
                                        .context(ImmutableContextSet.of("worldguard:region", "layerthree")).build(),
                                    PrefixNode.builder("§5Goblets :$emote: §5-", 14)
                                        .context(ImmutableContextSet.of("worldguard:region", "layerfour")).build(),
                                    PrefixNode.builder("§8Sea :$emote: §8-", 15)
                                        .context(ImmutableContextSet.of("worldguard:region", "layerfive")).build(),
                                    PrefixNode.builder("§8Nazarick :$emote: §8-", 16)
                                        .context(ImmutableContextSet.of("world", "nazarick")).build(),
                                    PrefixNode.builder("§3Camelot :$emote: §3-", 17)
                                        .context(ImmutableContextSet.of("world", "camelot")).build()
                                ).forEach { node ->
                                    luckPerms.userManager.getUser(player.uniqueId)?.data()?.add(node)
                                }

                            } else {
                                val c: PrefixNode = when (loc) {
                                    "orth" -> PrefixNode.builder("§eOrth :$emote: §e-", 10)
                                        .context(ImmutableContextSet.of("worldguard:region", "orth")).build()

                                    "layerone" -> PrefixNode.builder("§cEdge :$emote: §c-", 11)
                                        .context(ImmutableContextSet.of("worldguard:region", "layerone")).build()

                                    "layertwo" -> PrefixNode.builder("§9Forest :$emote: §9-", 12)
                                        .context(ImmutableContextSet.of("worldguard:region", "layertwo")).build()

                                    "layerthree" -> PrefixNode.builder("§dFault :$emote: §d-", 13)
                                        .context(ImmutableContextSet.of("worldguard:region", "layerthree")).build()

                                    "layerfour" -> PrefixNode.builder("§5Goblets :$emote: §5-", 14)
                                        .context(ImmutableContextSet.of("worldguard:region", "layerfour")).build()

                                    "layerfive" -> PrefixNode.builder("§8Sea :$emote: §8-", 15)
                                        .context(ImmutableContextSet.of("worldguard:region", "layerfive")).build()

                                    "nazarick" -> PrefixNode.builder("§8Nazarick :$emote: §8-", 16)
                                        .context(ImmutableContextSet.of("world", "nazarick")).build()

                                    "camelot" -> PrefixNode.builder("§3Camelot :$emote: §3-", 17)
                                        .context(ImmutableContextSet.of("world", "camelot")).build()

                                    else -> PrefixNode.builder().build()
                                }
                                luckPerms.userManager.getUser(player.uniqueId)?.data()?.add(c)
                            }
                            luckPerms.userManager.saveUser(luckPerms.userManager.getUser(player.uniqueId)!!)
                        }
                    }
                }
            }
        }
        val locs = listOf(
                "global",
                "orth",
                "layerone",
                "layertwo",
                "layerthree",
                "layerfour",
                "layerfive",
                "nazarick",
                "camelot",
            )
        tabCompletion {
            when (args.size) {
                1 -> listOf("patreon").filter { it.startsWith(args[0]) }
                2 -> when (args[0]) {
                    "patreon" -> listOf("prefix", "token").filter { it.startsWith(args[1]) }
                    else -> null
                }
                3 -> when (args[1]) {
                    "prefix" -> listOf("remove", "set").filter { it.startsWith(args[2]) }
                    else -> null
                }
                4 -> when (args[2]) {
                    "set" -> listOf("kekw", "pogo", "pogyou", "pog").filter { it.startsWith(args[3]) }
                    else -> null
                }
                5 -> when (args[2]) {
                    "set" -> locs.filter { it.startsWith(args[4]) }
                    else -> null
                }
                else -> null
            }
        }
    }
}
