package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Feedup extends AppCompatActivity {
    private TextView name,email,number,inv,slot,et,ext,dt,vt,vn,amp,ccentry33333;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedup);

        Intent intent =getIntent();
        String date =intent.getStringExtra("dat");
        String sltinv =intent.getStringExtra("invoice");
        name = findViewById(R.id.csln);
        email =findViewById(R.id.ccinvoice);
        number =findViewById(R.id.ccdt);
        inv =findViewById(R.id.ccpay);
        slot =findViewById(R.id.ccexit);
        et =findViewById(R.id.ccvn);
        ext =findViewById(R.id.ccvt);
        dt =findViewById(R.id.ccemail);
        vt =findViewById(R.id.ccnumber);
        vn =findViewById(R.id.ccname);
        amp =findViewById(R.id.ccentry);
        ok = findViewById(R.id.cpaycon);
        ccentry33333=findViewById(R.id.ccentry33333);
        data(date,sltinv);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feedup.this, Adminui.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void data( String date, String sltinv) {

        FirebaseDatabase.getInstance().getReference("booking").child("Srinagar").child(date).child(sltinv).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String agt="";
                if(snapshot.hasChild("Relocated")){
                    String am=snapshot.child("Relocated").getValue(String.class);
                    agt=" relocated to "+am;
                }
                Bookingdetail a = snapshot.getValue(Bookingdetail.class);
                if (a != null) {
                    String userid = a.UserId;
                    String datte = a.Date;
                    String entrytime = a.Entry;
                    String slotn =a.SlotNumber+agt;
                    String exittime = a.Exit;
                    String Payment = a.Payment;
                    String vnumb = a.VehcileNumber;
                    String vtype = a.VehcileType;
                    String Pid=a.PaymentId;
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
                                ccentry33333.setText(Pid);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Feedup.this, "Oops error occured2", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Feedup.this, "Oops error occured2", Toast.LENGTH_SHORT).show();
            }
        });
    }


}