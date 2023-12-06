package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Address;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class editmobilenumber extends AppCompatActivity {
    private EditText mobilenumberchange;
    private ImageView cross;
    private ImageView tick;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference reference;

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
        setContentView(R.layout.activity_editmobilenumber);


        cross= findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editmobilenumber.this, loggedinn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mobilenumberchange= findViewById(R.id.mobilenumberchange);
        Intent intent = getIntent();
        String number= intent.getStringExtra("number");
        mobilenumberchange.setText(number);


        tick= findViewById(R.id.tick);
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newnumber=mobilenumberchange.getText().toString();
                if(newnumber.equals(number))
                {
                    mobilenumberchange.setError("Enter the new number");
                    mobilenumberchange.requestFocus();
                    return;
                }
                if(newnumber.isEmpty()){
                    mobilenumberchange.setError("Enter the phone number");
                    mobilenumberchange.requestFocus();
                    return;
                }
                if(newnumber.length()!=10) {
                    mobilenumberchange.setError("Enter the correct phone number");
                    mobilenumberchange.requestFocus();
                    return;
                }
                    update(newnumber);

            }
        });
    }

    private void update(String newnumber) {
        String phone= newnumber;

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
        reference.child(userID).child("Phone").setValue(phone).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(editmobilenumber.this,"Number Updated Successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(editmobilenumber.this, loggedinn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editmobilenumber.this,"Error! Try again later",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(editmobilenumber.this, loggedinn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}