package com.mineinabyss.features.custom_hud

import kr.toxicity.hud.api.BetterHud
import org.bukkit.Bukkit

class CustomHudHelper

//internal val mythicHud by lazy { Bukkit.getPluginManager().getPlugin("MythicHUD") as MythicHUD }
//private val LAYOUT_KEY = NamespacedKey.fromString("hud_layouts", mythicHud)!!
//private val Player.activeLayouts get() = persistentDataContainer.get(LAYOUT_KEY, PersistentDataType.TAG_CONTAINER)?.keys?.mapNotNull { mythicHud.layouts().get(it.key) } ?: emptyList()
//fun CustomHudFeature.customHudEnabled(player: Player) = this.customHudTemplate in player.packyData.enabledPackAddons.map { it.id }
//
//val Player.hudHolder: HudHolder get() = HudHolder.get(this) ?: HudHolder(this)
//fun toggleBackgroundLayouts(player: Player, feature: CustomHudFeature) {
//    val backgroundLayout = mythicHud.layouts().layouts.find { it.key == feature.backgroundLayout } ?: return
//    val oldActiveLayouts = player.activeLayouts.toMutableList().minus(backgroundLayout)
//
//    player.hudHolder.let { hudHolder ->
//        // Clear layouts and add backgrounds back in if they were enabled
//        oldActiveLayouts.forEach(hudHolder::removeLayout)
//        when {
//            player.customHudData.showBackgrounds -> hudHolder.addLayout(backgroundLayout)
//            else -> hudHolder.removeLayout(backgroundLayout)
//        }
//        oldActiveLayouts.forEach(hudHolder::addLayout)
//        hudHolder.send()
//    }
//}

internal val betterhud by lazy { Bukkit.getPluginManager().getPlugin("BetterHud") as BetterHud }
