package com.example.smartparking;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class parkinglots extends AppCompatActivity implements Adapter1.OnNoteListener {
    private ArrayList<parkingname1> parkingname1;
    private ArrayList<parkingname1> shit;
    private ArrayList<String> check;
    private Button parkdistwicho;
    Boolean searchinfo=false;
    private RecyclerView mRecyclerView;
    private Adapter1 mAdapter;
    String count;
    Long agg;
    private EditText editsearch;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private static final int LOCATION_SETTINGS_REQUEST_CODE = 1002;


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
        setContentView(R.layout.activity_parkinglots);
        check=new ArrayList<>();
        shit=new ArrayList<>();
        parkdistwicho=findViewById(R.id.parkdistwicho);
        parkdistwicho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(parkinglots.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest();
                    getLastKnownLocation();
                } else {
                    ActivityCompat.requestPermissions(parkinglots.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        parkingname1= new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("cost").child("Srinagar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agg=snapshot.getValue(Long.class);
                buildRecyclerView();
                getcount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        editsearch= findViewById(R.id.editsearch);
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }



    private void filter(String text) {
        ArrayList<parkingname1> filteredList =new ArrayList<>();

        for(parkingname1 item : parkingname1){
            if(item.getParkinglotname1().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }


        if(editsearch.getText().toString().trim().equals("")||filteredList.isEmpty()){
            searchinfo=false;
        }else{
            shit.clear();
            searchinfo=true;
            shit.addAll(filteredList);
        }
        mAdapter.filterList(filteredList);
    }

        private void buildRecyclerView() {
            mRecyclerView=findViewById(R.id.recycler1231);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mAdapter = new Adapter1(parkingname1,this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);
    }






    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
    }




































    private void setparkinglist()
    {   parkingname1.clear();
        parkingname1.add(new parkingname1("Srinagar",agg+"Rs per hour","Na",count));
        parkingname1.add(new parkingname1("Parking Area 2","60Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 3","30Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 4","40Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("IUST","10Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 6","30Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 7","40Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 8","50Rs per hour","Na","Na"));
        mAdapter.notifyDataSetChanged();
    }

    private void getcount() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int mn=month+1;
        String datee = day+""+mn+year;
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
        FirebaseDatabase.getInstance().getReference("parkings").child("Srinagar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                check.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Boolean childValue = childSnapshot.getValue(Boolean.class);
                    if (childValue.equals(Boolean.TRUE)) {
                            check.add(childSnapshot.getKey());
                    }
                    }
                    if(!check.isEmpty()){
                        FirebaseDatabase.getInstance().getReference("Vehiclenumber").child("Srinagar").child(datee).child(aphour).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    int c=0;
                                    String ac = snapshot.getValue(String.class);
                                    if(ac!=null){
                                        for(int i=0;i<check.size();i++){
                                            if(ac.contains(check.get(i))){
                                                c++;
                                            }
                                        }
                                        count=String.valueOf(check.size()-c);
                                    }else{
                                        count =String.valueOf(check.size());
                                    }
                                }else{
                                    count =String.valueOf(check.size());
                                }
                                setparkinglist();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                count = "Na";
                                setparkinglist();
                            }
                        });





                    }else{
                        count=String.valueOf(0);
                        setparkinglist();
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                count ="Na";
                setparkinglist();
            }
        });




    }



    @Override
    public void onNoteClick(int position) {


        if (searchinfo.equals(Boolean.FALSE)) {
            String a = parkingname1.get(position).parkinglotname1;
            if (a.equals("Srinagar")) {
                Intent intent = new Intent(this, srinagar.class);
                intent.putExtra("parkingname","Srinagar");
                startActivity(intent);
            } else {
                Toast.makeText(this, "Dummy Slot", Toast.LENGTH_LONG).show();
            }
        } else {
            String a = shit.get(position).parkinglotname1;
            if (a.equals("Srinagar")) {
                Intent intent = new Intent(this, srinagar.class);
                intent.putExtra("parkingname","Srinagar");
                startActivity(intent);
            } else {
                Toast.makeText(this, "Dummy Slot", Toast.LENGTH_LONG).show();
            }
        }
    }













    private void setparkinglist(String distanceText) {
        parkingname1.clear();
        parkingname1.add(new parkingname1("Srinagar",agg+"Rs per hour",distanceText,count));
        parkingname1.add(new parkingname1("Parking Area 2","60Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 3","30Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 4","40Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("IUST","10Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 6","30Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 7","40Rs per hour","Na","Na"));
        parkingname1.add(new parkingname1("Parking Area 8","50Rs per hour","Na","Na"));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            getLastKnownLocation();
        });

        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(parkinglots.this, LOCATION_SETTINGS_REQUEST_CODE);
                } catch (IntentSender.SendIntentException sendEx) {

                }
            }
        });
    }

    private void getLastKnownLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());


                                float[] results = new float[1];

                                LatLng destinationLatLng = new LatLng(34.07294, 74.81288);
                                Location.distanceBetween(
                                        currentLatLng.latitude, currentLatLng.longitude,
                                        destinationLatLng.latitude, destinationLatLng.longitude,
                                        results);

                                double distanceInMeters = results[0];
                                double distanceInKilometers = distanceInMeters / 1000.0;
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                String dts=decimalFormat.format(distanceInKilometers) +" Kms";

                                setparkinglist(dts);
                            } else {
                                LocationRequest locationRequest = LocationRequest.create()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setNumUpdates(1);

                                LocationCallback locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        Location currentLocation = locationResult.getLastLocation();
                                        if (currentLocation != null) {
                                            calculateDistanceAndSetParkingList(currentLocation);
                                        } else {
                                            Toast.makeText(parkinglots.this, "Unable to get current location.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                };

                                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                } else {
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            LOCATION_PERMISSION_REQUEST_CODE);
                                }
                            }
                        });
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        });

        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, LOCATION_SETTINGS_REQUEST_CODE);
                } catch (IntentSender.SendIntentException sendEx) {

                }
            } else {
                Toast.makeText(parkinglots.this, "Location settings are not satisfied", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void calculateDistanceAndSetParkingList(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        float[] results = new float[1];

        LatLng destinationLatLng = new LatLng(34.07294, 74.81288);
        Location.distanceBetween(
                currentLatLng.latitude, currentLatLng.longitude,
                destinationLatLng.latitude, destinationLatLng.longitude,
                results);

        double distanceInMeters = results[0];
        double distanceInKilometers = distanceInMeters / 1000.0;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String dts = decimalFormat.format(distanceInKilometers) + " Kms";

        setparkinglist(dts);
    }
    }







