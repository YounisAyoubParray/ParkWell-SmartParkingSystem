package com.example.smartparking;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Unbantime extends AppCompatActivity implements PaymentResultListener {
    private Button calculate;
    private EditText vcn;
    String number="";
    long afg;
    private FirebaseUser user;
    private String userID;
    private Button pay;
    private TextView totaloffences,dateofban,fine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbantime);
        calculate=findViewById(R.id.button71);
        pay=findViewById(R.id.Paytheunbanfee);
        dateofban=findViewById(R.id.textView80);
        totaloffences=findViewById(R.id.textView81);
        fine=findViewById(R.id.textView82);
        user = FirebaseAuth.getInstance().getCurrentUser();
        vcn=findViewById(R.id.banedetet1);
        afg=0l;
        vcn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totaloffences.setText("Na");
                fine.setText("Na");
                dateofban.setText("Na");
            }
        });
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vcn.getText().toString().isEmpty()) {
                    Toast.makeText(Unbantime.this, "Enter the vehcile number first", Toast.LENGTH_SHORT).show();
                } else {
                    totalfine();
                }
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempts();

            }
        });
    }

    private void attempts() {
        String a=vcn.getText().toString();
        String finee=fine.getText().toString();
        if(finee.equals("Na")){
            Toast.makeText(this, "Calculate the fine first", Toast.LENGTH_SHORT).show();
        }else{

            FirebaseDatabase.getInstance().getReference("UnbanTries").child(a).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long af=snapshot.getValue(Long.class);
                    afg=af;
                            if(af>0l){
                            FirebaseDatabase.getInstance().getReference("UnbanTries").child(a).setValue(af-1l).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(af==1){
                                            FirebaseDatabase.getInstance().getReference("Offlimits").child(a).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                payment();
                                                }
                                            });
                                        }else{
                                    payment();}
                                    }
                                }
                            });}else{
                                Toast.makeText(Unbantime.this, "You are off limits contact the administrator to unban yourself", Toast.LENGTH_SHORT).show();
                            }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Unbantime.this, "Long issue", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void payment() {
        String a=vcn.getText().toString();
        String b=totaloffences.getText().toString();
        String finee=fine.getText().toString();
        String time=datenow();
        String TransactionId="Temp"+time;
        String userId=user.getUid();
        UnbanObj omg=new UnbanObj(time,finee,TransactionId,userId);
        FirebaseDatabase.getInstance().getReference("FinePayment").child(a).child(b).setValue(omg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("bandetails").child(a).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                PaymentNow(finee);
                            }
                        }
                    });
                }else{
                    Toast.makeText(Unbantime.this, "Try again later", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void PaymentNow(String payment) {
        String numericPart = payment.replaceAll("[^0-9.]", "");
        final Activity activity=this;
        Checkout checkout=new Checkout();
        checkout.setKeyID("***********************************");
        checkout.setImage(R.drawable.parkinglogo);
        double fa= Float.parseFloat(numericPart)*100;
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userEmail = user.getEmail();
                if (snapshot.exists()) {
                    number = snapshot.getValue(String.class);
                    try {
                        JSONObject options = new JSONObject();
                        options.put("name", "ParkWell");
                        options.put("description", "Reinstate Reference No. #" + totaloffences.getText().toString());
                        options.put("currency", "INR");
                        options.put("theme.color", "#3399cc");
                        options.put("amount", fa + "");
                        options.put("prefill.email", userEmail);
                        options.put("prefill.contact", number);
                        checkout.open(activity, options);
                    } catch (Exception e) {
                        String a=vcn.getText().toString();
                        String b=totaloffences.getText().toString();
                        FirebaseDatabase.getInstance().getReference("FinePayment").child(a).child(b).removeValue();
                        FirebaseDatabase.getInstance().getReference("UnbanTries").child(a).setValue(afg);
                        FirebaseDatabase.getInstance().getReference("bandetails").child(a).setValue(false);
                        Toast.makeText(Unbantime.this, "Unsuccessful Payment", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Unbantime.this, loggedinn.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                } else {
                    try {
                        JSONObject options = new JSONObject();
                        options.put("name", "ParkWell");
                        options.put("description", "Reinstate Reference No. #" + totaloffences.getText().toString());
                        options.put("currency", "INR");
                        options.put("theme.color", "#3399cc");
                        options.put("amount", fa + "");
                        options.put("prefill.email", userEmail);
                        checkout.open(activity, options);
                    } catch (Exception e) {
                        String a=vcn.getText().toString();
                        String b=totaloffences.getText().toString();
                        FirebaseDatabase.getInstance().getReference("FinePayment").child(a).child(b).removeValue();
                        FirebaseDatabase.getInstance().getReference("UnbanTries").child(a).setValue(afg);
                        FirebaseDatabase.getInstance().getReference("bandetails").child(a).setValue(false);
                        Toast.makeText(Unbantime.this, "Unsuccessful Payment", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Unbantime.this, loggedinn.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });}

    @Override
    public void onPaymentSuccess(String s) {
        String a=vcn.getText().toString();
        String b=totaloffences.getText().toString();
        FirebaseDatabase.getInstance().getReference("FinePayment").child(a).child(b).child("TransactionId").setValue(s);
        Toast.makeText(Unbantime.this, "Successfully unbanned the car", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Unbantime.this, loggedinn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    @Override
    public void onPaymentError(int i, String s) {
        String a=vcn.getText().toString();
        String b=totaloffences.getText().toString();
        FirebaseDatabase.getInstance().getReference("FinePayment").child(a).child(b).removeValue();
        FirebaseDatabase.getInstance().getReference("UnbanTries").child(a).setValue(afg);
        FirebaseDatabase.getInstance().getReference("bandetails").child(a).setValue(false);
        Toast.makeText(Unbantime.this, "Unsuccessful Payment", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Unbantime.this, loggedinn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }










    private void totalfine() {
        String a=vcn.getText().toString();
        FirebaseDatabase.getInstance().getReference("bandetails").child(a).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Boolean af=snapshot.getValue(Boolean.class);
                    if(af.equals(Boolean.FALSE)){
                        Toast.makeText(Unbantime.this, "Vehcile not banned", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        FirebaseDatabase.getInstance().getReference("banrecords").child(a).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long ag = snapshot.getChildrenCount();
                                FirebaseDatabase.getInstance().getReference("banrecords").child(a).child(String.valueOf(ag)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        BanObj data=snapshot.getValue(BanObj.class);
                                        if(data!=null){
                                            dateofban.setText(data.Dated);
                                            totaloffences.setText(String.valueOf(ag));
                                            finecalculator();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Unbantime.this, "Try again later!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Unbantime.this, "Try again later!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(Unbantime.this, "Vehicle not banned", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Unbantime.this, "Try again later!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void finecalculator() {
        long weeeks=0l;
        String ab =  dateofban.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());

        try {
            Date d1 = sdf.parse(ab);
            Date d2 = sdf.parse(currentDate);

            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            long days_difference = (difference_In_Time / (1000*60*60*24)) % 365;
            weeeks=days_difference/7;

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        if(weeeks==0l){
            fine.setText("₹200");
        }else if(weeeks>0l && weeeks<5l) {
            long a = 250 * weeeks;
            fine.setText("₹" + String.valueOf(a));
        }else{
            long a = 300 * weeeks;
            fine.setText("₹" + String.valueOf(a));
        }
    }
    private String datenow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
}
