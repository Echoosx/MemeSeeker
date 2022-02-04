package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MemeSeekerConfig:AutoSavePluginConfig("config") {
    @ValueDescription("Cookie")
    val cookie:String by value()

    @ValueDescription("自定义指令名")
    val alias:Array<String> by value(
        arrayOf(
            "查梗",
            "梗百科",
            "梗科普"
        )
    )
}