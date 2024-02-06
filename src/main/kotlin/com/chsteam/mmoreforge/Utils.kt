package com.chsteam.mmoreforge

import io.lumine.mythic.lib.api.item.NBTItem
import net.Indyuce.mmoitems.ItemStats
import net.Indyuce.mmoitems.api.ReforgeOptions
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem
import net.Indyuce.mmoitems.api.util.MMOItemReforger
import net.Indyuce.mmoitems.stat.component.Mergeable
import net.Indyuce.mmoitems.stat.data.UpgradeData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


object Utils {

    val reforgeOptions = ReforgeOptions(true, true, true, true, true, true, true, true, false, false, true, true)
    val rerollOptions = ReforgeOptions(true, true, true, true, true, true, true, true, true, true, true)

    fun reforgeItems(itemStack: ItemStack, player: Player, reforgeOptions: ReforgeOptions) : ItemStack? {
        val reforge = MMOItemReforger(itemStack)
        reforge.setPlayer(player)
        reforge.reforge(reforgeOptions)
        
        return reforge.result
    }

    fun canReforge(itemStack: ItemStack) : Boolean {
        val liveMMOItem = LiveMMOItem(NBTItem.get(itemStack))

        if(liveMMOItem.type.id !in MMOReforge.conf.getStringList("AllowType") || liveMMOItem.id in MMOReforge.conf.getStringList("Blacklist")) return false
        return MMOItemReforger(itemStack).canReforge()
    }

    fun getTier(nbtItem: NBTItem) : String {
        val liveMMOItem = LiveMMOItem(nbtItem)
        return liveMMOItem.tier?.name ?: "无"
    }

    fun getMoney(nbtItem: NBTItem, type: String) : Double {
        val liveMMOItem = LiveMMOItem(nbtItem)
        val tier = liveMMOItem.tier?.id ?: "null"

        return MMOReforge.conf.getDouble("$type.$tier", 0.0)
    }
}