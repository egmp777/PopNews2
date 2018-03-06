package com.sevenrabbits.popnews.utils;

/**
 * Created by elena on 6/27/17.
 */
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class NewsApiJsonUtility {


    /**
     * This method converts the JSON response into a String array
     * @param context
     * @param newsJsonStr
     * @return String array thet holds each element of the movie info that comes in the JSON response
     * @throws JSONException
     */
    public static String [] getNewsDetailsStringFromJson(Context context, String newsJsonStr)
            throws JSONException {
        final String NEWSAPI_STATUS="status";
        final String NEWSAPI_ARTICLES = "articles";
        final String NEWSAPI_ARTICLE_AUTHOR="author";
        final String NEWSAPI_ARTICLE_TITLE="title";
        final String NEWSAPI_ARTICLE_DESCRIPTION="description";
        final String NEWSAPI_ARTICLE_URL="url";
        final String NEWSAPI_URI_TO_IMAGE="urlToImage";
        final String NEWSAPI_URI_PUBLISHED_AT="publishedAt";

        String [] parsedData=null;
        String status = null;

        JSONObject newsJsonObject = new JSONObject(newsJsonStr);


        if(newsJsonObject.has(NEWSAPI_STATUS)) {
            status = newsJsonObject.getString(NEWSAPI_STATUS);
            if (!(status.equals("ok"))) {
                return null;
            }
        }
            JSONArray articles = newsJsonObject.getJSONArray(NEWSAPI_ARTICLES);
            parsedData = new String[articles.length()];

            for(int i = 0; i < articles.length(); i++){
                JSONObject article = articles.getJSONObject(i);
                String author = article.getString(NEWSAPI_ARTICLE_AUTHOR);
                String title = article.getString(NEWSAPI_ARTICLE_TITLE);
                String description = article.getString(NEWSAPI_ARTICLE_DESCRIPTION);
                String url = article.getString(NEWSAPI_ARTICLE_URL);
                String uriToImage = article.getString(NEWSAPI_URI_TO_IMAGE);
                String publishedAt = article.getString(NEWSAPI_URI_PUBLISHED_AT);
                //TODO for presentation 7RabbitsApps
                //System.out.println(author+"\n"+title+"\n"+description+"\n"
                //  +url+"\n"+uriToImage+"\n"+publishedAt+"\n"+"\n");

                parsedData[i]= author + "\n" + title + "\n" + description +
                        "\n" + url + "\n" + uriToImage + "\n" + publishedAt;
            }
        return parsedData;
    }

}
