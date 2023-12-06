package com.example.smartparking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AnewRecycler extends RecyclerView.Adapter<AnewRecycler.ViewHold> {
    private ArrayList<Tinv> numbers;
    private final AnewRecycler.OnNoteListener mOnNoteListener;

    public AnewRecycler(ArrayList<Tinv> numbers, Illegalcar onNoteListener) {
        this.numbers=numbers;
        this.mOnNoteListener = onNoteListener;
    }

    public AnewRecycler(ArrayList<Tinv> numbers, Waste onNoteListener) {
        this.numbers=numbers;
        this.mOnNoteListener = onNoteListener;
    }

    public AnewRecycler(ArrayList<Tinv> numbers, OnNoteListener onNoteListener) {
        this.numbers=numbers;
        this.mOnNoteListener = onNoteListener;
    }

    public void filterList(ArrayList<Tinv> filteredList) {
        numbers=filteredList;
        notifyDataSetChanged();
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }


    public static class ViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView invnumber;
        private final TextView ena;
        AnewRecycler.OnNoteListener onNoteListener;

        public ViewHold(final View view, AnewRecycler.OnNoteListener onNoteListener) {
            super(view);
            invnumber = view.findViewById(R.id.textView599);
            ena = view.findViewById(R.id.textView533);
            this.onNoteListener = onNoteListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
        }
    }

        @NonNull
        @Override
        public AnewRecycler.ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.changedaterecycle, parent, false);
            return new AnewRecycler.ViewHold(itemView, mOnNoteListener);
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {
        String invnumberr = numbers.get(position).getinnumber();
        String ena =numbers.get(position).getname();
        holder.invnumber.setText(invnumberr);
        holder.ena.setText(ena);
    }

        @Override
        public int getItemCount() {
            return numbers.size();
        }

    }



