package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.echoosx.mirai.plugin.command.MemeSeekerCommand

object MemeSeeker : KotlinPlugin(
    JvmPluginDescription(
        id = "org.echoosx.mirai.plugin.MemeSeeker",
        name = "MemeSeeker",
        version = "0.1.0"
    ) {
        author("Echoosx")
    }
) {
    override fun onEnable() {
        MemeSeekerCommand.register()
        logger.info { "MemeSeeker loaded" }
    }
}
