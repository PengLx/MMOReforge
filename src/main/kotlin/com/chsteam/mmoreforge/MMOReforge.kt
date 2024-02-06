package com.chsteam.mmoreforge

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration


object MMOReforge : Plugin() {

    var econ: Economy? = null

    @Config
    lateinit var conf: Configuration
        private set

    override fun onEnable() {
        info("Successfully running MMOReforge!")
        setupEconomy()
    }

    private fun setupEconomy(): Boolean {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false
        }
        val rsp: RegisteredServiceProvider<Economy> =
            Bukkit.getServer().getServicesManager().getRegistration(Economy::class.java)
                ?: return false
        econ = rsp.provider
        return econ != null
    }
}