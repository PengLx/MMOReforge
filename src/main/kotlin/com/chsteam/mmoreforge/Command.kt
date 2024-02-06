package com.chsteam.mmoreforge

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper

@CommandHeader("reforge", ["mmoreforge"] )
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val reforge = subCommand {
        dynamic {
            suggest { Bukkit.getOnlinePlayers().map { it.displayName } }
            execute<CommandSender> { _, arg, _ ->
                Bukkit.getPlayer(arg.argument(0))?.let {
                    UI.openReforge(it, "Reforge")
                }
            }
        }
    }

    @CommandBody
    val reroll = subCommand {
        dynamic {
            suggest { Bukkit.getOnlinePlayers().map { it.displayName } }
            execute<CommandSender> { _, arg, _ ->
                Bukkit.getPlayer(arg.argument(0))?.let {
                    UI.openReforge(it, "Reroll")
                }
            }
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { _,_,_ ->
            MMOReforge.conf.reload()
        }
    }
}