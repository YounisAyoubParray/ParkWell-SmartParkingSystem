package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import android.widget.WrapperListAdapter;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class Relocation extends AppCompatActivity {
    private EditText slotnumber,vehcilenumber;
    private Boolean con;
    private Button confirm;
    FirebaseUser user;
    String cid;
    String goat ;
    ArrayList<String> check;
    String vcn;
    private ProgressDialog builder;
    private Boolean nowwhat;
    private final String[] know = new String[24];
    int entrytimee;
    int exittimee;
    final int miny=500,maxy=700;
    private Handler handler = new Handler();


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
        setContentView(R.layout.activity_relocation);
        slotnumber=findViewById(R.id.slotkikhabar);
        check=new ArrayList<>();
        builder = new ProgressDialog(Relocation.this);
        vehcilenumber=findViewById(R.id.banedetet1145);
        confirm=findViewById(R.id.button8);
        nowwhat=true;
        user= FirebaseAuth.getInstance().getCurrentUser();
        cid= user.getUid();
        con=false;
        Intent intent=getIntent();
        String a=intent.getStringExtra("parkingname");
        slotnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String s=slotnumber.getText().toString();
                    if(s.length()!=0){
                        String sn="slot"+s;
                        FirebaseDatabase.getInstance().getReference("parkings").child(a).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(sn)){
                                    Boolean at=snapshot.child(sn).getValue(Boolean.class);
                                    if(at.equals(Boolean.TRUE)){
                                        Toast.makeText(Relocation.this, "Slot is empty for future assistance contact parking officials", Toast.LENGTH_LONG).show();
                                    }else{
                                        authcheck(a,sn);
                                    }
                                }else{
                                    Toast.makeText(Relocation.this, "Slot number doesn't exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Relocation.this, "Opps error occured", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        con=false;
                    }
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuetoend(a);
            }
        });
        vehcilenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                if(con.equals(Boolean.FALSE)){
                    if(slotnumber.getText().toString().equals("")){
                        vehcilenumber.setText("");
                        Toast.makeText(Relocation.this, "First enter the slot number you booked", Toast.LENGTH_SHORT).show();
                    }else{
                        vehcilenumber.setText("");
                        Toast.makeText(Relocation.this, "Check the slot number you entered", Toast.LENGTH_SHORT).show();
                    }
                }
                }
            }
        });





    }

    private void continuetoend(String a) {
        if(slotnumber.getText().toString().isEmpty()){
            Toast.makeText(this, "First enter the slot number you booked", Toast.LENGTH_SHORT).show();
        }else if(vehcilenumber.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter the vehicle number first", Toast.LENGTH_SHORT).show();
        }else{
            builder.setTitle("Park Well");
            builder.setIcon(R.drawable.parkinglogo);
            builder.setMessage("Please wait and don't close the app");
            builder.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            builder.setIndeterminate(false);
            builder.setCancelable(false);
            builder.show();
            reserverefund();
            int timeLimitInMillis = 12000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                }
            }, timeLimitInMillis);
        }



    }
    private void dismissProgressDialog() {
        if (builder != null && builder.isShowing()) {
            builder.dismiss();
        }
    }
    private void cancelDismissHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelDismissHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelDismissHandler();
    }

    private void thealgo(String a) {

        FirebaseDatabase.getInstance().getReference("parkings").child(a).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    check.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Boolean childValue = childSnapshot.getValue(Boolean.class);
                        if (childValue.equals(Boolean.TRUE)) {
                            check.add(childSnapshot.getKey());
                        }
                    }
                    if(check.isEmpty()){
                        builder.setProgress(80);
                        Refund();
                    }else{
                        builder.setProgress(20);
                        checkthecheck(a,0);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                builder.dismiss();
                Toast.makeText(Relocation.this, "Try again. Sorry for inconvenience", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkthecheck(String a,int i) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn = month + 1;
        String datee = day + "" + mn + year;
        builder.setProgress(30);
        FetaBool(a, datee, String.valueOf(check.get(i)),i);
        }




    private void FetaBool(String a, String datee, String m,int i) {
        FirebaseDatabase.getInstance().getReference("availability").child(a).child(m).child(datee).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Available(a, m, datee,i);

                } else {
                    NotAvailable(a, m, datee,i);
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String a = error.toString();
                Toast.makeText(Relocation.this, a, Toast.LENGTH_LONG).show();
            }
        });

    }


    private void checkhuvakina(String a, String n, String d,int ig) {
        FirebaseDatabase.getInstance().getReference("availability").child(a).child(n).child(d).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            if (!know[i].contains(vcn)) {
                                nowwhat = false;
                                break;
                            }
                        }
                    }

                    if (!nowwhat) {
                        FirebaseDatabase.getInstance().getReference("availability").child(a).child(n).child(d).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                for (int i = entrytimee; i < exittimee; i++) {
                                    if (know[i].contains(vcn)) {
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
                                if (committed) {
                                    if(check.size()>ig+1){
                                        nowwhat = true;
                                        checkthecheck(a,ig+1);
                                    }
                                    else{
                                        Refund();
                                    }
                                }
                            }
                        });
                    } else {
                        builder.setProgress(60);
                        newtry(a, n, d);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Relocation.this, "Some error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Available(String a, String n, String d,int ig) {

        FirebaseDatabase.getInstance().getReference("availability").child(a).child(n).child(d).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            if (know[i].contains("booked")) {
                                nowwhat = false;
                                break;
                            }
                        }
                    }
                    if (nowwhat) {
                        FirebaseDatabase.getInstance().getReference("availability").child(a).child(n).child(d).runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                                for (int i = entrytimee; i < exittimee; i++) {
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
                                    currentData.child(aphour).setValue("booked_" + goat + "R_" + vcn);
                                }
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                if (committed) {
                                    getdelay(a, n, d,ig);
                                } else {
                                    Toast.makeText(Relocation.this, "Some error happened", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    } else {
                        nowwhat = true;
                        if(check.size()>ig+1){
                            checkthecheck(a,ig+1);
                            }else{
                            Refund();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Relocation.this, "Unknown Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getdelay(String a, String n, String d, int ig) {
        int b = (int) (Math.random() * (maxy - miny + 1) + miny);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkhuvakina(a, n, d,ig);
            }
        }, b);
    }

    private void NotAvailable(String a, String n, String d, int ig) {

        for (int i = 0; i < 24; i++) {
            know[i] = "notb";
        }
        slotstatus slotupdate = new slotstatus(know[0], know[1], know[2], know[3], know[4], know[5], know[6], know[7], know[8], know[9],
                know[10], know[11], know[12], know[13], know[14], know[15], know[16], know[17], know[18], know[19], know[20], know[21],
                know[22], know[23]);

        FirebaseDatabase.getInstance().getReference("availability").child(a).child(n).child(d).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                currentData.setValue(slotupdate);
                return Transaction.success(currentData);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                if (committed) {
                    FirebaseDatabase.getInstance().getReference("Vehiclenumber").child(a).child(d).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Available(a, n, d,ig);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Relocation.this, "Error Occured 3", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    Toast.makeText(Relocation.this, "Error Occured 1", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void reserverefund() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn=month+1;
        String datee = day+""+mn+year;
         FirebaseDatabase.getInstance().getReference("parkings").child("SrinagarR").child("R1").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Boolean ag=snapshot.getValue(Boolean.class);
            if(ag.equals(Boolean.TRUE)){
                builder.setProgress(50);
                Write1(datee);
            }else{
                builder.setProgress(10);
                thealgo("Srinagar");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            builder.dismiss();
        }
    });
    }


    private void Write1(String datee) {
        if(goat==null){
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
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
            FirebaseDatabase.getInstance().getReference("availability").child("Srinagar").child("slot"+slotnumber.getText().toString()).child(datee).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String inv=snapshot.child(aphour).getValue(String.class);
                        String[] at=inv.split("_");
                        goat=at[1];
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }
        FirebaseDatabase.getInstance().getReference("parkings").child("SrinagarR").child("R1").setValue(false);
        builder.setProgress(70);
        FirebaseDatabase.getInstance().getReference("availability").child("SrinagarR").child(datee).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.child(String.valueOf("1")).setValue("booked_" + goat + "R_" + vcn);
                } else {
                    long i =currentData.getChildrenCount();
                        currentData.child(String.valueOf(i+1)).setValue("booked_" + goat + "R_" + vcn);
                    }
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if(committed){
                    FirebaseDatabase.getInstance().getReference("booking").child("Srinagar").child(datee).child(goat).child("Relocated").setValue("R1").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                builder.setProgress(100);
                                Toast.makeText(Relocation.this, "Relocation Successful, please wait a minute", Toast.LENGTH_SHORT).show();
                                confirm(vehcilenumber.getText().toString(),"Srinagar",datee);
                            }
                        }
                    });
                }
            }
        });

    }




    private void Refund() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn=month+1;
        String datee = day+""+mn+year;
        Toast.makeText(this, "Opps No Slot found. You will receive refund within 48 hours", Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference("Refund").child("Srinagar").child(datee).child(goat).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(Relocation.this, loggedinn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


    private void authcheck(String a,String sn) {
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
        FirebaseDatabase.getInstance().getReference("availability").child(a).child(sn).child(datee).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String inv=snapshot.child(aphour).getValue(String.class);
                    if(inv.contains("notb")){
                        Toast.makeText(Relocation.this, "Slot not booked currently", Toast.LENGTH_LONG).show();
                    }else{
                        String[] at=inv.split("_");
                        if(at.length>0){
                            authchecker( datee,a,at[1]);
                        }
                    }
                }else{
                    Toast.makeText(Relocation.this, "Slot not booked currently", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void authchecker(String datee, String a, String s){
        FirebaseDatabase.getInstance().getReference("booking").child(a).child(datee).child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bookingdetail book = snapshot.getValue(Bookingdetail.class);
                if(book!=null){
                if(book.UserId.equals(cid)){
                    String er=book.Er;
                    String[] a=er.split("-",0);
                    goat=s;
                    vcn=book.VehcileNumber;
                    entrytimee=Integer.parseInt(a[0]);
                    exittimee=Integer.parseInt(a[1]);
                    if(snapshot.child("Relocated").exists()){
                        Toast.makeText(Relocation.this,"Vehicle relocated already contact parking authority for assistance",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Relocation.this, "Enter the vehicle number who occupied your slot", Toast.LENGTH_SHORT).show();
                        con = true;
                    }
                }else{
                    Toast.makeText(Relocation.this, "Slot is not booked by you, Contact the parking authority for assistance", Toast.LENGTH_LONG).show();
                }}else{
                    Toast.makeText(Relocation.this, "Error occurred try again later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Relocation.this, "Oops something wrong happened", Toast.LENGTH_SHORT).show();
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

    private void newtry(String a, String n,String d) {
        if(goat==null){
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
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
            FirebaseDatabase.getInstance().getReference("availability").child("Srinagar").child("slot"+slotnumber.getText().toString()).child(d).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String inv=snapshot.child(aphour).getValue(String.class);
                        String[] at=inv.split("_");
                        goat=at[1];
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }
        FirebaseDatabase.getInstance().getReference("Vehiclenumber").child(a).child(d).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                for (int i = entrytimee; i < exittimee; i++) {
                    if(know[i].contains(vcn)){
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
                        currentData.child(aphour).setValue(a  + n + "_" + goat + "R_" + vcn + "-");
                    }
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if(committed){

                        FirebaseDatabase.getInstance().getReference("booking").child(a).child(d).child(goat).child("Relocated").setValue(n).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    builder.setProgress(80);
                                    Toast.makeText(Relocation.this, "Relocation done successfully please a min", Toast.LENGTH_SHORT).show();
                                    confirm(vehcilenumber.getText().toString(), a, d);
                                }

                            }
                        });

                }else{
                    for (int i = entrytimee; i < exittimee; i++) {
                        if(know[i].contains(vcn)){
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
                            FirebaseDatabase.getInstance().getReference("availability").child(a).child(n).child(d).child(aphour).setValue("notb");
                        }
                    }
                }
            }

        });
    }

    private void confirm(String at,String a,String d) {
        builder.dismiss();
        String timenow=datenow();
        String dateofban=jusdate();

        FirebaseDatabase.getInstance().getReference("bandetails").child(at).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Boolean bj=snapshot.getValue(Boolean.class);
                    if(bj.equals(Boolean.TRUE)){
                        Intent intent = new Intent(Relocation.this, Rel.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("date",d);
                        intent.putExtra("inv",goat);
                        startActivity(intent);
                    }else{
                        abt(timenow,dateofban,at,a,d);
                    }
                }
                else{
                    gotto(timenow, dateofban,at,a,d);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void gotto(String timenow, String dateofban, String at,String a,String d) {

        FirebaseDatabase.getInstance().getReference("UnbanTries").child(at).setValue(6l).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    abit(timenow,dateofban,at,a,d);
                } else{
                    Toast.makeText(Relocation.this, "Some error happened banning!", Toast.LENGTH_SHORT).show();
                }}
        });
    }

    private void abit(String timenow, String dateofban, String at,String a,String d) {

        FirebaseDatabase.getInstance().getReference("bandetails").child(at).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    BanObj data = new BanObj(timenow,cid,dateofban,a,slotnumber.getText().toString());
                    FirebaseDatabase.getInstance().getReference("banrecords").child(at).child(String.valueOf(1)).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(Relocation.this, Rel.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("date",d);
                            intent.putExtra("inv",goat);
                            startActivity(intent);

                        }
                    });


                }
            }

        });
    }

    private void abt(String timenow, String dateofban, String at,String a,String d) {

        FirebaseDatabase.getInstance().getReference("bandetails").child(at).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("banrecords").child(at).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            long af=snapshot.getChildrenCount();
                            af=af+1;
                            BanObj data = new BanObj(timenow,cid,dateofban,a,slotnumber.getText().toString());
                            FirebaseDatabase.getInstance().getReference("banrecords").child(at).child(String.valueOf(af)).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(Relocation.this, Rel.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("date",d);
                                        intent.putExtra("inv",goat);
                                        startActivity(intent);

                                    }
                                }
                            });



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }


    private String datenow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
    private String jusdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
}