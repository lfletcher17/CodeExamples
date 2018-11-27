package com.example.android.engagingtechtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TelephoneAdapter extends RecyclerView.Adapter<TelephoneAdapter.TelephoneAdapterViewHolder>  {

    private ArrayList<TelephoneNumber> mTelephoneData;

    @Override
    public TelephoneAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.telephone_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TelephoneAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TelephoneAdapterViewHolder holder, int position) {
        TelephoneNumber number = mTelephoneData.get(position);
        holder.mTelephoneType.setText(mTelephoneData.get(position).getType()+":");
        holder.mTelephoneNumber.setText(mTelephoneData.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        if (mTelephoneData == null) {
            return 0;
        }
        return mTelephoneData.size();
    }

    public void setmTelephoneData(ArrayList<TelephoneNumber> telephoneData) {
        mTelephoneData= telephoneData;
        notifyDataSetChanged();
    }

    public class TelephoneAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTelephoneType;
        public final TextView mTelephoneNumber;

        public TelephoneAdapterViewHolder(View view) {
            super(view);
            mTelephoneType = (TextView) view.findViewById(R.id.telephone_type);
            mTelephoneNumber = (TextView) view.findViewById(R.id.telephone_number);
        }

    }


}
