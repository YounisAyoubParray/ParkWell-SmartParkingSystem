package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Waste extends AppCompatActivity implements AnewRecycler.OnNoteListener {
    private TextView textView, totalb;
    ArrayList<Tinv> d = new ArrayList<Tinv>();
    ArrayList<Tinv> dc = new ArrayList<Tinv>();
    private Button button;
    private EditText editText;
    Boolean searchinfo = false;
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
        setContentView(R.layout.activity_waste);
        textView = findViewById(R.id.textView60);
        totalb = findViewById(R.id.textView62);
        button = findViewById(R.id.button6);
        editText = findViewById(R.id.ssearch2s);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icsearch24, 0, 0, 0);
                    editText.setCompoundDrawablePadding(20);
                } else {
                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
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
        datee();
        buildrecycler();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.set(2023, 4, 1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Waste.this, R.style.TimeDatePickerTheme, (datePicker, year1, month1, day1) -> textView.setText(day1 + "/" + (month1 + 1) + "/" + year1), year, month, day);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String date = textView.getText().toString();
                helltime(date);
                editText.setText("");
                searchinfo=false;
            }
        });
    }

    private void newsearch(String toString) {
        ArrayList<Tinv> filteredList = new ArrayList<>();
        for (Tinv item : d) {
            if (item.getinnumber().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (editText.getText().toString().trim().equals("") || filteredList.isEmpty()) {
            searchinfo = false;
        } else {
            dc.clear();
            searchinfo = true;
            dc.addAll(filteredList);
        }
        Adapter.filterList(filteredList);
    }

    private void buildrecycler() {
        RecyclerView = findViewById(R.id.recycle12);
        RecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Adapter = new AnewRecycler(d, this);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(Adapter);
    }

    private void datee() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn = month + 1;
        String date = day + "/" + mn + "/" + year;
        helltime(date);
        textView.setText(date);
    }

    private void helltime(String date) {
        String daty = date.replace("/", "");
        FirebaseDatabase.getInstance().getReference("booking").child("Srinagar").child(daty).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long a = snapshot.getChildrenCount();
                    totalb.setText(String.valueOf(a));
                    d.clear();
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        Tinv af= new Tinv("Invoice",dsp.getKey());
                        d.add(af);
                    }
                    Adapter.filterList(d);
                    Adapter.notifyDataSetChanged();
                } else {
                    d.clear();
                    Tinv af= new Tinv("Opps, No Bookings for the day","");
                    d.add(af);
                    totalb.setText("0");
                    Adapter.filterList(d);
                    Adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Waste.this, "Some error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        if(searchinfo.equals(Boolean.FALSE)) {
            String a = d.get(position).innumber;
            if(a.equals("")){
                Toast.makeText(Waste.this,"No bookings found",Toast.LENGTH_SHORT).show();
            }else {
                String datee = textView.getText().toString();
                datee = datee.replace("/", "");
                Intent intent = new Intent(Waste.this, Feedup.class);
                intent.putExtra("dat", datee);
                intent.putExtra("invoice", a);
                startActivity(intent);
            }
        }else{
            String a = dc.get(position).innumber;
            if(a.equals("")){
                Toast.makeText(Waste.this,"No bookings found",Toast.LENGTH_SHORT).show();
            }else {
                String datee = textView.getText().toString();
                datee = datee.replace("/", "");
                Intent intent = new Intent(Waste.this, Feedup.class);
                intent.putExtra("dat", datee);
                intent.putExtra("invoice", a);
                startActivity(intent);
            }
        }


    }
}