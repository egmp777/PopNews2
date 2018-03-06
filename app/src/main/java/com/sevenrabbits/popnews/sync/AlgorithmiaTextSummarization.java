package com.sevenrabbits.popnews.sync;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.algorithmia.APIException;
import com.algorithmia.AlgorithmException;
import com.algorithmia.Algorithmia;
import com.algorithmia.algo.AlgoFailure;
import com.algorithmia.algo.AlgoResponse;

import com.algorithmia.AlgorithmiaClient;
import com.algorithmia.algo.AlgoSuccess;
import com.algorithmia.algo.Algorithm;
import com.sevenrabbits.popnews.MainActivity;
import com.sevenrabbits.popnews.R;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import static android.R.id.input;


/**
 * Created by elena on 6/17/17.
 */

public abstract class AlgorithmiaTextSummarization extends AsyncTask<Void,Void, AlgoResponse> {

    private static final String TAG = "AlgorithmiaTask";

    private String algoUrl;
    public AlgorithmiaClient client;
    private Algorithm algo;
    private String mInput;
    private String [] newsArticleItems;

    private String result;
    private String articleFile;
    private String articleText;


    public AlgorithmiaTextSummarization(String api_key, String algoUrl, String textFile, String input, String[] items) {
        super();

        client = Algorithmia.client(api_key);
        algo = client.algo(algoUrl);
        articleFile = textFile;
        mInput = input;
        newsArticleItems = items;


    }


    @Override
    protected  void onPreExecute(){
        MainActivity.mLoadingIndicator.setVisibility(View.VISIBLE);

    }

    @Override
    protected AlgoResponse doInBackground(Void... inputs) {



        if (mInput.isEmpty()) {


            try {
                if (!articleFile.isEmpty() && client.file(articleFile).exists()) {
                    mInput = client.file(articleFile).getString();

                } else {
                    System.out.println("Please check that your file exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



            // Llamamos a API  algorithmia
            try {
                AlgoResponse response = algo.pipe(mInput);
                return response;
            } catch(APIException e) {
                // Connection error
                Log.e(TAG, "Algorithmia API Exception", e);
                return null;
            }

    }

    @Override
    protected void onPostExecute(AlgoResponse response) {

        if(response == null) {
           result = "Algorithm Error: network connection failed";
        } else if(response.isSuccess()) {
            AlgoSuccess success = (AlgoSuccess) response;
            try {
                result = response.asString();

            }catch(AlgorithmException a){
                a.printStackTrace();
            }

        } else {
            AlgoFailure failure = (AlgoFailure) response;
            result = "Algorithm Error: " + failure.error;
        }
        //MainActivity.showArticleDataView();
        MainActivity.mNewsAdapter.mHolder.mHeadlineText.setText(newsArticleItems[1]);
        //TODO for demonstration use mInput as param in next line to see full article
        MainActivity.mNewsAdapter.mHolder.mNewsSummaryText.setText(result);

    }



}
