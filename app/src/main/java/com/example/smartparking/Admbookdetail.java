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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Admbookdetail extends AppCompatActivity implements AdaptorAdmbr.OnNoteListener {
    private ArrayList<AdAdmbr> newer;
    String datee;
    private TextView datetime;
    Boolean searchinfo=false;
    private ArrayList<AdAdmbr> searcch;
    private RecyclerView RecyclerView;
    private EditText ssearchs;
    private AdaptorAdmbr Adapter;
    String pa = "", pb = "", pc = "", pd = "";
    String a = "", b = "", c = "";
    private DatabaseReference reference;

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
        setContentView(R.layout.activity_admbookdetail);
        ssearchs = findViewById(R.id.ssearchs);
        datetime=findViewById(R.id.datetime);
        ssearchs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ssearchs.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icsearch24, 0, 0, 0);
                    ssearchs.setCompoundDrawablePadding(20);
                } else {
                    ssearchs.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        ssearchs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                newsearch(editable.toString());
            }
        });

        newer = new ArrayList<>();
        searcch=new ArrayList<>();
        data();
        buildrecyclerview();
    }

    private void newsearch(String toString) {
        ArrayList<AdAdmbr> filteredList=  new ArrayList<>();;
        for (AdAdmbr item : newer) {
            if (item.getinvnumber().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if(ssearchs.getText().toString().trim().equals("")||filteredList.isEmpty()){
            searchinfo=false;
        }else{
            searcch.clear();
            searchinfo=true;
            searcch.addAll(filteredList);
        }



        Adapter.filterList(filteredList);
    }

    private void data() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn = month + 1;
        datee = day + "" + mn + "" + year;
        String must="Date: "+day+"/"+mn+"/"+year+"   Hour: ";
        String aphour;
        if (hour > 12) {
            int nt = hour - 12;
            aphour = "pm" + nt;
            datetime.setText(must+String.valueOf(nt)+"pm");

        } else if (hour == 0) {
            int nt = 12;
            aphour = "am" + nt;
            datetime.setText(must+String.valueOf(nt)+"am");

        } else if (hour == 12) {
            aphour = "pm" + hour;
            datetime.setText(must+String.valueOf(hour)+"pm");
        } else {
            aphour = "am" + hour;
            datetime.setText(must+String.valueOf(hour)+"am");
        }
        reference = FirebaseDatabase.getInstance().getReference("Vehiclenumber").child("Srinagar").child(datee);
//        reference.child("slot1").child(datee).child(aphour).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String paa = snapshot.getValue(String.class);
//                    if (paa.contains("_")) {
//                        pa = paa;
//                        dsdata();
//                    } else {
//                        pa = "Not Booked";
//                        dsdata();
//                    }
//
//                } else {
//                    pa = "Not Booked";
//                    dsdata();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        reference.child("slot2").child(datee).child(aphour).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String pab = snapshot.getValue(String.class);
//                    if (pab.contains("_")) {
//                        pb = pab;
//                        dsdata();
//                    } else {
//                        pb = "Not Booked";
//                        dsdata();
//                    }
//                } else {
//                    pb = "Not Booked";
//                    dsdata();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        reference.child("slot3").child(datee).child(aphour).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String pac = snapshot.getValue(String.class);
//                    if (pac.contains("_")) {
//                        pc = pac;
//                        dsdata();
//                    } else {
//                        pc = "Not Booked";
//                        dsdata();
//                    }
//                } else {
//                    pc = "Not Booked";
//                    dsdata();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//        reference.child("slot4").child(datee).child(aphour).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//                    String pad = snapshot.getValue(String.class);
//                    if (pad.contains("_")) {
//                        pd = pad;
//                        dsdata();
//                    } else {
//                        pd = "Not Booked";
//                        dsdata();
//                    }
//                } else {
//                    pd = "Not Booked";
//                    dsdata();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        reference.child(aphour).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String amg=snapshot.getValue(String.class);
                    if(amg==null || amg.equalsIgnoreCase("init_")){
                        setupdatedata("Not Booked", "Not Booked", "Not Booked");
                        Adapter.notifyDataSetChanged();
                    }else{
                        dsdata(amg);
                    }
                }else{
                    setupdatedata("Not Booked", "Not Booked", "Not Booked");
                    Adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void dsdata(String amg) {
        newer.clear();
        if(!amg.contains("slot1")){
            a="Not Booked";
        }
        if(!amg.contains("slot2")){
            b="Not Booked";
        }
        if(!amg.contains("slot3")){
            c="Not Booked";
        }

        String[] thinkbig=amg.split("-",0);
        for (int i=1;i<thinkbig.length;i++) {
            if(thinkbig[i].contains("slot1")){
                String[] aq=thinkbig[i].split("_",0);
                a=aq[1]+"   Vehicle number: "+aq[2];
            }
            if(thinkbig[i].contains("slot2")){
                String[] aq=thinkbig[i].split("_",0);
                b=aq[1]+"   Vehicle number: "+aq[2];
            }
            if(thinkbig[i].contains("slot3")){
                String[] aq=thinkbig[i].split("_",0);
                c=aq[1]+"   Vehicle number: "+aq[2];
            }

        }
        setupdatedata(a, b, c);
        Adapter.notifyDataSetChanged();
    }


    public void setupdatedata(String pa, String pb, String pc) {
        newer.clear();
        newer.add(new AdAdmbr("1", pa));
        newer.add(new AdAdmbr("2", pb));
        newer.add(new AdAdmbr("3", pc));
        newer.add(new AdAdmbr("4", "Dummy"));
        newer.add(new AdAdmbr("5", "Dummy"));
        newer.add(new AdAdmbr("6", "Dummy"));
        newer.add(new AdAdmbr("7", "Dummy"));
        newer.add(new AdAdmbr("8", "Dummy"));

    }


    private void buildrecyclerview() {
        RecyclerView = findViewById(R.id.recycle2);
        RecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Adapter = new AdaptorAdmbr(newer, this);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(Adapter);
    }

    @Override
    public void onNoteClick(int position) {
            if(searchinfo.equals(Boolean.FALSE)) {
                String a = newer.get(position).number;
                String b = newer.get(position).invnumber;
                String inv;
                if(b.contains("Vehicle number:")){
                String[] invt=b.split("   ");
                inv=invt[0];
                }else{
                    inv="Not Booked";
                }
                if (Integer.valueOf(a) < 5) {
                    if(b.equals("Not Booked")){
                        Toast.makeText(this, "Slot not booked currently", Toast.LENGTH_LONG).show();
                    }else{
                    Intent intent = new Intent(Admbookdetail.this, Feedup.class);
                    intent.putExtra("dat", datee);
                    intent.putExtra("invoice", inv);
                    startActivity(intent);}
                } else {
                    Toast.makeText(this, "Dummy Slot", Toast.LENGTH_LONG).show();
                }
            }else{
                String a=  searcch.get(position).number;
                String b = searcch.get(position).invnumber;
                String inv;
                if(b.contains("Vehicle number:")){
                    String[] invt=b.split("   ");
                    inv=invt[0];
                }else{
                    inv="Not Booked";
                }
                if (Integer.valueOf(a) < 5) {
                    if(b.equals("Not Booked")){
                        Toast.makeText(this, "Slot not booked currently", Toast.LENGTH_LONG).show();
                    }else{
                    Intent intent = new Intent(Admbookdetail.this, Feedup.class);
                    intent.putExtra("dat", datee);
                    intent.putExtra("invoice", inv);
                    startActivity(intent);}
                } else {
                    Toast.makeText(this, "Dummy Slot", Toast.LENGTH_LONG).show();
                }
            }

    }
}