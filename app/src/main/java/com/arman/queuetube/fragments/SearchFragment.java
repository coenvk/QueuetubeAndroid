package com.arman.queuetube.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arman.queuetube.R;
import com.arman.queuetube.model.adapters.VideoItemAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {

    private LinearLayout emptyTextLayout;

    private RecyclerView resultsView;
    private VideoItemAdapter resultsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private VideoItemAdapter.OnItemClickListener onItemClickListener;

    public VideoItemAdapter getResultsAdapter() {
        return resultsAdapter;
    }

    public void scrollToTop() {
        if (this.resultsView != null && this.layoutManager != null) {
            this.layoutManager.smoothScrollToPosition(this.resultsView, null, 0);
        }
    }

    public void setOnItemClickListener(VideoItemAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void showEmptyText() {
        this.resultsView.setVisibility(View.GONE);
        this.emptyTextLayout.setVisibility(View.VISIBLE);
    }

    public void showResults() {
        this.emptyTextLayout.setVisibility(View.GONE);
        this.resultsView.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.emptyTextLayout = (LinearLayout) view.findViewById(R.id.search_results_empty_text_layout);

        this.resultsView = (RecyclerView) view.findViewById(R.id.search_results);
        this.resultsView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.resultsView.setLayoutManager(this.layoutManager);

        this.resultsAdapter = new VideoItemAdapter(getActivity(), this.onItemClickListener);

        this.resultsView.setAdapter(this.resultsAdapter);
    }

}
