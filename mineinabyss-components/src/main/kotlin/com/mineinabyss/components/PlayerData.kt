package com.mineinabyss.components

import com.mineinabyss.geary.papermc.tracking.entities.toGeary
import com.mineinabyss.geary.serialization.getOrSetPersisting
import com.mineinabyss.geary.serialization.setPersisting
import com.mineinabyss.idofront.serialization.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

@Serializable
@SerialName("mineinabyss:player_data")
class PlayerData(
    var isAffectedByCurse: Boolean = true,
    var curseAccrued: Double = 0.0,
    var exp: Double = 0.0,
    var keepInvStatus: Boolean = true,
    var showPvpPrompt: Boolean = true,
    var pvpUndecided: Boolean = true,
    var pvpStatus: Boolean = false,
    var orthCoinsHeld: Int = 0,
    var mittyTokensHeld: Int = 0,
    var showPlayerBalance: Boolean = true,
    var displayProfileArmor: Boolean = true,
    var defaultDisplayLockState: Boolean = false,
    var recentInteractEntity: @Serializable(with = UUIDSerializer::class) UUID? = null,
    var replant: Boolean = true,
) {
    val level: Int get() = exp.toInt() / 10 //TODO write a proper formula

    fun addExp(exp: Double) {
        this.exp += exp
    }

    fun getRecentEntity(): Entity? {
        return recentInteractEntity?.let { Bukkit.getEntity(it) }
    }
}

@Deprecated(
    "Use editPlayerData when writing, or getPlayerDataOrNull when just reading",
    ReplaceWith("playerDataOrNull")
)
val Player.playerData get() = toGeary().getOrSetPersisting<PlayerData> { PlayerData() }

/**
 * Edit [PlayerData], ensuring it is saved back to the entity right after modifying.
 */
inline fun <T> Player.editPlayerData(edit: PlayerData.() -> T): T {
    val entity = toGeary()
    val playerData = entity.get<PlayerData>() ?: PlayerData()
    val returned = playerData.run(edit)
    entity.setPersisting(playerData)
    return returned
}

val Player.playerDataOrNull get() = toGeary().get<PlayerData>()

// TODO Feels a bit dangerous to add while playerData is mutable, consider later
//val Player.playerDataOrDefault get() = playerDataOrNull ?: PlayerData()
