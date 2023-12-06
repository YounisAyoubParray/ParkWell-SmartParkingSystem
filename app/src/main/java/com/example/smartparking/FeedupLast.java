package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedupLast extends AppCompatActivity {
    private TextView name,email,number,inv,slot,et,ext,dt,vt,vn,amp;
    private Button ok;
    Boolean  w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedup_last);
        w=false;
        Intent intent =getIntent();
        String date =intent.getStringExtra("dat");
        String sltinv =intent.getStringExtra("invoice");
        name = findViewById(R.id.csln111);
        email =findViewById(R.id.ccinvoice111);
        number =findViewById(R.id.ccdt111);
        inv =findViewById(R.id.ccpay111);
        slot =findViewById(R.id.ccexit111);
        et =findViewById(R.id.ccvn111);
        ext =findViewById(R.id.ccvt111);
        dt =findViewById(R.id.ccemail111);
        vt =findViewById(R.id.ccnumber111);
        vn =findViewById(R.id.ccname111);
        amp =findViewById(R.id.ccentry111);
        ok = findViewById(R.id.cpaycon111);
        data(date,sltinv);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(w) {
                    Intent intent = new Intent(FeedupLast.this, Adminui.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{

                    FirebaseDatabase.getInstance().getReference("Refund").child("Srinagar").child(date).child(sltinv).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                FirebaseDatabase.getInstance().getReference("booking").child("Srinagar").child(date).child(sltinv).child("Refund").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(FeedupLast.this, "Refund Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(FeedupLast.this, Adminui.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }
                    });
            }}
        });
    }

    private void data( String date, String sltinv) {

        FirebaseDatabase.getInstance().getReference("booking").child("Srinagar").child(date).child(sltinv).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bookingdetail a = snapshot.getValue(Bookingdetail.class);
                if (a != null) {
                    String userid = a.UserId;
                    String datte = a.Date;
                    String entrytime = a.Entry;
                    String slotn =a.SlotNumber;
                    String exittime = a.Exit;
                    String Payment = a.Payment;
                    String vnumb = a.VehcileNumber;
                    String vtype = a.VehcileType;
                    FirebaseDatabase.getInstance().getReference("Users").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User p = snapshot.getValue(User.class);
                            if (p != null) {
                                String namep = p.FullName;
                                String phonen = p.Phone;
                                String e = p.Email;
                                name.setText(namep);
                                email.setText(e);
                                number.setText(phonen);
                                inv.setText(sltinv);
                                slot.setText(slotn);
                                et.setText(entrytime);
                                ext.setText(exittime);
                                dt.setText(datte);
                                vt.setText(vtype);
                                vn.setText(vnumb);
                                amp.setText(Payment);
                            }else{
                                w=true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(FeedupLast.this, "Oops error occured2", Toast.LENGTH_SHORT).show();
                            w=true;
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FeedupLast.this, "Oops error occured2", Toast.LENGTH_SHORT).show();
                w=true;
            }
        });
    }







}
