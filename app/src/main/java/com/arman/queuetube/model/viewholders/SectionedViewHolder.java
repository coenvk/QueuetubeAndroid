package com.arman.queuetube.model.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SectionedViewHolder extends RecyclerView.ViewHolder {

    private TextView sectionHeader;

    public SectionedViewHolder(@NonNull View itemView, @IdRes int textResId) {
        super(itemView);
        this.sectionHeader = (TextView) itemView.findViewById(textResId);
    }

    public void setHeader(String header) {
        this.sectionHeader.setText(header);
    }

}
