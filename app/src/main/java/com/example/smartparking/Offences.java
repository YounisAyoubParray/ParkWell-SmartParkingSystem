package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
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

import java.util.ArrayList;

public class Offences extends AppCompatActivity implements AnewRecycler.OnNoteListener {
    private TextView textView87,totaloffencecount;
    private String a;
    private RecyclerView recyclerView;
    ArrayList<Tinv> d = new ArrayList<Tinv>();
    ArrayList<Tinv> dc = new ArrayList<Tinv>();
    Boolean searchinfo = false;
    private Button addtolimit;
    private AnewRecycler Adapter;
    private EditText ssearchtofflimit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offences);
        Intent intent=getIntent();
        a=intent.getStringExtra("vcn");
        addtolimit=findViewById(R.id.addtolimit);
        addtolimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("UnbanTries").child(a).setValue(5).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Offences.this, "Authorization successful", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Offlimits").child(a).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(Offences.this,Adminui.class));
                                    }else{
                                        Toast.makeText(Offences.this, "Authorization success but node deletion failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(Offences.this, "Authorization failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        textView87=findViewById(R.id.textView87);
        SpannableString spannableString = new SpannableString("Total offences by vehicle number: " + a);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), spannableString.length() - a.length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView87.setText(spannableString);
        totaloffencecount=findViewById(R.id.totaloffencecount);
        ssearchtofflimit=findViewById(R.id.ssearchstoffence);
        ssearchtofflimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newsearch(ssearchtofflimit.getText().toString());
            }
        });
        data();
        buildrecycler();
    }
    private void buildrecycler() {
        recyclerView = findViewById(R.id.recycleoffcs);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Adapter = new AnewRecycler(d, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(Adapter);
    }
    private void data() {
        FirebaseDatabase.getInstance().getReference("banrecords").child(a).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    d.clear();
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                            Tinv af= new Tinv("Offence Number: ",dsp.getKey());
                            d.add(af);
                    }
                totaloffencecount.setText("Total Offences: "+String.valueOf(snapshot.getChildrenCount()));
                    Adapter.filterList(d);
                    Adapter.notifyDataSetChanged();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Tinv ad = new Tinv("Opps Error occured", "");
                d.add(ad);
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
        if (ssearchtofflimit.getText().toString().trim().equals("") || filteredList.isEmpty()) {
            searchinfo = false;
        } else {
            dc.clear();
            searchinfo = true;
            dc.addAll(filteredList);
        }
        Adapter.filterList(filteredList);
    }

    @Override
    public void onNoteClick(int position) {
        if (searchinfo.equals(Boolean.FALSE)) {
            String af = d.get(position).innumber;
            if (af.equals("")) {
                Toast.makeText(Offences.this, "Error happened", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Offences.this,  BanRecordstime.class);
                intent.putExtra("offn", af);
                intent.putExtra("vcnt",a);
                startActivity(intent);
            }
        } else {
            String af = dc.get(position).innumber;
            if (af.equals("")) {
                Toast.makeText(Offences.this, "Error happened", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Offences.this, BanRecordstime.class);
                intent.putExtra("offn", af);
                intent.putExtra("vcnt",a);
                startActivity(intent);
            }
        }
    }
}