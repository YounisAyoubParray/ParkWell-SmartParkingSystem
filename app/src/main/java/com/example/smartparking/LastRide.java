package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class LastRide extends AppCompatActivity implements AnewRecycler.OnNoteListener {
    private ArrayList<Tinv> dc;
    private AnewRecycler Adapter;
    private RecyclerView RecyclerView;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_ride);
        dc=new ArrayList<>();
        data();
        buildrecyclerview();
    }

    private void data() {
        Calendar calendar= Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn=month+1;
        String datee = day+""+mn+year;
        FirebaseDatabase.getInstance().getReference("parkings").child("SrinagarR").child("R1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean a=snapshot.getValue(Boolean.class);
                dc.clear();
                if(a.equals(Boolean.TRUE)){
                    dc.add(new Tinv("Yaay!! No vehicle found",""));
                    Adapter.notifyDataSetChanged();
                }else{
                    FirebaseDatabase.getInstance().getReference("availability").child("SrinagarR").child(datee).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    String childValue = childSnapshot.getValue(String.class);
                                    String[] rt=childValue.split("_",0);
                                    dc.add(new Tinv(childSnapshot.getKey()+"    ",rt[1]+"  "+rt[2]));
                                }
                                Adapter.notifyDataSetChanged();
                            }else{
                                dc.add(new Tinv("Yaay!! No vehicle found",""));
                                Adapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buildrecyclerview() {
        RecyclerView = findViewById(R.id.recycle2212);
        RecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Adapter = new AnewRecycler(dc,this);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(Adapter);
    }

    @Override
    public void onNoteClick(int position) {
        Calendar calendar= Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn=month+1;
        String datee = day+""+mn+year;
        String a = dc.get(position).innumber;
        if(a.equals("")){
            Toast.makeText(LastRide.this,"No Relocations found",Toast.LENGTH_SHORT).show();
        }else if(a.contains("R")){
            String[] atf=a.split("R",0);
            String am=atf[0];
            Intent intent = new Intent(LastRide.this, Feedup.class);
            intent.putExtra("dat", datee);
            intent.putExtra("invoice", am);
            startActivity(intent);
        }else {
            Intent intent = new Intent(LastRide.this, Adminui.class);
            intent.putExtra("dat", datee);
            intent.putExtra("invoice", a);
            startActivity(intent);
        }


    }
}