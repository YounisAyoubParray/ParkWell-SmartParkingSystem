package com.example.smartparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class Illegalcar extends AppCompatActivity implements AnewRecycler.OnNoteListener {
    private ArrayList<Tinv> fc;
    private ArrayList<Tinv> dc;
    Boolean searchinfo = false;
    private EditText editTextq;
    private AnewRecycler Adapter;
    private TextView textView;
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
        setContentView(R.layout.activity_illegalcar);
        Intent intent =getIntent();
        String data =intent.getStringExtra("data");
        String alpha =intent.getStringExtra("museebat");
        fc=new ArrayList<>();
        dc=new ArrayList<>();
        textView=findViewById(R.id.textView7m3);
        editTextq=findViewById(R.id.donmess);
        editTextq.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextq.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icsearch24, 0, 0, 0);
                    editTextq.setCompoundDrawablePadding(20);}
                else{
                    editTextq.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);}
            }
        });
        editTextq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newsearch(s.toString());
            }
        });
        if(!data.isEmpty()) {
            String[] da=data.split("_");
            textView.setText(alpha);
            data(da);
            buildrecyclerview();
        }else {
            buildrecyclerview1();
        }
    }

    private void newsearch(String toString) {
        ArrayList<Tinv> filteredList = new ArrayList<>();
        for (Tinv item : fc) {
            if (item.getinnumber().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (editTextq.getText().toString().trim().equals("") || filteredList.isEmpty()) {
            searchinfo = false;
        } else {
            dc.clear();
            searchinfo = true;
            dc.addAll(filteredList);
        }
        Adapter.filterList(filteredList);
    }

    private void buildrecyclerview1() {
        Tinv at = new Tinv("Yaay!! No illegal occupants found","");
        fc.add(at);
        Log.d("at","MUB");
        RecyclerView = findViewById(R.id.recycle22);
        RecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Adapter = new AnewRecycler(fc,this);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(Adapter);
    }

    private void data(String[] da) {
        int a=da.length;
        for(int i=1;i<a;i++){
            Tinv ad=new Tinv("Slot",da[i]);
            fc.add(ad);
        }
    }

    private void buildrecyclerview() {
        RecyclerView = findViewById(R.id.recycle22);
        RecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Adapter = new AnewRecycler(fc,this);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(Adapter);
    }

    @Override
    public void onNoteClick(int position) {
        if(searchinfo.equals(Boolean.FALSE)) {
            String a = fc.get(position).innumber;
            if(a.equals("")){
                Toast.makeText(Illegalcar.this,"No cars found",Toast.LENGTH_SHORT).show();
            }else{
                    Intent intent = new Intent(Illegalcar.this, Ban.class);
                    intent.removeExtra("data");
                    intent.putExtra("report","Reported by system");
                    intent.putExtra("slot", a);
                    startActivity(intent);
            }
            }
        else{
            String a = dc.get(position).innumber;
            if(a.equals("")){
                Toast.makeText(Illegalcar.this,"No cars found",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(Illegalcar.this, Ban.class);
                intent.removeExtra("data");
                intent.putExtra("report","Reported by system");
                intent.putExtra("slot", a);
                startActivity(intent);}
        }
    }


}