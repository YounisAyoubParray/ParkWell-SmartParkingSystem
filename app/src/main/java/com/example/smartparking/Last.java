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

public class Last extends AppCompatActivity implements AnewRecycler.OnNoteListener{
    private TextView textView;
    ArrayList<Tinv> dc = new ArrayList<Tinv>();
    private Button button;
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
        setContentView(R.layout.activity_last);
        textView = findViewById(R.id.textView6000);
        button = findViewById(R.id.button600);


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
                DatePickerDialog datePickerDialog = new DatePickerDialog(Last.this, R.style.TimeDatePickerTheme, (datePicker, year1, month1, day1) -> textView.setText(day1 + "/" + (month1 + 1) + "/" + year1), year, month, day);
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
            }
        });








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
        FirebaseDatabase.getInstance().getReference("Refund").child("Srinagar").child(daty).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dc.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Boolean childValue = childSnapshot.getValue(Boolean.class);
                        if(childValue.equals(Boolean.TRUE)) {
                            Tinv af = new Tinv("Invoice", childSnapshot.getKey());
                            dc.add(af);
                        }
                    }
                } else {
                    Tinv af= new Tinv("Opps, No Refunds for the day","");
                    dc.add(af);
                }
                Adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Last.this, "Some error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buildrecycler() {
        RecyclerView = findViewById(R.id.recycle1200);
        RecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Adapter = new AnewRecycler(dc, this);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setAdapter(Adapter);
    }

    @Override
    public void onNoteClick(int position) {
        String a = dc.get(position).innumber;
        if(a.equals("")){
            Toast.makeText(Last.this,"No Refunds found",Toast.LENGTH_SHORT).show();
        }else if(a.contains("R")){
            String datee = textView.getText().toString();
            datee = datee.replace("/", "");
            String[] atf=a.split("R",0);
            String am=atf[0];
            Intent intent = new Intent(Last.this, FeedupLast.class);
            intent.putExtra("dat", datee);
            intent.putExtra("invoice", am);
            startActivity(intent);
        }else {
            String datee = textView.getText().toString();
            datee = datee.replace("/", "");
            Intent intent = new Intent(Last.this, FeedupLast.class);
            intent.putExtra("dat", datee);
            intent.putExtra("invoice", a);
            startActivity(intent);
        }
    }
}