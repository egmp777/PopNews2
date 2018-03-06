package com.sevenrabbits.popnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sevenrabbits.popnews.sync.AlgoSummarizationTask;

public class NewsDetails extends AppCompatActivity {
    private static final String ALGORITHMIA_API_KEY = BuildConfig.ALGORITHMIA_API_KEY;
    private AlgoSummarizationTask algorithmiaTask;
    public static TextView summaryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        summaryText = (TextView) findViewById(R.id.summary);
    }
}
