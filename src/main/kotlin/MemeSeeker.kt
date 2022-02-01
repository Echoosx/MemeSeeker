package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.echoosx.mirai.plugin.MemeSeekerConfig.cookie
import org.echoosx.mirai.plugin.command.MemeSeekerCommand

object MemeSeeker : KotlinPlugin(
    JvmPluginDescription(
        id = "org.echoosx.mirai.plugin.MemeSeeker",
        name = "MemeSeeker",
        version = "1.0.1"
    ) {
        author("Echoosx")
    }
) {
    override fun onEnable() {
        MemeSeekerConfig.reload()
        MemeSeekerCommand.register()
        if(cookie.isEmpty())
            logger.warning("没有填写cookie，请求次数过多可能会受到反爬虫限制")
        else
            logger.info(cookie)
        logger.info { "MemeSeeker loaded" }
    }
}
