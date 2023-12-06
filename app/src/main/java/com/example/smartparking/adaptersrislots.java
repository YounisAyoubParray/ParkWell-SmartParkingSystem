package com.example.smartparking;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adaptersrislots extends RecyclerView.Adapter<adaptersrislots.ViewHolder1> {
    private ArrayList<srinagarslots> srinagarslot;
    private final OnNoteListener m1OnNoteListener;
    public adaptersrislots(ArrayList<srinagarslots> srinagarslots, com.example.smartparking.adaptersrislots.OnNoteListener onNoteListener1)
    {
        this.srinagarslot=srinagarslots;
        this.m1OnNoteListener=onNoteListener1;
    }

    public void filterList(ArrayList<srinagarslots> filteredList) {
        srinagarslot=filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView slot;
        private final TextView status;
        OnNoteListener onNoteListener1;
        public ViewHolder1(final View view, adaptersrislots.OnNoteListener onNoteListener) {
            super(view);
            slot=view.findViewById(R.id.textView56);
            status=view.findViewById(R.id.empty1);
            this.onNoteListener1=onNoteListener;
            view.setOnClickListener(this);
    }
        public void onClick(View view) {
            onNoteListener1.onNoteClick(getAbsoluteAdapterPosition());
        }
    }
    public interface OnNoteListener {
        void onNoteClick(int position);
    }


    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewrecycleslot,parent, false);
        return new ViewHolder1(itemView, m1OnNoteListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder1 holder, int position) {
        String slott=srinagarslot.get(position).getslot();
        String statuss=srinagarslot.get(position).getStatus();
        holder.slot.setText(slott);
        holder.status.setText(statuss);
    }

    @Override
    public int getItemCount() {
        return srinagarslot.size();
    }

}
