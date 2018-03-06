package com.sevenrabbits.popnews.utils;

import android.net.ConnectivityManager;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by elena on 6/28/17.
 */

public class NetworkUtils {
    private static final String API_KEY = "apiKey";
    private static final String DEFAULT_SORT_CRITERION = "top";
    private static final String NEWS_API_ARTICLES_URL = "https://newsapi.org/v1/articles";
    private static final String NEWS_API_SOURCES_URL = "https://newsapi.org/v1/sources";
    private static final String SOURCE_PARAM = "source";
    private static final String ARTICLE_SORT_PARAM= "sortBy";
    private static final String CATEGORY_FOR_SOURCES_PARAM = "category";
    private static final String COUNTRY_FOR_SOURCES_PARAM = "country";
    private static final String LANGUAGE_FOR_SOURCES_PARAM= "language";
    private static final String DEFAULT_LANGUAGE_FOR_SOURCES = "en";
    public static Process ipProcess;


    public static URL buildUrl(String sortCriterion, String api_key, int infoToGet, String source) {
        URL url = null;
        //Can be a list of sources or a list of articles from a source
        switch (infoToGet) {
            case 0: //sources
                if (sortCriterion == null || sortCriterion == "") {
                    sortCriterion = DEFAULT_SORT_CRITERION;
                }
                Uri builtUri = Uri.parse(NEWS_API_SOURCES_URL).buildUpon()
                        .appendQueryParameter(LANGUAGE_FOR_SOURCES_PARAM,DEFAULT_LANGUAGE_FOR_SOURCES )
                        .build();

                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case 1:default: // list of articles from a source

                builtUri = Uri.parse(NEWS_API_ARTICLES_URL ).buildUpon()
                        .appendQueryParameter(SOURCE_PARAM, source)
                        .appendQueryParameter(ARTICLE_SORT_PARAM, sortCriterion)
                        .appendQueryParameter(API_KEY, api_key)
                        .build();


                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;


        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    /**
     * This mmethod checks if there is network availability
     *
     * @param cm the ConnectivityManager
     * @return The boolean response of checking network connection availability
     *
     */
    public static boolean networkConnectionAvailable(ConnectivityManager cm) {


        Runtime runtime = Runtime.getRuntime();

        try {
            ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;

    }

}
