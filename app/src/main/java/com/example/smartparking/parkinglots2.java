package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class parkinglots2 extends AppCompatActivity implements adapter.OnNoteListener {
    private ArrayList<parkingname> parkingname;
    private RecyclerView mRecyclerView;
    private adapter mAdapter;
    Long agg;
    private ArrayList<parkingname> shit;
    Boolean searchinfo=false;
    private EditText editsearchhh;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkinglots2);
        parkingname= new ArrayList<>();
        shit=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("cost").child("Srinagar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agg=snapshot.getValue(Long.class);
                setparkinglist();
                buildRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        editsearchhh = findViewById(R.id.editsearchhh);
        editsearchhh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<parkingname> filteredList =new ArrayList<>();

        for(parkingname item : parkingname){
            if(item.getParkinglotname().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }


        if(editsearchhh.getText().toString().trim().equals("")||filteredList.isEmpty()){
            searchinfo=false;
        }else{
            shit.clear();
            searchinfo=true;
            shit.addAll(filteredList);
        }

        mAdapter.filterList(filteredList);
    }

    private void buildRecyclerView() {
        mRecyclerView=findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new adapter(parkingname,this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setparkinglist()
    {
        parkingname.add(new parkingname("Srinagar",String.valueOf(agg)+"Rs per hour"));
        parkingname.add(new parkingname("Parking Area 2","60Rs per hour"));
        parkingname.add(new parkingname("Parking Area 3","30Rs per hour"));
        parkingname.add(new parkingname("Parking Area 4","40Rs per hour"));
        parkingname.add(new parkingname("IUST","10Rs per hour"));
        parkingname.add(new parkingname("Parking Area 6","30Rs per hour"));
        parkingname.add(new parkingname("Parking Area 7","40Rs per hour"));
        parkingname.add(new parkingname("Parking Area 8","50Rs per hour"));

    }

    @Override
    public void onNoteClick(int position) {
        if (searchinfo.equals(Boolean.FALSE)) {
            String a = parkingname.get(position).parkinglotname;
            if (a.equals("Srinagar")) {

                Intent intent = new Intent(this, Relocation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("parkingname","Srinagar");
                startActivity(intent);
            } else {
                Toast.makeText(this, "Dummy Slot", Toast.LENGTH_LONG).show();
            }
        } else {
            String a = shit.get(position).parkinglotname;
            if (a.equals("Srinagar")) {

                Intent intent = new Intent(this, Relocation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("parkingname","Srinagar");
                startActivity(intent);
            } else {
                Toast.makeText(this, "Dummy Slot", Toast.LENGTH_LONG).show();
            }
        }
    }




}