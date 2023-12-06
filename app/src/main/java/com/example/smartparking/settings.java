package com.example.smartparking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;



public class settings extends Fragment {

    View view;
    private AlertDialog.Builder builder;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_settings,container, false);
        builder = new AlertDialog.Builder(getActivity());
        TextView chgemail = view.findViewById(R.id.chgemail);
        chgemail.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChangeEmail.class);
            startActivity(intent);
        });


        TextView chgepass = view.findViewById(R.id.chgepass);
        chgepass.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChangePassword.class);
            startActivity(intent);
        });

        TextView delaccount = view.findViewById(R.id.delaccount);
        delaccount.setOnClickListener(view -> {
            builder.setTitle("Park Well");
            builder.setIcon(R.drawable.parkinglogo);
            builder.setMessage("Are you sure you want to Delete your account?");
            builder.setCancelable(false);
            builder.setNegativeButton(Html.fromHtml("<font color='#8f0404'> Yes </font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getActivity(), Delete.class);
                    startActivity(intent);
                }
            });
            builder.setPositiveButton(Html.fromHtml("<font color='#014077'> No </font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();



        });

        TextView logbutout = view.findViewById(R.id.logbutout);
        logbutout.setOnClickListener(view -> {


            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        });

        return view;
    }

}

