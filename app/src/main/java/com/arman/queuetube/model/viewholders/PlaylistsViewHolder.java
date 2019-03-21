package com.arman.queuetube.model.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.config.Constants;
import com.arman.queuetube.model.adapters.BaseTouchAdapter;
import com.arman.queuetube.modules.playlists.GsonPlaylistHelper;

import androidx.annotation.NonNull;

public class PlaylistsViewHolder extends BaseTouchViewHolder<String> {

    private ImageView deleteButton;
    private TextView titleView;

    public PlaylistsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.titleView = (TextView) itemView.findViewById(R.id.playlists_item_title);
        this.deleteButton = (ImageView) itemView.findViewById(R.id.playlists_item_delete_button);
    }

    @Override
    public void bind(String item, BaseTouchAdapter.OnItemClickListener clickListener, BaseTouchAdapter.OnItemDragListener dragListener) {
        super.bind(item, clickListener, dragListener);
        if (item.equals(Constants.Json.Playlist.FAVORITES) || item.equals(Constants.Json.Playlist.HISTORY)) {
            this.deleteButton.setVisibility(View.GONE);
        } else {
            this.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GsonPlaylistHelper.remove(PlaylistsViewHolder.this.titleView.getText().toString());
                }
            });
        }
    }

}
