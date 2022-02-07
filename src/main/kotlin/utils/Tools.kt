package org.echoosx.mirai.plugin.utils

import org.echoosx.mirai.plugin.MemeSeekerConfig.cookie
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Meme{
    var title:String = ""
    var content:String = ""
    var image:MutableList<String> = arrayListOf()
    var reference:MutableList<Reference> = arrayListOf()
}

class Reference{
    var text:String = ""
    var link:String = ""
}

fun searchMeme(keyword:String):Meme{
    val meme = Meme()
    val document: Document = Jsoup.connect("https://jikipedia.com/search")
        .header("cookie",cookie)
        .data("phrase",keyword)
        .get()

    val box = document.select("div.masonry>div[data-category='definition']")[0]
    val link = box.select("a.title-container").attr("href")
    getDetail(meme,link)
    return meme
}

fun getDetail(meme:Meme, link:String){
    val document: Document = Jsoup.connect(link)
        .header("cookie",cookie)
        .get()

    val card = document.select("div.full-card")
    meme.title = card.select("div.title-container-content>h1").text()
    val contentNodes = card.select("div.content>div>*")
    val imagNodes = card.select("div.show-images>img.show-images-img")
    var content = ""
    for(line in contentNodes){
        content += line.text()
    }
    meme.content = content
    for(img in imagNodes){
        val url = img.attr("src")
        if(url!=""){
            meme.image.add(url)
        }
    }

    val refNodes = card.select("div.reference-card")
    for(refNode in refNodes) {
        val reference = Reference()
        reference.text = refNode.select("a").text()
        reference.link = refNode.select("a").attr("href")
        meme.reference.add(reference)
    }
}