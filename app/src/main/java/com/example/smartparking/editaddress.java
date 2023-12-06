package com.example.smartparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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

public class editaddress extends AppCompatActivity {
    private EditText addresschange;
    private ImageView cross2;
    private ImageView tick2;
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
        setContentView(R.layout.activity_editaddress);
        cross2= findViewById(R.id.cross2);
        cross2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editaddress.this, loggedinn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        addresschange= findViewById(R.id.addresschange);
        Intent intent = getIntent();
        String address= intent.getStringExtra("address");
        addresschange.setText(address);


        tick2= findViewById(R.id.tick2);
        tick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newaddress=addresschange.getText().toString();
                if(newaddress.equals(address))
                {
                    addresschange.setError("Enter the new Address");
                    addresschange.requestFocus();
                    return;
                }
                if(newaddress.isEmpty()){
                    addresschange.setError("Enter the Address");
                    addresschange.requestFocus();
                    return;
                }

                update(newaddress);

            }
        });
    }
    private void update(String newaddress) {

        String addresss= newaddress;


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
        reference.child(userID).child("Address").setValue(addresss).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(editaddress.this,"Address Updated Successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(editaddress.this, loggedinn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(editaddress.this,"Error! Try again later",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(editaddress.this, loggedinn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}