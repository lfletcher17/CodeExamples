package com.example.android.eventsuggester;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EventPerformerAdapter extends RecyclerView.Adapter<EventPerformerAdapter.EventPerformerAdapterViewHolder> {

    private ArrayList<SongKickArtist> mPerformerData;
    private final EventPerformerAdapter.EventPerformerAdapterOnClickHandler mClickHandler;

    public EventPerformerAdapter(EventPerformerAdapter.EventPerformerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface EventPerformerAdapterOnClickHandler {
        void onClick(SongKickArtist selectedArtist);
    }

    @Override
    public EventPerformerAdapter.EventPerformerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.event_performer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new EventPerformerAdapter.EventPerformerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventPerformerAdapter.EventPerformerAdapterViewHolder holder, int position) {
        SongKickArtist artist = mPerformerData.get(position);
        holder.mEventPerformer.setText(artist.getName());
    }

    @Override
    public int getItemCount() {
        if (mPerformerData == null) {
            return 0;
        }
        return mPerformerData.size();
    }

    public void setEventData(ArrayList<SongKickArtist> performerData) {
        mPerformerData = performerData;
        notifyDataSetChanged();
    }


    public class EventPerformerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mEventPerformer;

        public EventPerformerAdapterViewHolder (View view) {
            super(view);
            mEventPerformer = (TextView) view.findViewById(R.id.event_performer);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            SongKickArtist selectedArtist = mPerformerData.get(adapterPosition);
            mClickHandler.onClick(selectedArtist);
        }
    }
}
