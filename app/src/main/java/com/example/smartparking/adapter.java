package com.example.smartparking;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {
    private ArrayList<parkingname> parkinglotname;
    private final OnNoteListener mOnNoteListener;


    public adapter(ArrayList<parkingname> parkinglotname, OnNoteListener onNoteListener)
    {
        this.parkinglotname=parkinglotname;
        this.mOnNoteListener=onNoteListener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView name;
        private final TextView cost;
        OnNoteListener onNoteListener;
        public MyViewHolder(final View view, OnNoteListener onNoteListener) {
            super(view);
            name=view.findViewById(R.id.empty);
            cost=view.findViewById(R.id.parkcost);
            this.onNoteListener=onNoteListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
        }

    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    @NonNull
    @Override
    public adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewrecycle,parent, false);
        return new MyViewHolder(itemView, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String nameee=parkinglotname.get(position).getParkinglotname();
        String costt=parkinglotname.get(position).getParkcost();
        holder.name.setText(nameee);
        holder.cost.setText(costt);
    }

    @Override
    public int getItemCount() {
        return parkinglotname.size();
    }

    public void filterList(ArrayList<parkingname> filteredList){
        parkinglotname = filteredList;
        notifyDataSetChanged();
    }
}
