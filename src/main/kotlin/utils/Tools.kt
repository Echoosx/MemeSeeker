package org.echoosx.mirai.plugin.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Meme{
    var title:String = ""
    var content:String = ""
    var url:MutableList<String> = arrayListOf()
}

fun searchMeme(keyword:String):Meme{
    val meme = Meme()
    val document: Document = Jsoup.connect("https://jikipedia.com/search")
        .data("phrase",keyword)
        .timeout(3000)
        .get()

    val box = document.select("div.masonry>div")[0]
    val link = box.select("a.title-container").attr("href")
    getDetail(meme,link)
    return meme
}

fun getDetail(meme:Meme, link:String){
    val document: Document = Jsoup.connect(link)
        .timeout(3000)
        .get()
    meme.title = document.select("div.title-container-content>h1").text()
    val contentNodes = document.select("div.content>div>*")
    val imagNodes = document.select("div.show-images>img.show-images-img")
    var content = ""
    for(line in contentNodes){
        content += line.text()
    }
    meme.content = content
    for(img in imagNodes){
        val url = img.attr("src")
        if(url!=""){
            meme.url.add(url)
        }
    }
}