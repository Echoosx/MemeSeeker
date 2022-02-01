package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MemeSeekerConfig:AutoSavePluginConfig("config") {
    @ValueDescription("Cookie")
    val cookie:String by value()
}