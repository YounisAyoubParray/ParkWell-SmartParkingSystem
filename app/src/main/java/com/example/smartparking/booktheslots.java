package com.example.smartparking;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.util.Base64;

public class booktheslots extends AppCompatActivity implements PaymentResultListener {
    private EditText etvn;
    long valuee;
    String number="";
    final int miny=500,maxy=2000;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView slotnum,selrb, pay;
    private TextView parkingareaname;
    private TextView tvedate;
    private TextView tventime,tvenap;
    private TextView tvextime,tvexap;
    private Button edate, entime, extime,check1, button2;
    private int entrytimee,exittimee;
    private String userID;
    private FirebaseUser user;
    private DatabaseReference reference;
    private ProgressDialog builder;
    private TextView am12, am1, am2, am3, am4, am5, am6, am7,am8,
     am9, am10,  am11, pm12,  pm1, pm2, pm3, pm4, pm5,
    pm6,pm7, pm8, pm9,pm10, pm11, tvupd, tvdteup;
    private Boolean nowwhat;

    private final String[] know = new String[24];

    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog  timePickerDialog;



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











    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booktheslots);
        selrb= findViewById(R.id.selrb);
        radioGroup=findViewById(R.id.radioGroup);
        pay= findViewById(R.id.pay);
        etvn= findViewById(R.id.etvn);
        builder = new ProgressDialog(booktheslots.this);
        Intent intent = getIntent();
        nowwhat=true;
        valuee=0;
        parkingareaname=findViewById(R.id.parkingare5aname);
        parkingareaname.setShadowLayer(10, 0, 0, Color.BLACK);
        am12=findViewById(R.id.am12);
        am1=findViewById(R.id.am1);
        am2=findViewById(R.id.am2);
        am3=findViewById(R.id.am3);
        am4=findViewById(R.id.am4);
        am5=findViewById(R.id.am5);
        am6=findViewById(R.id.am6);
        am7=findViewById(R.id.am7);
        am8=findViewById(R.id.am8);
        am9=findViewById(R.id.am9);
        am10=findViewById(R.id.am10);
        am11=findViewById(R.id.am11);
        pm12=findViewById(R.id.pm12);
        pm1=findViewById(R.id.pm1);
        pm2=findViewById(R.id.pm2);
        pm3=findViewById(R.id.pm3);
        pm4=findViewById(R.id.pm4);
        pm5=findViewById(R.id.pm5);
        pm6=findViewById(R.id.pm6);
        pm7=findViewById(R.id.pm7);
        pm8=findViewById(R.id.pm8);
        pm9=findViewById(R.id.pm9);
        pm10=findViewById(R.id.pm10);
        pm11=findViewById(R.id.pm11);
        tvupd=findViewById(R.id.tvupd);
        tvdteup=findViewById(R.id.tvdteup);
        String parkingname = intent.getStringExtra("parkingname");
        String slotnumb = intent.getStringExtra("slotnumber");
        parkingareaname.setText(parkingname + " Parking");
        slotnum=findViewById(R.id.slotnum);
        slotnum.setText(String.valueOf(slotnumb));
        edate =  findViewById(R.id.edate);
        tvedate=  findViewById(R.id.tvedate);

        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holddate();
            }
        });

        entime = findViewById(R.id.entime);
        tventime=  findViewById(R.id.tventime);
        tvenap= findViewById(R.id.tvenap);
        button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvedate.getText().equals("NA")) {
                    Toast.makeText(booktheslots.this, "Enter the Date first", Toast.LENGTH_LONG).show();
                }else{
                greenred();}
            }
        });
        entime.setOnClickListener(view -> holdenterytime());


        extime =  findViewById(R.id.extime);
        tvextime=  findViewById(R.id.tvextime);
        tvexap= findViewById(R.id.tvexap);
        extime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holdextime();

            }
        });
        check1 =  findViewById(R.id.check1);
        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a =etvn.getText().toString().trim();
                if (tvedate.getText().equals("NA")) {
                    Toast.makeText(booktheslots.this, "Enter the Date first", Toast.LENGTH_LONG).show();
                } else if (tventime.getText().equals("NA")) {
                    Toast.makeText(booktheslots.this, "Enter the Entry time first", Toast.LENGTH_LONG).show();
                } else if (tvextime.getText().equals("NA")) {
                    Toast.makeText(booktheslots.this, "Enter the Exit time first", Toast.LENGTH_LONG).show();
                }else if(selrb.getText().equals("NA")){
                    Toast.makeText(booktheslots.this, "Select Vehicle type", Toast.LENGTH_LONG).show();
                }else if(a.isEmpty()){
                    etvn.setError("Enter the Vehicle number");
                    etvn.requestFocus();
                }
                else{

                    builder.setTitle("Smart Parking");
                    builder.setIcon(R.drawable.parkinglogo);
                    builder.setMessage("Please wait and don't close the app");
                    builder.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    builder.setIndeterminate(false);
                    builder.setCancelable(false);
                    builder.show();

                    checkban();

                }

            }
            });


    }





    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        selrb.setText(radioButton.getText());
    }






    private void checkban() {
        String bany=etvn.getText().toString();
        FirebaseDatabase.getInstance().getReference("bandetails").child(bany).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Boolean a=snapshot.getValue(Boolean.class);
                    if(a.equals(Boolean.TRUE)){
                        Toast.makeText(booktheslots.this, "Vehcile is restricted, Reinstate vehicle by paying the fine", Toast.LENGTH_LONG).show();
                        builder.dismiss();
                    }else{
                        builder.setProgress(10);
                        checkslotavailability();
                    }
                }else{
                    builder.setProgress(10);
                    checkslotavailability();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(booktheslots.this, "Database error please try again later", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkslotavailability() {
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        FirebaseDatabase.getInstance().getReference("invoice").child(parkingname).child("count").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Long a= currentData.getValue(Long.class);
                if (a != null) {
                    Long b = a + 1;
                    currentData.setValue(b);
                    valuee = b;
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if(committed) {
                    String etdate = String.valueOf(tvedate.getText());
                    String eedate = etdate.replace("/", "");
                    final String eedate1 = eedate;
                    Intent intent = getIntent();
                    String parkingname = intent.getStringExtra("parkingname");
                    String slotnumb = String.valueOf(slotnum.getText());
                    String patha = "slot" + slotnumb;
                    reference = FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        builder.setProgress(30);
                                        Available();
                                    }
                                }, 100);
                            }
                            else {
                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        builder.setProgress(30);
                                        NotAvailable();
                                    }
                                }, 100);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            builder.dismiss();
                            String a = error.toString();
                            Toast.makeText(booktheslots.this, a, Toast.LENGTH_LONG).show();}
                    });
                }

                if(error!=null){
                    builder.dismiss();
                    Toast.makeText(booktheslots.this, "Toast to a failure", Toast.LENGTH_LONG).show();}
            }



        });
    }
























    private void getdelay() {
        int b = (int) (Math.random() * (maxy - miny + 1) + miny);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                builder.setProgress(70);
                checkhuvakina();
            }
            },b);
    }

    private void checkhuvakina() {
        String eedate = String.valueOf(tvedate.getText());
        String etdate = eedate.replace("/", "");
        final String eedate1 = etdate;
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        String slotnumb = String.valueOf(slotnum.getText());
        String patha = "slot" + slotnumb;
        reference = FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slotstatus slotstate = snapshot.getValue(slotstatus.class);
                if (slotstate != null) {
                    String am12 = slotstate.am12;
                    String am1 = slotstate.am1;
                    String am2 = slotstate.am2;
                    String am3 = slotstate.am3;
                    String am4 = slotstate.am4;
                    String am5 = slotstate.am5;
                    String am6 = slotstate.am6;
                    String am7 = slotstate.am7;
                    String am8 = slotstate.am8;
                    String am9 = slotstate.am9;
                    String am10 = slotstate.am10;
                    String am11 = slotstate.am11;
                    String pm12 = slotstate.pm12;
                    String pm1 = slotstate.pm1;
                    String pm2 = slotstate.pm2;
                    String pm3 = slotstate.pm3;
                    String pm4 = slotstate.pm4;
                    String pm5 = slotstate.pm5;
                    String pm6 = slotstate.pm6;
                    String pm7 = slotstate.pm7;
                    String pm8 = slotstate.pm8;
                    String pm9 = slotstate.pm9;
                    String pm10 = slotstate.pm10;
                    String pm11 = slotstate.pm11;
                    setknow(am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10,
                            am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8,
                            pm9, pm10, pm11);
                    for (int i = 0; i < 24; i++) {
                        if (i >= entrytimee && i < exittimee) {
                            if (!know[i].contains(etvn.getText().toString().trim())) {
                                nowwhat = false;
                                break;
                            }
                        }
                    }
                    if(nowwhat){
                        String payment = pay.getText().toString();
                        PaymentNow(payment);
                    }else{
                        FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                for (int i = entrytimee; i < exittimee; i++) {
                                    if(know[i].contains(etvn.getText().toString().trim())){
                                        String aphour;
                                        if (i > 12) {
                                            int nt = i - 12;
                                            aphour = "pm" + nt;
                                        } else if (i == 0) {
                                            int nt = 12;
                                            aphour = "am" + nt;
                                        } else if (i == 12) {
                                            aphour = "pm" + i;
                                        } else {
                                            aphour = "am" + i;
                                        }
                                        currentData.child(aphour).setValue("notb");
                                    }
                                }
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                if(committed){
                                    builder.dismiss();
                                    nowwhat=true;
                                    Toast.makeText(booktheslots.this, "Concurrent booking issue found. Check slot availability and try again or try booking another slot!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(booktheslots.this, "Some error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void newtry(String sat) {

        String eedate = String.valueOf(tvedate.getText());
        String etdate = eedate.replace("/", "");
        final String eedate1 = etdate;
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        String slotnumb = String.valueOf(slotnum.getText());
        String patha = "slot" + slotnumb;
        FirebaseDatabase.getInstance().getReference("Vehiclenumber").child(parkingname).child(eedate1).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                for (int i = entrytimee; i < exittimee; i++) {
                    if(know[i].contains(etvn.getText().toString().trim())){
                        String aphour;
                        if (i > 12) {
                            int nt = i - 12;
                            aphour = "pm" + nt;
                        } else if (i == 0) {
                            int nt = 12;
                            aphour = "am" + nt;
                        } else if (i == 12) {
                            aphour = "pm" + i;
                        } else {
                            aphour = "am" + i;
                        }
                        String a=currentData.child(aphour).getValue(String.class);
                            currentData.child(aphour).setValue(a + "slot" + slotnumb + "_" + valuee + "_" + etvn.getText().toString().trim() + "-");
                    }
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if(committed){
                    savedata(valuee,sat);


                }else{
                    for (int i = entrytimee; i < exittimee; i++) {
                        if(know[i].contains(etvn.getText().toString().trim())){
                            String aphour;
                            if (i > 12) {
                                int nt = i - 12;
                                aphour = "pm" + nt;
                            } else if (i == 0) {
                                int nt = 12;
                                aphour = "am" + nt;
                            } else if (i == 12) {
                                aphour = "pm" + i;
                            } else {
                                aphour = "am" + i;
                            }
                            FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1).child(aphour).setValue("notb");
                            Toast.makeText(booktheslots.this, "! more error", Toast.LENGTH_SHORT).show();
                            builder.dismiss();
                        }
                }
            }
        };

    });
    }


    private void savedata(long b,String s) {
        builder.setProgress(95);
        String eedate = String.valueOf(tvedate.getText());
        String etdate = eedate.replace("/", "");
        final String eedate1 = etdate;
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        FirebaseDatabase.getInstance().getReference("booking").child(parkingname).child(eedate1).child(String.valueOf(b)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData cutData) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return Transaction.abort();
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                    String currentDateandTime = sdf.format(new Date());
                    userID = user.getUid();
                    String slot = slotnum.getText().toString();
                    String date = tvedate.getText().toString();
                    String payment = pay.getText().toString();
                    String entry = tvenap.getText().toString();
                    String exit = tvexap.getText().toString();
                    String vn =etvn.getText().toString();
                    String vt =selrb.getText().toString();
                    String er=entrytimee+"-"+exittimee;
                    Bookingdetail detail = new Bookingdetail(slot, userID, entry, exit, date, payment, vn, vt,currentDateandTime,er,s);
                    cutData.setValue(detail);
                    return Transaction.success(cutData);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot cutData) {

                if(committed){
                    Intent intent = new Intent(booktheslots.this, Carpay.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("parkingname",parkingname);
                    intent.putExtra("date",eedate1);
                    intent.putExtra("invoice",valuee);
                    builder.dismiss();
                    startActivity(intent);
                        }
                    }});
                }

    private void PaymentNow(String payment) {



        String numericPart = payment.replaceAll("[^0-9.]", "");
        final Activity activity=this;
        Checkout checkout=new Checkout();
        checkout.setKeyID("********************************");
        checkout.setImage(R.drawable.parkinglogo);
        double fa= Float.parseFloat(numericPart)*100;
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userEmail =user.getEmail();
                if(snapshot.exists()){
                    number=snapshot.getValue(String.class);
                    try{
                        JSONObject options = new JSONObject();
                        options.put("name","ParkWell");
                        options.put("description","Reference No. #"+valuee);
                        options.put("currency","INR");
                        options.put("theme.color","#3399cc");
                        options.put("amount",fa+"");
                        options.put("prefill.email",userEmail);
                        options.put("prefill.contact",number);
                        checkout.open(activity,options);
                    }catch (Exception e){
                        builder.dismiss();
                        String eedate = String.valueOf(tvedate.getText());
                        String etdate = eedate.replace("/", "");
                        final String eedate1 = etdate;
                        Intent intent = getIntent();
                        String parkingname = intent.getStringExtra("parkingname");
                        String slotnumb = String.valueOf(slotnum.getText());
                        String patha = "slot" + slotnumb;

                        reference = FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
                        reference.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                for(int i=entrytimee;i<exittimee;i++){
                                    String aphour;
                                    if(i>12){
                                        int nt=i-12;
                                        aphour="pm"+nt;
                                    }else if(i==0){
                                        int nt=12;
                                        aphour="am"+nt;
                                    }else if(i==12){
                                        aphour="pm"+i;
                                    }
                                    else{
                                        aphour="am"+i;
                                    }
                                    currentData.child(aphour).setValue("notb");
                                }

                                return Transaction.success(currentData);
                            }


                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                if(committed){
                                    tvedate.setText("Na");
                                    pay.setText("Na");
                                    tvenap.setText("Na");
                                    tvexap.setText("Na");
                                    etvn.setText("");
                                    selrb.setText("Na");
                                    builder.dismiss();
                                    Toast.makeText(booktheslots.this, "Payment not successful, Payment Gateway issue", Toast.LENGTH_SHORT).show();
                                }
                                if (error!=null){
                                    tvedate.setText("Na");
                                    pay.setText("Na");
                                    tvenap.setText("Na");
                                    tvexap.setText("Na");
                                    etvn.setText("");
                                    selrb.setText("Na");
                                    Toast.makeText(booktheslots.this,"Complete mess up",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }else{
                    try{
                        JSONObject options = new JSONObject();
                        options.put("name","ParkWell");
                        options.put("description","Reference No. #"+valuee);
                        options.put("currency","INR");
                        options.put("theme.color","#3399cc");
                        options.put("amount",fa+"");
                        options.put("prefill.email",userEmail);
                        checkout.open(activity,options);
                    }catch (Exception e){
                        builder.dismiss();
                        String eedate = String.valueOf(tvedate.getText());
                        String etdate = eedate.replace("/", "");
                        final String eedate1 = etdate;
                        Intent intent = getIntent();
                        String parkingname = intent.getStringExtra("parkingname");
                        String slotnumb = String.valueOf(slotnum.getText());
                        String patha = "slot" + slotnumb;

                        reference = FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
                        reference.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                for(int i=entrytimee;i<exittimee;i++){
                                    String aphour;
                                    if(i>12){
                                        int nt=i-12;
                                        aphour="pm"+nt;
                                    }else if(i==0){
                                        int nt=12;
                                        aphour="am"+nt;
                                    }else if(i==12){
                                        aphour="pm"+i;
                                    }
                                    else{
                                        aphour="am"+i;
                                    }
                                    currentData.child(aphour).setValue("notb");
                                }

                                return Transaction.success(currentData);
                            }


                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                if(committed){
                                    tvedate.setText("Na");
                                    pay.setText("Na");
                                    tvenap.setText("Na");
                                    tvexap.setText("Na");
                                    etvn.setText("");
                                    selrb.setText("Na");
                                    builder.dismiss();
                                    Toast.makeText(booktheslots.this, "Payment not successful, Payment Gateway issue", Toast.LENGTH_SHORT).show();
                                }
                                if (error!=null){
                                    tvedate.setText("Na");
                                    pay.setText("Na");
                                    tvenap.setText("Na");
                                    tvexap.setText("Na");
                                    etvn.setText("");
                                    selrb.setText("Na");
                                    Toast.makeText(booktheslots.this,"Complete mess up",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private void Available() {
        String eedate = String.valueOf(tvedate.getText());
        String etdate = eedate.replace("/", "");
        final String eedate1 = etdate;
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        String slotnumb = String.valueOf(slotnum.getText());
        String patha = "slot" + slotnumb;
        reference = FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slotstatus slotstate = snapshot.getValue(slotstatus.class);
                if (slotstate != null) {
                    String am12 = slotstate.am12;
                    String am1 = slotstate.am1;
                    String am2 = slotstate.am2;
                    String am3 = slotstate.am3;
                    String am4 = slotstate.am4;
                    String am5 = slotstate.am5;
                    String am6 = slotstate.am6;
                    String am7 = slotstate.am7;
                    String am8 = slotstate.am8;
                    String am9 = slotstate.am9;
                    String am10 = slotstate.am10;
                    String am11 = slotstate.am11;
                    String pm12 = slotstate.pm12;
                    String pm1 = slotstate.pm1;
                    String pm2 = slotstate.pm2;
                    String pm3 = slotstate.pm3;
                    String pm4 = slotstate.pm4;
                    String pm5 = slotstate.pm5;
                    String pm6 = slotstate.pm6;
                    String pm7 = slotstate.pm7;
                    String pm8 = slotstate.pm8;
                    String pm9 = slotstate.pm9;
                    String pm10 = slotstate.pm10;
                    String pm11 = slotstate.pm11;
                    setknow(am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10,
                            am11, pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8,
                            pm9, pm10, pm11);
                    builder.setProgress(50);
                    for (int i = 0; i < 24; i++) {
                        if(i>=entrytimee && i <exittimee) {
                            if (know[i].contains("booked")) {
                                nowwhat = false;
                                break;
                            }
                        }
                    }
                    if (nowwhat) {
                        FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                                for(int i=entrytimee;i<exittimee;i++) {
                                    String aphour;
                                    if (i > 12) {
                                        int nt = i - 12;
                                        aphour = "pm" + nt;
                                    } else if (i == 0) {
                                        int nt = 12;
                                        aphour = "am" + nt;
                                    } else if (i == 12) {
                                        aphour = "pm" + i;
                                    } else {
                                        aphour = "am" + i;
                                    }
                                        currentData.child(aphour).setValue("booked_" + valuee + "_" + etvn.getText().toString().trim());
                                    }
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                if(committed){
                                  getdelay();
                                }else{
                                    Toast.makeText(booktheslots.this, "Some error happened {CE}", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }else{
                        nowwhat=true;
                        builder.dismiss();
                        Toast.makeText(booktheslots.this, "Slot booked already", Toast.LENGTH_SHORT).show();
                    }
                }}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                builder.dismiss();
                Toast.makeText(booktheslots.this,"Unknown Error",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void NotAvailable() {
        String eedate = String.valueOf(tvedate.getText());
        String etdate = eedate.replace("/", "");
        final String eedate1 = etdate;
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        String slotnumb = String.valueOf(slotnum.getText());
        String patha = "slot" + slotnumb;

            for(int i=0; i<24;i++){
                    know[i]="notb";
            }
            slotstatus slotupdate = new slotstatus(know[0],know[1],know[2],know[3],know[4],know[5],know[6],know[7],know[8],know[9],
                    know[10],know[11],know[12],know[13],know[14],know[15],know[16],know[17],know[18],know[19],know[20],know[21],
                    know[22],know[23]);

        reference= FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
        reference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                currentData.setValue(slotupdate);
                return Transaction.success(currentData);
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                if(committed){
                    FirebaseDatabase.getInstance().getReference("Vehiclenumber").child(parkingname).child(eedate1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Available();
                            }else{
                                for(int i=0; i<24;i++){
                                    know[i]="init-";
                                }
                                slotstatus slotupdate1 = new slotstatus(know[0],know[1],know[2],know[3],know[4],know[5],know[6],know[7],know[8],know[9],
                                        know[10],know[11],know[12],know[13],know[14],know[15],know[16],know[17],know[18],know[19],know[20],know[21],
                                        know[22],know[23]);
                                reference= FirebaseDatabase.getInstance().getReference("Vehiclenumber").child(parkingname).child(eedate1);
                                reference.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        currentData.setValue(slotupdate1);
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                        if(committed){
                                            Available();
                                        }else{
                                            Toast.makeText(booktheslots.this, "Error Occured 2", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(booktheslots.this, "Error Occured 3", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{

                    Toast.makeText(booktheslots.this, "Error Occured 1", Toast.LENGTH_SHORT).show();
                }

            }
        });








    }






    private void setknow(String am12,String am1, String am2, String am3,String am4,String am5, String am6,String am7,
                         String am8, String am9,String am10, String am11,String pm12,String pm1,String pm2,String pm3,String pm4,
                         String pm5,String pm6,String pm7,String pm8, String pm9,String pm10,String pm11) {
        know[0]=am12;
        know[1]=am1;
        know[2]=am2;
        know[3]=am3;
        know[4]=am4;
        know[5]=am5;
        know[6]=am6;
        know[7]=am7;
        know[8]=am8;
        know[9]=am9;
        know[10]=am10;
        know[11]=am11;
        know[12]=pm12;
        know[13]=pm1;
        know[14]=pm2;
        know[15]=pm3;
        know[16]=pm4;
        know[17]=pm5;
        know[18]=pm6;
        know[19]=pm7;
        know[20]=pm8;
        know[21]=pm9;
        know[22]=pm10;
        know[23]=pm11;
    }


    private void holddate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.TimeDatePickerTheme, (datePicker, year1, month1, day1) -> tvedate.setText(day1 + "/" + (month1 + 1) + "/" + year1), year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private void greenred() {
        String eedate = String.valueOf(tvedate.getText());
        String etdate = eedate.replace("/", "");
        final String eedate1 = etdate;
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        String slotnumb = String.valueOf(slotnum.getText());
        String patha = "slot" + slotnumb;
        reference = FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null && snapshot.exists()){
                    gr();
                }else{
                    agr();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(booktheslots.this,"Database error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void agr() {
        am1.setBackgroundColor(getResources().getColor(R.color.green));
                am2.setBackgroundColor(getResources().getColor(R.color.green));
        am3.setBackgroundColor(getResources().getColor(R.color.green));
                am4.setBackgroundColor(getResources().getColor(R.color.green));
        am5.setBackgroundColor(getResources().getColor(R.color.green));
                am6.setBackgroundColor(getResources().getColor(R.color.green));
        am7.setBackgroundColor(getResources().getColor(R.color.green));
                am8.setBackgroundColor(getResources().getColor(R.color.green));
        am9.setBackgroundColor(getResources().getColor(R.color.green));
        am10.setBackgroundColor(getResources().getColor(R.color.green));
                am11.setBackgroundColor(getResources().getColor(R.color.green));
        am12.setBackgroundColor(getResources().getColor(R.color.green));
                pm1.setBackgroundColor(getResources().getColor(R.color.green));
        pm2.setBackgroundColor(getResources().getColor(R.color.green));
        pm3.setBackgroundColor(getResources().getColor(R.color.green));
                pm4.setBackgroundColor(getResources().getColor(R.color.green));
        pm5.setBackgroundColor(getResources().getColor(R.color.green));
        pm6.setBackgroundColor(getResources().getColor(R.color.green));
                pm7.setBackgroundColor(getResources().getColor(R.color.green));
        pm8.setBackgroundColor(getResources().getColor(R.color.green));
                pm9.setBackgroundColor(getResources().getColor(R.color.green));
        pm10.setBackgroundColor(getResources().getColor(R.color.green));
                        pm11.setBackgroundColor(getResources().getColor(R.color.green));
                                pm12.setBackgroundColor(getResources().getColor(R.color.green));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
        String currentDateandTime = sdf.format(new Date());
                                tvupd.setText(currentDateandTime);
        tvdteup.setText(tvedate.getText().toString());
    }

    private void gr() {
        String eedate = String.valueOf(tvedate.getText());
        String etdate = eedate.replace("/", "");
        final String eedate1 = etdate;
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        String slotnumb = String.valueOf(slotnum.getText());
        String patha = "slot" + slotnumb;
        reference = FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    slotstatus slotstate = snapshot.getValue(slotstatus.class);
                    if (slotstate != null) {
                        String[] amSlots = {slotstate.am12, slotstate.am1, slotstate.am2, slotstate.am3, slotstate.am4, slotstate.am5, slotstate.am6, slotstate.am7, slotstate.am8, slotstate.am9, slotstate.am10, slotstate.am11};
                        String[] pmSlots = {slotstate.pm12, slotstate.pm1, slotstate.pm2, slotstate.pm3, slotstate.pm4, slotstate.pm5, slotstate.pm6, slotstate.pm7, slotstate.pm8, slotstate.pm9, slotstate.pm10, slotstate.pm11};
                        View[] amViews = {am12, am1, am2, am3, am4, am5, am6, am7, am8, am9, am10, am11};
                        View[] pmViews = {pm12, pm1, pm2, pm3, pm4, pm5, pm6, pm7, pm8, pm9, pm10, pm11};

                        for (int i = 0; i < amSlots.length; i++) {
                            if (amSlots[i] != null) {
                                String cleanedSlot = amSlots[i].trim().toLowerCase();
                                System.out.println("AM Slot[" + i + "]: " + cleanedSlot);
                                if (cleanedSlot.contains("notb")) {
                                    amViews[i].setBackgroundColor(getResources().getColor(R.color.green));
                                } else {
                                    amViews[i].setBackgroundColor(getResources().getColor(R.color.red));
                                }
                            }
                        }

                        for (int i = 0; i < pmSlots.length; i++) {
                            if (pmSlots[i] != null) {
                                String cleanedSlot = pmSlots[i].trim().toLowerCase();
                                System.out.println("PM Slot[" + i + "]: " + cleanedSlot);
                                if (cleanedSlot.contains("notb")) {
                                    pmViews[i].setBackgroundColor(getResources().getColor(R.color.green));
                                } else {
                                    pmViews[i].setBackgroundColor(getResources().getColor(R.color.red));
                                }
                            }
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
                        String currentDateandTime = sdf.format(new Date());
                        tvupd.setText(currentDateandTime);
                        tvdteup.setText(tvedate.getText().toString());

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void payment() {
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        String ab = (String) tventime.getText();
        String[] values = ab.split(":");
        int b = Integer.parseInt(values[0]);
        String ba = (String) tvextime.getText();
        String[] valuess = ba.split(":");
        int a = Integer.parseInt(valuess[0]);
        int c = a-b;
        FirebaseDatabase.getInstance().getReference("cost").child(parkingname).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long cost = snapshot.getValue(Long.class);
                if(cost!=null){
                    int tc = Math.toIntExact(cost);
                    float tcc =tc*c;
                    pay.setText("₹ " + tcc);

                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvextime.setText("NA");
                tvexap.setText("NA");
                Toast.makeText(booktheslots.this,"Database Error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void holdenterytime() {
        Calendar calendar = Calendar.getInstance();
        int chour = calendar.get(Calendar.HOUR_OF_DAY);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        if (tvedate.getText().equals("NA")) {
            Toast.makeText(this, "Enter the date first", Toast.LENGTH_LONG).show();
        } else if (tvedate.getText().equals(day + "/" + (month + 1) + "/" + year)) {
            boolean timeformat = DateFormat.is24HourFormat(this);
            timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hour, int min, int second) {
                    min=0;
                        if (chour <= hour) {
                            entrytimee=hour;
                            String entimee = hour + ":" + min + min;
                            tventime.setText(entimee);
                            if(hour>12){
                                int nt=hour-12;
                                String aphour=nt + ":" + min + min+" pm";
                                tvenap.setText(aphour);
                            }else if(hour==0){
                                int nt=12;
                                String aphour=nt + ":" + min + min+ " am";
                                tvenap.setText(aphour);
                            }else if(hour==12){
                                int nt=hour;
                                String aphour=nt + ":" + min + min+" pm";
                                tvenap.setText(aphour);}
                            else{
                                String aphour=hour + ":" + min + min+ " am";
                                tvenap.setText(aphour);
                            }
                            tvextime.setText("NA");
                            tvexap.setText("NA");
                        }
                        else {
                            tvextime.setText("NA");
                            tvexap.setText("NA");
                            Toast.makeText(booktheslots.this, "Invalid time", Toast.LENGTH_LONG).show();
                        }
                }

            }, chour, 0, timeformat);
            timePickerDialog.enableMinutes(false);
            timePickerDialog.setAccentColor(getColor(R.color.cursorlogin));
            timePickerDialog.show(getSupportFragmentManager(), "Time");
        } else {
            boolean timeformat = DateFormat.is24HourFormat(this);
            timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hour, int min, int second) {
                    min=0;
                    entrytimee=hour;
                    String entimee=hour + ":" + min+ min;
                    tventime.setText(entimee);
                    if(hour>12){
                        int nt=hour-12;
                        String aphour=nt + ":" + min+ min + " pm";
                        tvenap.setText(aphour);
                    }else if(hour==0){
                        int nt=12;
                        String aphour=nt + ":" + min+ min + " am";
                        tvenap.setText(aphour);
                    }else if(hour==12){
                        String aphour=hour + ":" + min+ min + " pm";
                        tvenap.setText(aphour);
                    }
                    else{
                        String aphour=hour + ":" + min+ min + " am";
                        tvenap.setText(aphour);
                    }
                    tvextime.setText("NA");
                    tvexap.setText("NA");
                }

            }, chour, 0, timeformat);
            timePickerDialog.enableMinutes(false);
            timePickerDialog.setAccentColor(getColor(R.color.cursorlogin));
            timePickerDialog.show(getSupportFragmentManager(), "Time");
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent =new Intent(booktheslots.this,loggedinn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    void holdextime() {
        Calendar calendar = Calendar.getInstance();
        int ehour = calendar.get(Calendar.HOUR_OF_DAY);

        if (tvedate.getText().equals("NA")) {
            Toast.makeText(this, "Enter the date first", Toast.LENGTH_LONG).show();
        } else if (tventime.getText().equals("NA")) {
            Toast.makeText(this, "Enter the entry time first", Toast.LENGTH_LONG).show();
        } else {
            boolean etimeformat = DateFormat.is24HourFormat(this);
            timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hour, int min, int second) {
                    String timeen = (String) tventime.getText();
                    String[] values = timeen.split(":");
                    int enhour = Integer.parseInt(values[0]);
                    min=0;
                    if (enhour < hour) {
                        exittimee = hour;
                        String extimee = hour + ":" + min+ min;
                        tvextime.setText(extimee);
                        payment();
                        if (hour > 12) {
                            int nt = hour - 12;
                            String aphour = nt + ":" + min+ min + " pm";
                            tvexap.setText(aphour);
                        } else if (hour == 0) {
                            int nt = 12;
                            String aphour = nt + ":" + min+ min + " am";
                            tvexap.setText(aphour);
                        }else if (hour == 12) {
                            int nt = 12;
                            String aphour = nt + ":" + min+ min + " pm";
                            tvexap.setText(aphour);}
                        else {
                            String aphour = hour + ":" + min+ min + " am";
                            tvexap.setText(aphour);
                        }

                    }
                    else if(hour==0){
                        exittimee = 24;
                        String extimee = 24 + ":" + min+ min;
                        tvextime.setText(extimee);
                        String aphour = 12 + ":" + min+ min + " am";
                        tvexap.setText(aphour);
                        payment();
                    }
                    else{
                        Toast.makeText(booktheslots.this, "Invaild time", Toast.LENGTH_LONG).show();}
                }

            }, ehour, 0, etimeformat);
            timePickerDialog.enableMinutes(false);
            timePickerDialog.setAccentColor(getColor(R.color.cursorlogin));
            timePickerDialog.show(getSupportFragmentManager(), "Time");
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        builder.setProgress(80);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newtry(s);
            }
        }, 1000);
    }

    @Override
    public void onPaymentError(int ii, String s) {

        String eedate = String.valueOf(tvedate.getText());
        String etdate = eedate.replace("/", "");
        final String eedate1 = etdate;
        Intent intent = getIntent();
        String parkingname = intent.getStringExtra("parkingname");
        String slotnumb = String.valueOf(slotnum.getText());
        String patha = "slot" + slotnumb;

        reference = FirebaseDatabase.getInstance().getReference("availability").child(parkingname).child(patha).child(eedate1);
        reference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                for(int i=entrytimee;i<exittimee;i++){
                    String aphour;
                    if(i>12){
                        int nt=i-12;
                        aphour="pm"+nt;
                    }else if(i==0){
                        int nt=12;
                        aphour="am"+nt;
                    }else if(i==12){
                        aphour="pm"+i;
                    }
                    else{
                        aphour="am"+i;
                    }
                    currentData.child(aphour).setValue("notb");
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if(committed){
                    Toast.makeText(booktheslots.this, "Payment not successful", Toast.LENGTH_SHORT).show();
                    tvedate.setText("Na");
                    pay.setText("Na");
                    tvenap.setText("Na");
                    tvexap.setText("Na");
                    etvn.setText("");
                    selrb.setText("Na");
                    builder.dismiss();
                }
                if (error!=null){
                    Toast.makeText(booktheslots.this,"Complete mess up",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


