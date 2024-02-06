package com.chsteam.mmoreforge

import io.lumine.mythic.lib.api.item.NBTItem
import net.milkbowl.vault.economy.Economy
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.module.ui.ClickType
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.module.ui.type.Stored
import taboolib.platform.util.asLangText
import taboolib.platform.util.buildItem
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir

object UI {
    fun openReforge(player: Player, type: String) {
        player.openMenu<Linked<Stored>>(player.asLangText("reforge-title")) {
            rows(1)
            handLocked(false)
            title = when(type) {
                "Reforge" -> player.asLangText("reforge-title")
                else -> player.asLangText("reroll-title")
            }

            var currentItem : ItemStack? =  ItemStack(Material.AIR)
            val canNot = when(type) {
                "Reforge" -> player.asLangText("can-not-reforge")
                else -> player.asLangText("can-not-reroll")
            }
            val start = when(type) {
                "Reforge" -> player.asLangText("start-reforge")
                else -> player.asLangText("start-reroll")
            }

            for(i in 0..8) {
                if(i != 4) {
                    set(i, buildItem(Material.BLACK_STAINED_GLASS_PANE) {
                        name = canNot
                        colored()
                    }
                    ) {
                        if(currentItem != null && currentItem!!.hasItemMeta()) {
                            if (Utils.canReforge(currentItem!!)) {
                                if(MMOReforge.econ!!.getBalance(player) >= Utils.getMoney(NBTItem.get(currentItem), type)) {
                                    MMOReforge.econ!!.withdrawPlayer(player, Utils.getMoney(NBTItem.get(currentItem), type))
                                    if(type == "Reforge") {
                                        val reforgeItem = Utils.reforgeItems(currentItem!!, player, Utils.reforgeOptions)
                                        reforgeItem?.let {
                                            inventory.setItem(4, it)
                                            currentItem = it
                                        }
                                    } else {
                                        val reforgeItem = Utils.reforgeItems(currentItem!!, player, Utils.rerollOptions)
                                        reforgeItem?.let {
                                            inventory.setItem(4, it)
                                            currentItem = it
                                        }
                                    }
                                } else {
                                    player.sendMessage(player.asLangText("not-enough-money"))
                                }
                            }
                        }
                    }
                }
            }

            onClose { e ->
                if(currentItem != null && currentItem.isNotAir()) {
                    e.player.inventory.addItem(currentItem)
                }
            }

            fun inventoryRefresh(inventory: Inventory) {
                if (currentItem != null && currentItem!!.hasItemMeta() && Utils.canReforge(currentItem!!)) {
                    for(i in 0..8) {
                        if(i != 4) {
                            inventory.setItem(i, buildItem(Material.GREEN_STAINED_GLASS_PANE) {
                                name = start
                                lore += player.asLangText("item-tier").replace("#",
                                    Utils.getTier(NBTItem.get(currentItem))
                                )
                                lore += player.asLangText("item-money").replace( "#", "${Utils.getMoney(NBTItem.get(currentItem), type)}")
                                colored()
                            })
                        }
                    }
                } else {
                    for(i in 0..8) {
                        if(i != 4) {
                            inventory.setItem(i, buildItem(Material.BLACK_STAINED_GLASS_PANE) {
                                name = canNot
                                colored()
                            })
                        }
                    }
                }
            }

            onClick { e ->
                if (e.clickType == ClickType.DRAG && e.dragEvent().rawSlots.size > 1) {
                    e.isCancelled = true
                } else {
                    val rawSlot = if (e.clickType == ClickType.DRAG) e.dragEvent().rawSlots.firstOrNull() ?: -1 else e.rawSlot
                    // 点击箱子内部
                    if (rawSlot == 4) {
                        e.isCancelled = true
                        val cursor = player.itemOnCursor
                        when {
                            currentItem.isAir() && cursor.isNotAir() -> {
                                player.setItemOnCursor(null)
                                currentItem = cursor
                                e.inventory.setItem(rawSlot, cursor)

                                inventoryRefresh(e.inventory)
                            }

                            currentItem.isNotAir() && cursor.isAir() -> {
                                player.setItemOnCursor(currentItem)
                                currentItem = ItemStack(Material.AIR)
                                e.inventory.setItem(rawSlot, ItemStack(Material.AIR))

                                inventoryRefresh(e.inventory)
                            }

                            currentItem.isNotAir() && currentItem.isNotAir() -> {
                                player.setItemOnCursor(currentItem)
                                currentItem = cursor
                                e.inventory.setItem(rawSlot, cursor)

                                inventoryRefresh(e.inventory)
                            }
                        }
                    } else if (e.clickType == ClickType.CLICK && e.clickEvent().isShiftClick) {
                        e.isCancelled = true
                    }
                }
            }
        }
    }
}