package com.sevenrabbits.popnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.sevenrabbits.popnews.sync.AlgoSummarizationTask;
import com.sevenrabbits.popnews.sync.NewsApiDataFetcher;

public class MainActivity extends AppCompatActivity {

    private static final String NEWS_ORG_API_KEY = BuildConfig.NEWS_API_KEY;


    public  static RecyclerView mRecyclerView;
    public static NewsAdapter mNewsAdapter;
    public static ProgressBar mLoadingIndicator;
    public  static TextView mErrorMessageDisplay;
    private NewsApiDataFetcher dataFetcher;
    private LinearLayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_top_news_main);
        mNewsAdapter = new NewsAdapter();

        dataFetcher = new NewsApiDataFetcher(this,"top",1,"reuters");

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        mRecyclerView = (RecyclerView) findViewById(R.id.top_news_list);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mNewsAdapter);

        loadArticleData();

    }

    public  static void showArticleDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void loadArticleData() {
        showArticleDataView();

        //Llamamos al NewsApi service desde la clase Async
        //NewsApiDataFetcher
        dataFetcher.execute(NEWS_ORG_API_KEY);

    }











    //TODO August 28 2017
    // We will use the NewsApiDatafetcher class as an inner class


}
