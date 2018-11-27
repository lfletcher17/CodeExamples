package com.example.android.eventsuggester;

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private ArrayList<Event> mEventData;
    private final EventAdapter.EventAdapterOnClickHandler mClickHandler;

    public EventAdapter(EventAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface EventAdapterOnClickHandler {
        void onClick(Event selectedEvent);
    }

    @Override
    public EventAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.event_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new EventAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapterViewHolder holder, int position) {
        Event event = mEventData.get(position);
        holder.mEventTextViewDate.setText(event.formattedDate());
        holder.mEventTextViewArtistAndVenue.setText(event.getHeadliner().getName() +" @ " + event.getVenue());
    }

    @Override
    public int getItemCount() {
        if (mEventData == null) {
            return 0;
        }
        return mEventData.size();
    }

    public void setEventData(ArrayList<Event> eventData) {
        mEventData = eventData;
        notifyDataSetChanged();
    }

    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mEventTextViewDate;
        public final TextView mEventTextViewArtistAndVenue;

        public EventAdapterViewHolder(View view) {
            super(view);
            mEventTextViewDate = (TextView) view.findViewById(R.id.event_date);
            mEventTextViewArtistAndVenue = (TextView) view.findViewById(R.id.event_artist);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Event selectedEvent = mEventData.get(adapterPosition);
            mClickHandler.onClick(selectedEvent);
        }
    }
}
