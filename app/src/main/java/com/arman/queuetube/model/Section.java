package com.arman.queuetube.model;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public abstract class Section {

    private State state;

    private boolean visible;
    private String title;
    private int startPosition;
    private int position;

    public Section(int startPosition, String title) {
        this.state = State.LOADED;
        this.visible = true;
        this.title = title;
        this.startPosition = startPosition;
        this.position = 0;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (state) {
            case LOADING:
                onBindLoadingViewHolder(holder, position);
                break;
            case LOADED:
                onBindItemViewHolder(holder, position);
                break;
            case FAILED:
                onBindFailedViewHolder(holder, position);
                break;
            case EMPTY:
                onBindEmptyViewHolder(holder, position);
                break;
            default:
                throw new IllegalStateException("Invalid state");
        }
    }

    public int size() {
        int size = 0;
        switch (state) {
            case LOADED:
                size = getItemCount();
                break;
            case LOADING:
            case FAILED:
            case EMPTY:
                size = 1;
                break;
            default:
                throw new IllegalStateException("Invalid state");
        }
        return size + 1;
    }

    public abstract int getItemCount();

    public abstract View getItemView(ViewGroup parent);

    public abstract RecyclerView.ViewHolder getItemViewHolder(View view);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract View getLoadingView(ViewGroup parent);

    public RecyclerView.ViewHolder getLoadingViewHolder(View view) {
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    public void onBindLoadingViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public abstract View getFailedView(ViewGroup parent);

    public RecyclerView.ViewHolder getFailedViewHolder(View view) {
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    public void onBindFailedViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public abstract View getEmptyView(ViewGroup parent);

    public RecyclerView.ViewHolder getEmptyViewHolder(View view) {
        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    public void onBindEmptyViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public enum State {
        LOADING,
        LOADED,
        FAILED,
        EMPTY,
    }

}
