package com.yasic.diycode.Model;

import android.util.Log;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yasic.diycode.Bean.CallbackBean;
import com.yasic.diycode.Bean.TopicDetailBean;
import com.yasic.diycode.Bean.TopicItemBean;
import com.yasic.diycode.Bean.TopicReplyBean;
import com.yasic.diycode.Util.OkhttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasic on 2016/5/18.
 */
public class TopicModel implements ITopicModel {

    @Override
    public CallbackBean getTopicList(String page) {
        OkhttpUtil okhttpUtil = OkhttpUtil.getInstance();
        final Request request = new Request.Builder().url("http://diycode.cc/?page=" + page).build();
        try {
            List<TopicItemBean> topicItemBeanList = new ArrayList<>();
            String title, type, author, lastReply, totalReply, icon, articleSequense;
            Response response = okhttpUtil.okHttpClient.newCall(request).execute();
            Document document = Jsoup.parse(response.body().string());
            Element mainElement = document.getElementById("main");
            Elements rowElements = mainElement.getElementsByClass("row");
            Elements colmdElements = rowElements.get(0).getElementsByClass("col-md-9");
            Elements panelBodyElements = colmdElements.get(0).getElementsByClass("panel-body");
            Elements articleElements = panelBodyElements.get(0).select("div.topic");
            for (int i = 0; i < articleElements.size(); i++){
                articleSequense = articleElements.get(i).className();
                String[] temp = articleSequense.split(" ");
                articleSequense = temp[2];
                temp = articleSequense.split("-");
                articleSequense = temp[1];
                title = articleElements.get(i).select("div.infos").get(0).select("div.title").get(0).getElementsByTag("a").text();
                type = articleElements.get(i).getElementsByClass("info").get(0).getElementsByClass("node").get(0).text();
                author = articleElements.get(i).getElementsByClass("info").get(0).getElementsByClass("hacknews_clear").get(0).text();
                lastReply = "最后由" + articleElements.get(i).getElementsByClass("info").get(0).getElementsByClass("hidden-mobile").get(0).getElementsByTag("a").text() + " "
                        + articleElements.get(i).getElementsByClass("info").get(0).getElementsByClass("hidden-mobile").get(0).getElementsByTag("abbr").text() + "回复";
                totalReply = articleElements.get(i).select("div.count").get(0).getElementsByTag("a").text();
                if (totalReply == ""){
                    totalReply = "0";
                }
                icon = articleElements.get(0).select("div.avatar").get(0).getElementsByClass("hacknews_clear").get(0).getElementsByTag("img").get(0).attr("src");
                topicItemBeanList.add(new TopicItemBean(title, type, icon, author, lastReply, totalReply, articleSequense));
            }
            CallbackBean<List<TopicItemBean>> callbackBean = new CallbackBean<>("0", "", topicItemBeanList);
            return callbackBean;
        }
        catch (Exception e){
            CallbackBean<List<TopicItemBean>> callbackBean = new CallbackBean<>("1", e.getMessage(), null);
            Log.i("Exception", e.getMessage());
            return callbackBean;
        }
    }

    @Override
    public CallbackBean getTopicDetail(String topicSequence) {
        OkhttpUtil okhttpUtil = OkhttpUtil.getInstance();
        final Request request = new Request.Builder().url("http://diycode.cc/topics/" + topicSequence).build();
        try {
            List<TopicDetailBean> topicDetailBeanList = new ArrayList<>();
            String title;
            String publishTime;
            String author;
            String type;
            String watchedNumber;
            String article;
            String headPortrait;
            List<TopicReplyBean> topicReplyBeanList;
            Response response = okhttpUtil.okHttpClient.newCall(request).execute();
            Document document = Jsoup.parse(response.body().string());
            Element mainElement = document.getElementById("main");
            Elements rowElements = mainElement.getElementsByClass("row");
            Elements colmdElements = rowElements.get(0).getElementsByClass("col-md-9");
            Elements topicDetail = colmdElements.get(0).select("div.topic-detail");
            Elements panelHeading = topicDetail.get(0).select("div.panel-heading");
            Elements mediaBody = panelHeading.get(0).getElementsByClass("media-body");
            Elements mediaHeading = mediaBody.get(0).select("h1.media-heading");
            title = mediaHeading.text();

            Elements info = mediaBody.get(0).getElementsByClass("info");
            Elements node = info.get(0).getElementsByClass("node");
            type = node.get(0).text();

            publishTime = info.get(0).getElementsByClass("timeago").attr("title");

            Elements hacknewsClear = info.get(0).select("a[data-author=true]");
            author = hacknewsClear.text();

            Elements avatar = panelHeading.select("div.avatar");
            headPortrait = avatar.get(0).getElementsByClass("hacknews_clear").get(0).getElementsByTag("img").get(0).attr("src");

            Elements panelBody = topicDetail.get(0).select("div.panel-body");
            Elements articleElement = panelBody.get(0).select("article");
            Log.i("articleElement", articleElement.text());
        }
        catch (Exception e){
            Log.i("Exception", e.getMessage());
        }
        return null;
    }
}
