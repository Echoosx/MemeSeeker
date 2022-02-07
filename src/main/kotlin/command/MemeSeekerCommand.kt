package org.echoosx.mirai.plugin.command

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import okhttp3.OkHttpClient
import okhttp3.Request
import org.echoosx.mirai.plugin.MemeSeeker
import org.echoosx.mirai.plugin.MemeSeekerConfig.alias
import org.echoosx.mirai.plugin.utils.NoResultException
import org.echoosx.mirai.plugin.utils.searchMeme
import org.jsoup.HttpStatusException

object MemeSeekerCommand:SimpleCommand(
    owner = MemeSeeker,
    primaryName = "meme",
    secondaryNames = alias,
    description = "梗查询"
) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    @Suppress("unused")
    @Handler
    suspend fun CommandSender.handle(vararg args:String){
        try {
            val keyword = args.joinToString(" ")
            val message_ = coroutineScope {
                async {
                    val meme = searchMeme(keyword)
                    var message = buildMessageChain { append("${meme.title}：${meme.content}") }
                    for (url in meme.image) {
                        val client = OkHttpClient()
                        val request = Request.Builder().url(url).get()
                        val response = client.newCall(request.build()).execute()
                        val memeImage = response.body!!.byteStream()
                        var image: Image? = null
                        try{image = memeImage.toExternalResource().use { it.uploadAsImage(user!!) }}
                        catch(_:IllegalArgumentException){}
                        if(image != null)
                            message += image
                    }
                    for (ref in meme.reference){
                        message += "\n《${ref.text}》${ref.link}"
                    }
                    return@async message
                }
            }

            val mutex = Mutex()
            mutex.withLock {
                delay(1_000)
                sendMessage(message_.await())
            }

        }catch(e: HttpStatusException){
            if(e.statusCode == 429)
                sendMessage("查询过于频繁了，请慢一点~❤")
            else
                throw e
        }catch(e: NoResultException){
            sendMessage("未查询到相关词条")
        }
        catch (e:Throwable){
            sendMessage("查询失败")
            MemeSeeker.logger.error(e)
        }
    }
}