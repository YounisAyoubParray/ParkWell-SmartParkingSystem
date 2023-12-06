package com.example.smartparking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter1 extends RecyclerView.Adapter<Adapter1.MyViewHolder> {
    private ArrayList<parkingname1> parkinglotname1;
    private final OnNoteListener mOnNoteListener;


    public Adapter1(ArrayList<parkingname1> parkinglotname1, OnNoteListener onNoteListener)
    {
        this.parkinglotname1=parkinglotname1;
        this.mOnNoteListener=onNoteListener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView name;
        private final TextView cost;
        private final TextView ta;
        private final TextView dts;
        Adapter1.OnNoteListener onNoteListener;
        public MyViewHolder(final View view, Adapter1.OnNoteListener onNoteListener) {
            super(view);
            name=view.findViewById(R.id.empty121);
            cost=view.findViewById(R.id.parkcost121);
            ta=view.findViewById(R.id.textView110);
            dts=view.findViewById(R.id.textView111);
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
    public Adapter1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewrecycle1,parent, false);
        return new MyViewHolder(itemView, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter1.MyViewHolder holder, int position) {
        String nameee=parkinglotname1.get(position).getParkinglotname1();
        String costt=parkinglotname1.get(position).getParkcost1();
        String dttts=parkinglotname1.get(position).getdt();
        String tass=parkinglotname1.get(position).gettas();
        holder.name.setText(nameee);
        holder.cost.setText(costt);
        holder.ta.setText(tass);
        holder.dts.setText(dttts);
    }

    @Override
    public int getItemCount() {
        return parkinglotname1.size();
    }

    public void filterList(ArrayList<parkingname1> filteredList){
        parkinglotname1 = filteredList;
        notifyDataSetChanged();
    }
}

