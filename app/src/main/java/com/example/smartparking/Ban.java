package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ban extends AppCompatActivity {
    private EditText ban;
    private TextView sln;
    private Button bu;
    String a="",b="";
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);
        bu=findViewById(R.id.button7);
        ban=findViewById(R.id.banedetet);
        builder = new AlertDialog.Builder(Ban.this);
        sln=findViewById(R.id.slnumberheadache);
        Intent intent =getIntent();
        a=intent.getStringExtra("report");
        b=intent.getStringExtra("slot");
        sln.setText(b);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String at=ban.getText().toString();
                if(at.isEmpty()){
                    Toast.makeText(Ban.this,"Enter the vehcile number",Toast.LENGTH_SHORT).show();
                }else{
                    Banprocess(at);
                }
            }
        });
    }

    private void Banprocess(String at) {
        builder.setTitle("Park Well");
        builder.setIcon(R.drawable.parkinglogo);
        builder.setMessage("Confirm vehcile number "+at);
        builder.setCancelable(false);
        builder.setPositiveButton(Html.fromHtml("<font color='#014077'> YES </font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            confirm(at);
            }
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#ba1e13'> NO </font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ban.setText("");
            }
        });
        builder.show();
    }

    private void confirm(String at) {

       String timenow=datenow();
       String dateofban=jusdate();

        FirebaseDatabase.getInstance().getReference("bandetails").child(at).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Boolean bj=snapshot.getValue(Boolean.class);
                    if(bj.equals(Boolean.TRUE)){
                        Toast.makeText(Ban.this,"Already banned",Toast.LENGTH_SHORT).show();
                    }else{
                       abt(timenow,dateofban,at);
                    }
                }
                else{
                    gotto(timenow, dateofban,at);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void gotto(String timenow, String dateofban, String at) {

        FirebaseDatabase.getInstance().getReference("UnbanTries").child(at).setValue(6l).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    abit(timenow,dateofban,at);
                } else{
                Toast.makeText(Ban.this, "Some error happened try again", Toast.LENGTH_SHORT).show();
            }}
        });
    }

    private void abit(String timenow, String dateofban, String at) {

        FirebaseDatabase.getInstance().getReference("bandetails").child(at).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    BanObj data = new BanObj(timenow,a,dateofban,"Srinagar",b);

                    FirebaseDatabase.getInstance().getReference("banrecords").child(at).child(String.valueOf(1)).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Ban.this,"Banned Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Ban.this, Adminui.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    });


                }
            }

    });
    }

    private void abt(String timenow, String dateofban, String at) {

        FirebaseDatabase.getInstance().getReference("bandetails").child(at).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("banrecords").child(at).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                long af=snapshot.getChildrenCount();
                                af=af+1;
                                BanObj data = new BanObj(timenow,a,dateofban,"Srinagar",b);
                                FirebaseDatabase.getInstance().getReference("banrecords").child(at).child(String.valueOf(af)).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Ban.this,"Banned Successfully",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Ban.this, Adminui.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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