package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class viewbookings extends AppCompatActivity {
    private TextView textview34,textview35;
    private EditText edinvoice;
    private Button button3,button4;
    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewbookings);

        textview34=findViewById(R.id.textView34);
        Intent intent= getIntent();
        String name=intent.getStringExtra("parkingname");
        textview34.setText(name);
        textview35=findViewById(R.id.textView35);

        edinvoice=findViewById(R.id.edinvoice);



        button3=findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectdate();
            }
        });

        button4=findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vb();
            }
        });
    }

    private void vb() {
        String name= textview34.getText().toString();
        String ab= textview35.getText().toString();
        String dateab=ab.replace("/","");
        String c=edinvoice.getText().toString();
        if(ab.isEmpty()){
            textview35.requestFocus();
        }
        else if(c.isEmpty()){
            edinvoice.setError("Enter the invoice");
            edinvoice.requestFocus();
        }
        else{
            user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
            userID=user.getUid();}
            FirebaseDatabase.getInstance().getReference("booking").child(name).child(dateab)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(c)){
                                FirebaseDatabase.getInstance().getReference("booking").child(name).child(dateab).child(c).child("UserId").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String uid= snapshot.getValue(String.class);
                                        if(uid.equals(userID)){
                                            Intent intent= new Intent(viewbookings.this,vwbookings.class);
                                            intent.putExtra("name",name);
                                            intent.putExtra("date",dateab);
                                            intent.putExtra("inv",c);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(viewbookings.this,"Incorrect invoice",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(viewbookings.this,"Oops error occurred",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else if(snapshot==null){
                                Toast.makeText(viewbookings.this, "Invalid information", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(viewbookings.this,"Invalid invoice",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(viewbookings.this,"Oops error occurred",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void selectdate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.TimeDatePickerTheme, (datePicker, year1, month1, day1) -> textview35.setText(day1 + "/" + (month1 + 1) + "/" + year1), year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
}