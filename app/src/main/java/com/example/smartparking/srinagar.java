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

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class srinagar extends AppCompatActivity implements adaptersrislots.OnNoteListener{
    ArrayList<srinagarslots> sfilteredList ;
    private TextView srinagar;
    private ArrayList<srinagarslots> srinagarslot;
    private RecyclerView m1RecyclerView;
    private adaptersrislots m1Adapter;
    private EditText availablesearch;
    Boolean searchinfo=false;
    private TextView totalslotsavailable;
    private DatabaseReference reference;
    String bl1="" ,bl2="",bl3="" ;
    String sl1="" ,sl2="",sl3="" ;

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
        setContentView(R.layout.activity_srinagar);

        availablesearch= findViewById(R.id.availablesearch);
        srinagar=findViewById(R.id.srinagar45);
        Intent intent=getIntent();
        String pkn=intent.getStringExtra("parkingname").toUpperCase()+" PARKING";
        srinagar.setText(pkn);
        availablesearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search(editable.toString());
            }
        });
        sfilteredList=new ArrayList<>();
        totalslotsavailable= findViewById(R.id.totalslotsavailable);
        srinagarslot= new ArrayList<>();
        slot();
        buildRecyclerView1();
    }




    private void slot() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn=month+1;
        String datee = day+""+mn+year;
        String aphour;
        if(hour>12){
            int nt=hour-12;
            aphour="pm"+nt;

        }else if(hour==0){
            int nt=12;
            aphour="am"+nt;

        }else if(hour==12){
            aphour="pm"+hour;
        }
        else{
            aphour="am"+hour;
        }

        FirebaseDatabase.getInstance().getReference("Vehiclenumber").child("Srinagar").child(datee).child(aphour)
                .addValueEventListener(new ValueEventListener()  {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                        String ac = snapshot.getValue(String.class);
                        if (ac.contains("slot1")) {
                            bl1 = "RESERVED";
                        }else{
                            bl1="";
                        }
                        if (ac.contains("slot2")) {
                                bl2 = "RESERVED";

                            }else{
                                bl2="";

                            }
                            if (ac.contains("slot3")) {
                                bl3 = "RESERVED";

                            }else{
                                bl3="";

                            }

                        }else{
                            bl1="";
                            bl2="";
                            bl3="";
                        }
                        data();
                    }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });







                }











    private void search(String text) {
        ArrayList<srinagarslots> filteredList =new ArrayList<>();

        for(srinagarslots item : srinagarslot){
            if(item.getStatus().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }

        if(availablesearch.getText().toString().trim().equals("")||filteredList.isEmpty()){

            searchinfo=false;
        }else{
            sfilteredList.clear();
            searchinfo=true;
            sfilteredList.addAll(filteredList);
        }



        m1Adapter.filterList(filteredList);
    }

    private void data() {

        reference = FirebaseDatabase.getInstance().getReference("parkings");
        reference.child("Srinagar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Slotssrinagar s = snapshot.getValue(Slotssrinagar.class);
                if(s != null) {
                    Boolean b1 = s.slot1;
                    Boolean b2 = s.slot2;
                    Boolean b3 = s.slot3;
                    adapdata(b1,b2,b3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                sl1 = "No Information";
                sl2 = "No Information";
                sl3 = "No Information";
                setsrinagarslot(sl1,sl2,sl3);
                m1Adapter.notifyDataSetChanged();
            }
        });
    }

    private void adapdata(Boolean b1, Boolean b2,Boolean b3) {
        int count = 0;
        srinagarslot.clear();
        if(b1.equals(Boolean.FALSE)) {
            sl1="NOT AVAILABLE";
        }else
         {
            if (bl1.equals("RESERVED")) {
                sl1 = "RESERVED";
            } else {
                sl1 = "AVAILABLE";
                count++;
            }
        }

        if(b2.equals(Boolean.FALSE)){
            sl2="NOT AVAILABLE";
        }else {
            if (bl2.equals("RESERVED")) {

                sl2 = "RESERVED";
            } else {
                sl2 = "AVAILABLE";
                count++;
            }
        }

        if(b3.equals(Boolean.FALSE)) {
            sl3="NOT AVAILABLE";
        }else
        {
            if (bl3.equals("RESERVED")) {
                sl3 = "RESERVED";
            } else {
                sl3 = "AVAILABLE";
                count++;
            }
        }


        setsrinagarslot(sl1, sl2,sl3);
        totalslotsavailable.setText(String.valueOf(count));
        m1Adapter.notifyDataSetChanged();
    }


    private void buildRecyclerView1() {
        m1RecyclerView=findViewById(R.id.recycler1);
        m1RecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        m1Adapter = new adaptersrislots(srinagarslot,this);
        m1RecyclerView.setLayoutManager(layoutManager);
        m1RecyclerView.setAdapter(m1Adapter);
    }
    private void setsrinagarslot(String sl1,String sl2,String sl3)
    {
        srinagarslot.add(new srinagarslots("1",sl1));
        srinagarslot.add(new srinagarslots("2",sl2));
        srinagarslot.add(new srinagarslots("3",sl3));
        srinagarslot.add(new srinagarslots("4","NA"));
        srinagarslot.add(new srinagarslots("5","NA"));
        srinagarslot.add(new srinagarslots("6","NA"));
        srinagarslot.add(new srinagarslots("7","NA"));
        srinagarslot.add(new srinagarslots("8","NA"));
    }

    @Override
    public void onNoteClick(int position) {

        if(searchinfo.equals(Boolean.FALSE)) {
            String a = srinagarslot.get(position).slot;
            if (Integer.valueOf(a) < 5) {
                Intent intent = new Intent(srinagar.this, booktheslots.class);
                intent.putExtra("slotnumber", a);
                intent.putExtra("parkingname","Srinagar" );
                startActivity(intent);
            } else {
                Toast.makeText(this, "Dummy Slot", Toast.LENGTH_LONG).show();
            }
        }else{
            String a=  sfilteredList.get(position).slot;
            if (Integer.valueOf(a) < 5) {
                Intent intent = new Intent(srinagar.this, booktheslots.class);
                intent.putExtra("slotnumber", a);
                intent.putExtra("parkingname","Srinagar" );
                startActivity(intent);
            } else {
                Toast.makeText(this, "Dummy Slot", Toast.LENGTH_LONG).show();
            }
        }






}
}


