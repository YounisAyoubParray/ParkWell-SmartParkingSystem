package com.example.smartparking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptorAdmbr extends RecyclerView.Adapter<AdaptorAdmbr.ViewHolder2> {
    private ArrayList<AdAdmbr> numbers;
    private final AdaptorAdmbr.OnNoteListener mOnNoteListener;

    public AdaptorAdmbr(ArrayList<AdAdmbr> numbers, Admbookdetail onNoteListener)
    {
        this.numbers=numbers;
        this.mOnNoteListener=onNoteListener;
    }

    public void filterList(ArrayList<AdAdmbr> filteredList) {
        numbers=filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView number;
        private final TextView invnumber;
        OnNoteListener onNoteListener;
        public ViewHolder2(final View view, AdaptorAdmbr.OnNoteListener onNoteListener) {
            super(view);
            number=view.findViewById(R.id.textView55);
            invnumber=view.findViewById(R.id.textView59);
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
    public AdaptorAdmbr.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminrecycle,parent, false);
        return new AdaptorAdmbr.ViewHolder2(itemView, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorAdmbr.ViewHolder2 holder, int position) {
        String numberr=numbers.get(position).getnumber();
        String invnumberr=numbers.get(position).getinvnumber();
        holder.number.setText(numberr);
        holder.invnumber.setText(invnumberr);
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

}
