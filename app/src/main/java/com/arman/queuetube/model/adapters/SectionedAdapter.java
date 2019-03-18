package com.arman.queuetube.model.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arman.queuetube.model.Section;
import com.arman.queuetube.model.viewholders.SectionedViewHolder;

import java.util.Arrays;
import java.util.Comparator;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SectionedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private @LayoutRes
    int sectionResId;
    private @IdRes
    int textResId;
    private RecyclerView.Adapter adapter;
    private SparseArray<Section> sections;

    public SectionedAdapter(Context context, @LayoutRes int sectionResId, @IdRes int textResId, final RecyclerView.Adapter adapter) {
        this.context = context;
        this.sectionResId = sectionResId;
        this.textResId = textResId;
        this.adapter = adapter;

        this.adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    public void setSections(Section[] sections) {
        this.sections.clear();
        Arrays.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section section, Section t1) {
                return Integer.compare(section.getStartPosition(), t1.getStartPosition());
            }
        });
        int offset = 0;
        for (Section section : sections) {
            section.setPosition(section.getStartPosition() + offset);
            this.sections.append(section.getPosition(), section);
            ++offset;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(this.sectionResId, viewGroup, false);
            return new SectionedViewHolder(view, this.textResId);
        }
        return this.adapter.onCreateViewHolder(viewGroup, i - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (isSectionHeaderPosition(i)) {
            ((SectionedViewHolder) viewHolder).setHeader(this.sections.get(i).getTitle());
        } else {
            this.adapter.onBindViewHolder(viewHolder, this.sectionedPositionToPosition(i));
        }
    }

    private boolean isSectionHeaderPosition(int i) {
        return this.sections.get(i) != null;
    }

    private int sectionedPositionToPosition(int i) {
        if (this.isSectionHeaderPosition(i)) {
            return RecyclerView.NO_POSITION;
        }
        int offset = 0;
        for (int j = 0; j < this.sections.size(); j++) {
            if (this.sections.valueAt(j).getPosition() > i) {
                break;
            }
            --offset;
        }
        return i + offset;
    }

    public int positionToSectionedPosition(int i) {
        int offset = 0;
        for (int j = 0; j < this.sections.size(); j++) {
            if (this.sections.valueAt(j).getStartPosition() > i) {
                break;
            }
            ++offset;
        }
        return i + offset;
    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position) ? 0 : this.adapter.getItemViewType(this.sectionedPositionToPosition(position)) + 1;
    }

    @Override
    public long getItemId(int position) {
        return this.isSectionHeaderPosition(position) ? Integer.MAX_VALUE - this.sections.indexOfKey(position) : this.adapter.getItemId(this.sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return this.adapter.getItemCount() + this.sections.size();
    }

}
