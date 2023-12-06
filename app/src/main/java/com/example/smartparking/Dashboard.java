package com.example.smartparking;







import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment implements CustomDialogListener {
    float count;
    String amigo;
    private CustomDialogListener dialogListener;

    int nc=0;
    String aa="",bb="",cc="",dd="";
    Boolean a,b,c,d;
    int ct=0;
    private ConstraintLayout constraintLayout;
    private int screenWidth;
    private int textWidth;
    private TextView adminbookdetail,adminbookdetail125, textView75, textView68,textView69,textView70,textView73,textView71,adminbookdetaillsa,movingTextView,a5656;
    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Assign the Fragment instance to the dialogListener variable
        dialogListener = this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        String date = holddate();
        String time= holdtime();


        FirebaseDatabase.getInstance().getReference("booking").child("Srinagar").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    count=available(snapshot);

                }else{
                    count=0;

                }
                textView75.setText("₹"+String.valueOf(count));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        FirebaseDatabase.getInstance().getReference("parkings").child("Srinagar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Slotssrinagar data= snapshot.getValue(Slotssrinagar.class);
                if(data!=null){
                     nc=0;
                     a=data.slot1;
                     b=data.slot2;
                     c=data.slot3;
                    if(a==Boolean.FALSE){
                        nc=nc+1;
                    }
                    if(b==Boolean.FALSE){
                        nc=nc+1;
                    }
                    if(c==Boolean.FALSE){
                        nc=nc+1;
                    }
                    if(d==Boolean.FALSE){
                        nc=nc+1;
                    }
                    textView68.setText(String.valueOf(nc));
                    illegalcheck();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("availability").child("Srinagar").child("slot1").child(date).child(time).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ac = snapshot.getValue(String.class);
                    if (ac != null) {
                        if (!ac.contains("booked")) {
                            aa = "notb";
                            illegalcheck();
                        }
                    }

                } else if (!snapshot.exists()) {
                    aa = "notb";
                    illegalcheck();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

                FirebaseDatabase.getInstance().getReference("availability").child("Srinagar").child("slot3").child(date).child(time).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String ac=snapshot.getValue(String.class);
                            if(ac!=null){
                                if(!ac.contains("booked"))
                                {
                                    cc="notb";
                                    illegalcheck();
                                }
                                }
                        }else if(!snapshot.exists()){
                            cc="notb";
                            illegalcheck();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                FirebaseDatabase.getInstance().getReference("availability").child("Srinagar").child("slot2").child(date).child(time).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String ac=snapshot.getValue(String.class);
                        if(ac!=null){
                            if(!ac.contains("booked")){
                                bb="notb";
                                illegalcheck();
                            }
                        }
                    }else if(!snapshot.exists()){
                        bb="notb";
                        illegalcheck();
                    }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










        FirebaseDatabase.getInstance().getReference("booking").child("Srinagar").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            long a=snapshot.getChildrenCount();
            textView69.setText(String.valueOf(a));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void illegalcheck() {
        ct=0;
        amigo="";
        if(a==Boolean.FALSE){
            if(aa.equals("notb")){
                ct=ct+1;
                amigo=amigo+"_1";
            }
        }
        if(b==Boolean.FALSE){
            if(bb.equals("notb")){
                ct=ct+1;
                amigo=amigo+"_2";
            }
        }
        if(c==Boolean.FALSE){
            if(cc.equals("notb")){
                ct=ct+1;
                amigo=amigo+"_3";
            }
        }

        textView73.setText(String.valueOf(ct));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        adminbookdetail = view.findViewById(R.id.adminbookdetail);
        textView70 = view.findViewById(R.id.textView70);
        textView69 = view.findViewById(R.id.textView69);
        textView68 = view.findViewById(R.id.textView68);
        textView73 = view.findViewById(R.id.textView73);
        constraintLayout=view.findViewById(R.id.constraintLayout10);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            CustomDialog customDialog=new CustomDialog(getActivity());
            customDialog.setDialogListener(dialogListener);
            customDialog.show();
            }
        });



        movingTextView = view.findViewById(R.id.carparkingprice);
        calprice();


        screenWidth = getResources().getDisplayMetrics().widthPixels;


        movingTextView.measure(0, 0);
        textWidth = movingTextView.getMeasuredWidth();


        movingTextView.setTranslationX(screenWidth);


        startTextAnimation();
        a5656=view.findViewById(R.id.a5656);
        a5656.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Last.class));
            }
        });

        adminbookdetail125=view.findViewById(R.id.adminbookdetail125);
        adminbookdetail125.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LastRide.class));
            }
        });

        adminbookdetaillsa=view.findViewById(R.id.adminbookdetaillsa);
        textView73.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letsgo();
            }
        });
        textView71=view.findViewById(R.id.textView71);
        textView71.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                letsgo();
            }
        });
        textView75 = view.findViewById(R.id.textView75);
        datenow();



        adminbookdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Admbookdetail.class);
                startActivity(intent);
            }
        });
        adminbookdetaillsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),Waste.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void calprice() {
        FirebaseDatabase.getInstance().getReference("cost").child("Srinagar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long  a =snapshot.getValue(Long.class);
                if(a!=null){
                    movingTextView.setText("Current Parking Price: ₹"+String.valueOf(a));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void startTextAnimation() {
            ValueAnimator animator = ValueAnimator.ofFloat(-textWidth,screenWidth);
            animator.setDuration(5000);
            animator.setRepeatCount(ValueAnimator.INFINITE);



            animator.setInterpolator(new LinearInterpolator());


            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float translationX = (float) animation.getAnimatedValue();
                    movingTextView.setTranslationX(translationX);
                }
            });

            animator.start();
    }

    private void letsgo() {
            Intent intent = new Intent(getActivity(), Illegalcar.class);
            intent.putExtra("museebat",textView73.getText().toString());
            intent.putExtra("data",amigo);
            startActivity(intent);
    }

    private String holddate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        return String.valueOf(day +""+ (month+1) +""+ year);
    }
    private String holdtime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String aphour;
        if(hour>12){
            int nt=hour-12;
            aphour="pm"+nt;

        }else if(hour==0){
            int nt=12;
            aphour="am"+nt;

        }else if(hour==12){
            aphour="pm"+hour;
        }
        else{
            aphour="am"+hour;
        }
        return aphour;
    }

    private float available(DataSnapshot snapshot) {
        float total=0;
        for (DataSnapshot dsnapshot : snapshot.getChildren()){
            String cost=dsnapshot.child("Payment").getValue(String.class);
            String[] a=cost.split(" ");
            Log.d("Array b",a[1]);
            total=Float.valueOf(a[1].trim())+total;
        }
        return total;
    }


    private void datenow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String currentDateandTime = sdf.format(new Date());
        textView70.setText(currentDateandTime);
    }

    @Override
    public void onDialogDismissed() {
        calprice();
    }
}