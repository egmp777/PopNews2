package com.sevenrabbits.popnews.sync;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.algorithmia.APIException;
import com.algorithmia.AlgorithmException;
import com.algorithmia.Algorithmia;
import com.algorithmia.AlgorithmiaClient;
import com.algorithmia.algo.AlgoFailure;
import com.algorithmia.algo.AlgoResponse;
import com.algorithmia.algo.AlgoSuccess;
import com.algorithmia.algo.Algorithm;
import com.sevenrabbits.popnews.MainActivity;
import com.sevenrabbits.popnews.R;

import static android.content.ContentValues.TAG;
import static com.sevenrabbits.popnews.BuildConfig.ALGORITHMIA_API_KEY;

/**
 * Created by elena on 31/08/17.
 */

public class AlgorithmiaUrlToText extends AsyncTask<Void, Void, AlgoResponse> {


    public AlgorithmiaClient mClient;
    private Algorithm mAlgo;
    private String mInput;
    private AlgoSummarizationTask algorithmiaSummarizationTask;
    private String [] mArticlePieces;

    public AlgorithmiaUrlToText(String apiKey, String algoUrl, String input, String[] articlePieces){


        mClient = Algorithmia.client(apiKey);
        mAlgo = mClient.algo(algoUrl);
        mInput = input;
        mArticlePieces = articlePieces;

    }

    @Override
    protected AlgoResponse doInBackground(Void... params) {

        try {
            AlgoResponse response = mAlgo.pipe(mInput);
            return response;
        } catch(APIException e) {
            // Connection error
            Log.e(TAG, "Algorithmia API Exception", e);
            return null;
        }

    }

    @Override
    protected  void onPreExecute(){
        MainActivity.mLoadingIndicator.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPostExecute(AlgoResponse response) {
        String result="";
        if(response == null) {
            result = "Algorithm Error: network connection failed";
        } else if(response.isSuccess()) {
            AlgoSuccess success = (AlgoSuccess) response;
            try {
                result = response.asString();
                Log.e("getting url2text result", result);


            }catch(AlgorithmException a){
                a.printStackTrace();
            }

        } else {
            AlgoFailure failure = (AlgoFailure) response;
            result = "Algorithm Error: " + failure.error;
            Log.e(result, " error");
        }


        algorithmiaSummarizationTask = new AlgoSummarizationTask(ALGORITHMIA_API_KEY,
                MainActivity.mNewsAdapter.mContext
                .getString(R.string.algo_summarization_url), "",
                result /*articlePieces[2]*/, mArticlePieces);
        algorithmiaSummarizationTask.execute();
       // MainActivity.showArticleDataView();
       // MainActivity.mNewsAdapter.mHolder.mNewsSummaryText.setText(result);
       // MainActivity.mNewsAdapter.articleText = result;

    }


}
