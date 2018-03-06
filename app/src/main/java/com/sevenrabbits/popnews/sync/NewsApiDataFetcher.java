package com.sevenrabbits.popnews.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sevenrabbits.popnews.BuildConfig;
import com.sevenrabbits.popnews.MainActivity;
import com.sevenrabbits.popnews.NewsAdapter;
import com.sevenrabbits.popnews.utils.BuildArrayUtil;
import com.sevenrabbits.popnews.utils.NetworkUtils;
import com.sevenrabbits.popnews.utils.NewsApiJsonUtility;

import java.net.URL;

/**
 * Created by elena on 6/28/17.
 */

public class NewsApiDataFetcher extends AsyncTask<String, Void, String[]> {

    private ConnectivityManager cm;
    private int vInfoToGet;
    private String vSortCriterion;
    private String vSource;
    private boolean haveConnection = true;
    private MainActivity mMainActivity;
    private String [] oneNewsPieceInfo;
    //Need next 2 lines to call Algorithmia summary algorithm frome here
    private static final String ALGORITHMIA_API_KEY =
            BuildConfig.ALGORITHMIA_API_KEY;
    private AlgoSummarizationTask algorithmiaTask;
    private NewsAdapter mNewsAdapter;

    public NewsApiDataFetcher(AppCompatActivity activity,
                              String sortCriterion, int infoToGet,
                              String source){
        cm = (ConnectivityManager) activity.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        vInfoToGet = infoToGet;
        vSortCriterion = sortCriterion;
        vSource = source;
        mNewsAdapter = new NewsAdapter();
        mMainActivity= (MainActivity)activity;


    }
    @Override
    protected void onPreExecute(){

        oneNewsPieceInfo = null;

        mMainActivity.mLoadingIndicator.setVisibility(View.VISIBLE);
    }
    @Override
    protected String[] doInBackground(String... params) {
        if(NetworkUtils.networkConnectionAvailable(cm) ) {
            if (params.length == 0) {
                return null;
            }
            String api_key = params[0];
            URL newsRequestUrl = NetworkUtils.buildUrl(vSortCriterion,
                    api_key, vInfoToGet, vSource);
            try{
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl
                    (newsRequestUrl);

            String [] simpleJsonDataArray = null;
            switch (vInfoToGet) {
                case 0:
                    break;
                case 1:
                    simpleJsonDataArray = NewsApiJsonUtility.
                            getNewsDetailsStringFromJson(mMainActivity,
                                    jsonResponse);
                    break;
            }
                return simpleJsonDataArray;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }else{
           haveConnection = false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] data) {

        oneNewsPieceInfo = BuildArrayUtil.buildDataArray(data[0]);


        mMainActivity.mLoadingIndicator.setVisibility(View.INVISIBLE);


        mMainActivity.mNewsAdapter.setArticleData(data);


        Log.e("Post Execute", "Count of items in adapter"+mMainActivity.mNewsAdapter.getItemCount());

        //Algorithmia execution lines to use input from newsApi article description
        //TODO August 29 2017 Comment out
       // mMainActivity.headlineText.setText(oneNewsPieceInfo[1]);


        //mMainActivity.summaryText.setText(oneNewsPieceInfo[2]);
       //TODO August 30 2017 Comment out
        //algorithmiaTask = new AlgoTask(ALGORITHMIA_API_KEY,mMainActivity.getBaseContext()
          //      .getString(R.string.algo_url), "", oneNewsPieceInfo[2]);
        //algorithmiaTask.execute();

    }

}
