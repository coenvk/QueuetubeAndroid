package com.arman.queuetube.model.viewholders;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.arman.queuetube.R;
import com.arman.queuetube.activities.MainActivity;
import com.arman.queuetube.model.VideoData;
import com.arman.queuetube.model.adapters.PlaylistItemAdapter;
import com.arman.queuetube.model.adapters.VideoItemAdapter;
import com.arman.queuetube.modules.playlists.PlaylistHelper;
import com.arman.queuetube.util.VideoSharer;

public class PlaylistItemViewHolder extends VideoViewHolder {

    public static final String TAG = "PlaylistItemViewHolder";

    private ImageView optionsButton;
    private PlaylistItemAdapter adapter;

    public PlaylistItemViewHolder(PlaylistItemAdapter adapter, View view) {
        super(view);
        this.adapter = adapter;
        this.optionsButton = (ImageView) view.findViewById(R.id.playlist_item_options_button);
    }

    private void showPopupMenu(View anchorView) {
        final Context context = this.adapter.getContext();
        PopupMenu optionsPopup = new PopupMenu(context, anchorView);
        optionsPopup.getMenuInflater().inflate(R.menu.playlist_item_popup_menu, optionsPopup.getMenu());
        optionsPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MainActivity activity = (MainActivity) context;
                switch (menuItem.getItemId()) {
                    case R.id.playlist_item_option_add_to_queue:
                        activity.addToQueue(PlaylistItemViewHolder.this.item);
                        break;
                    case R.id.playlist_item_option_remove:
                        PlaylistItemViewHolder.this.adapter.remove(PlaylistItemViewHolder.this.item);
                        PlaylistHelper.removeFrom(PlaylistItemViewHolder.this.adapter.getPlaylistName(), PlaylistItemViewHolder.this.item);
                        break;
                    case R.id.playlist_item_option_share:
                        VideoSharer.share(PlaylistItemViewHolder.this.adapter.getContext(), PlaylistItemViewHolder.this.item);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        optionsPopup.show();
    }

    @Override
    public void bind(VideoData item, VideoItemAdapter.OnItemClickListener clickListener, VideoItemAdapter.OnItemDragListener dragListener) {
        super.bind(item, clickListener, dragListener);
        this.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistItemViewHolder.this.showPopupMenu(view);
            }
        });
    }

}
