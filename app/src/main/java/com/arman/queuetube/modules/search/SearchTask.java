package com.arman.queuetube.modules.search;

import android.os.AsyncTask;

import com.arman.queuetube.fragments.SearchFragment;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.adapters.VideoItemAdapter;

import java.util.Arrays;
import java.util.List;

public class SearchTask extends AsyncTask<String, Integer, List<VideoData>> {

    private YouTubeSearcher ytSearcher;
    private SearchFragment searchFragment;

    public SearchTask(YouTubeSearcher ytSearcher, SearchFragment searchFragment) {
        super();
        this.ytSearcher = ytSearcher;
        this.searchFragment = searchFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<VideoData> videoData) {
        super.onPostExecute(videoData);
        this.searchFragment.getResultsAdapter().setAll(videoData);
        if (!videoData.isEmpty()) {
            this.searchFragment.showResults();
            this.searchFragment.scrollToTop();
        } else {
            this.searchFragment.showEmptyText();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(List<VideoData> videoData) {
        super.onCancelled(videoData);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected List<VideoData> doInBackground(String... strings) {
        List<VideoData> search = this.ytSearcher.search(strings[0]);
        return search;
    }

}
