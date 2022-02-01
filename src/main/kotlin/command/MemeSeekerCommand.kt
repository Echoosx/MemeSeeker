package org.echoosx.mirai.plugin.command

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import okhttp3.OkHttpClient
import okhttp3.Request
import org.echoosx.mirai.plugin.MemeSeeker
import org.echoosx.mirai.plugin.utils.searchMeme

object MemeSeekerCommand:SimpleCommand(
    MemeSeeker,
    "meme","梗科普","梗百科","查梗",
    description = "梗查询"
) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    @Handler
    suspend fun CommandSender.handle(vararg args:String){
        try {
            Mutex().withLock {
                delay(1000)
                val keyword = args.joinToString(" ")
                val meme = searchMeme(keyword)
                var message = buildMessageChain { append("${meme.title}：${meme.content}")}

                for(url in meme.url){
                    val client = OkHttpClient()
                    val request = Request.Builder().url(url).get()
                    val response = client.newCall(request.build()).execute()
                    val memeImage = response.body!!.byteStream()
                    val image = memeImage.toExternalResource().use { it.uploadAsImage(user!!) }
                    message += image
                }
                message += meme.ref
                sendMessage(message)
            }
        }catch (e:Throwable){
            sendMessage("查询失败")
            MemeSeeker.logger.error(e)
        }
    }
}