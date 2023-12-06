package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BanRecordstime extends AppCompatActivity {
    private TextView rt,dob,sln,pkn,ubt,tid,fpd,unbb,e;
    private Button Ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_recordstime);
        rt=findViewById(R.id.textView95);
        dob=findViewById(R.id.textView91);
        sln=findViewById(R.id.textView92);
        pkn=findViewById(R.id.textView93);
        ubt=findViewById(R.id.textView100);
        tid=findViewById(R.id.textView103);
        fpd=findViewById(R.id.textView102);
        unbb=findViewById(R.id.textView101);
        e=findViewById(R.id.textView107);
        Ok=findViewById(R.id.button9);
        Intent intent=getIntent();
        String a=intent.getStringExtra("vcnt");
        String b=intent.getStringExtra("offn");
        FirebaseDatabase.getInstance().getReference("banrecords").child(a).child(b).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            BanObj trr=snapshot.getValue(BanObj.class);
            if(trr!=null){
                rt.setText(b);
                dob.setText(trr.BanTime);
                sln.setText(trr.SlotNumber);
                pkn.setText(trr.ParkingName);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BanRecordstime.this, "Opps error occured", Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseDatabase.getInstance().getReference("FinePayment").child(a).child(b).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UnbanObj agj=snapshot.getValue(UnbanObj.class);
                if(agj!=null){
                    ubt.setText(agj.UnBanTime);
                    tid.setText(agj.TransactionId);
                    fpd.setText(agj.TotalFine);

                    FirebaseDatabase.getInstance().getReference("Users").child(agj.UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User  dd=snapshot.getValue(User.class);
                            if(dd!=null){
                                unbb.setText(dd.FullName);
                                e.setText(dd.Email);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BanRecordstime.this, "Opps error occured", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BanRecordstime.this, "Opps error occured", Toast.LENGTH_SHORT).show();
            }
        });
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(BanRecordstime.this,Adminui.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}