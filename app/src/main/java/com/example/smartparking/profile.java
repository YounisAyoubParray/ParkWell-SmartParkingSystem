package com.example.smartparking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class profile extends Fragment {
    private FirebaseUser user;
    private String userID;
    private DatabaseReference reference;
    private TextView textView7;
    private TextView textView8;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_profile,container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
        final TextView textViewn = view.findViewById(R.id.textViewn);
        final TextView textViewe = view.findViewById(R.id.textViewe);
        final TextView textViewm = view.findViewById(R.id.textViewm);
        final TextView textViewa = view.findViewById(R.id.textViewa);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                String Email=user.getEmail();
                if(userProfile!= null)
                {
                    String FullName=userProfile.FullName;
                    String Phone=userProfile.Phone;
                    String Address=userProfile.Address;
                    String emal=userProfile.Email;
                    if(!emal.equals(Email)){
                        reference.child(userID).child("Email").setValue(Email);
                    }
                    textViewn.setText(FullName);
                    textViewe.setText(emal);
                    textViewm.setText(Phone);
                    textViewa.setText(Address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        textView7= view.findViewById(R.id.textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number =textViewm.getText().toString();
                Intent intent =new Intent(getActivity(),editmobilenumber.class);
                intent.putExtra("number",number);
                startActivity(intent);
            }
        });
        textView8= view.findViewById(R.id.textView8);
        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address=textViewa.getText().toString();
                Intent intent =new Intent(getActivity(),editaddress.class);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });
        return view;
    }
}