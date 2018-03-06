package com.sevenrabbits.popnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sevenrabbits.popnews.sync.AlgoSummarizationTask;
import com.sevenrabbits.popnews.sync.AlgoUrlToTextTask;
import com.sevenrabbits.popnews.utils.BuildArrayUtil;

/**
 * Created by elena on 29/08/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    public Context mContext;
    private String[] mArticleData;

    //Need next 2 lines to call Algorithmia summary algorithm frome here
    private static final String ALGORITHMIA_API_KEY = BuildConfig.ALGORITHMIA_API_KEY;
    private AlgoSummarizationTask algorithmiaSummarizationTask;
    private AlgoUrlToTextTask algoUrlToTextTask;
    public static NewsAdapterViewHolder mHolder;
    public  String articleText;


    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutForNewsItem = R.layout.news_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutForNewsItem, viewGroup, shouldAttachToParentImmediately);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        String rawArticle = mArticleData[position];
        String [] articlePieces = BuildArrayUtil.buildDataArray(rawArticle);
        Log.e("array filled", "number of pieces="+articlePieces.length);
        mHolder = holder;
        //mHolder.mHeadlineText.setText(articlePieces[1]);
        //mHolder.mNewsSummaryText.setText(articlePieces[2]);

        algoUrlToTextTask = new AlgoUrlToTextTask(ALGORITHMIA_API_KEY, mContext
                .getString(R.string.algo_url2text_url),articlePieces[3],articlePieces );
        algoUrlToTextTask.execute();

        //System.out.println(articleText);
        //algorithmiaSummarizationTask = new AlgoSummarizationTask(ALGORITHMIA_API_KEY,mContext
          //     .getString(R.string.algo_summarization_url), "",
            //   articleText /*articlePieces[2]*/, articlePieces);
        //algorithmiaSummarizationTask.execute();



    }

    @Override
    public int getItemCount() {
        if (mArticleData== null) return 0;
        return mArticleData.length;
    }

    public void setArticleData(String [] articleData){
        mArticleData = articleData;
        notifyDataSetChanged();
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder{
        public final TextView mHeadlineText;
        public final TextView mNewsSummaryText;

        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            mHeadlineText = (TextView) itemView.findViewById(R.id.article_headline);
            mNewsSummaryText = (TextView) itemView.findViewById(R.id.article_summary);

        }
    }

}
