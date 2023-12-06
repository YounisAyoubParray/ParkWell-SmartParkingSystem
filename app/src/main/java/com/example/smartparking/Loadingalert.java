package com.example.smartparking;



import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class Loadingalert {
    private final Activity activity;
    private AlertDialog dialog;
    Loadingalert(Activity myActivity){
        activity = myActivity;
    }

    void startAlertDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog,null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    void closeAlertDialog() {
        dialog.dismiss();
    }
}

