package com.example.android.eventsuggester;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationAdapterViewHolder> {

    private ArrayList<Location> mLocationData;
    private final LocationAdapterOnClickHandler mClickHandler;

    public LocationAdapter(LocationAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface LocationAdapterOnClickHandler {
        void onClick(Location selectedLocation);
    }

    @Override
    public LocationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.location_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new LocationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationAdapterViewHolder holder, int position) {
        Location location = mLocationData.get(position);
        holder.mLocationTextView.setText(location.toString());
    }

    @Override
    public int getItemCount() {
        if (mLocationData == null) {
            return 0;
        }
        return mLocationData.size();
    }

    public void setLocationData(ArrayList<Location> locationData) {
        mLocationData = locationData;
        notifyDataSetChanged();
    }

    public class LocationAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mLocationTextView;

        public LocationAdapterViewHolder(View view) {
            super(view);
            mLocationTextView = (TextView) view.findViewById(R.id.location_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Location selectedLocation = mLocationData.get(adapterPosition);
            mClickHandler.onClick(selectedLocation);
        }
    }
}
