package com.example.smartparking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Offlimits#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offlimits extends Fragment implements AnewRecycler.OnNoteListener {
    private RecyclerView recyclerView;
    ArrayList<Tinv> d = new ArrayList<Tinv>();
    ArrayList<Tinv> dc = new ArrayList<Tinv>();
    Boolean searchinfo = false;
    private AnewRecycler Adapter;
    private EditText ssearchofflimit;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Offlimits() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Offlimits.
     */
    // TODO: Rename and change types and number of parameters
    public static Offlimits newInstance(String param1, String param2) {
        Offlimits fragment = new Offlimits();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void newsearch(String toString) {
        ArrayList<Tinv> filteredList = new ArrayList<>();
        for (Tinv item : d) {
            if (item.getinnumber().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (ssearchofflimit.getText().toString().trim().equals("") || filteredList.isEmpty()) {
            searchinfo = false;
        } else {
            dc.clear();
            searchinfo = true;
            dc.addAll(filteredList);
        }
        Adapter.filterList(filteredList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offlimits, container, false);
        data();
        ssearchofflimit=view.findViewById(R.id.ssearchofflimit);
        ssearchofflimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newsearch(ssearchofflimit.getText().toString());
            }
        });
        recyclerView = view.findViewById(R.id.recycleofflimits);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        Adapter = new AnewRecycler(d, this::onNoteClick);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(Adapter);


        return view;
    }

    private void data() {
        FirebaseDatabase.getInstance().getReference("Offlimits").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                d.clear();
                if(snapshot.exists()){
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Boolean childValue = childSnapshot.getValue(Boolean.class);
                    if (childValue != null ) {
                        String childKey = childSnapshot.getKey();
                        Tinv ad = new Tinv("Vehicle Number: ", childKey);
                        d.add(ad);
                    }

                }
            }else{
                        Tinv ad = new Tinv("No vehicle is off limits", "");
                        d.add(ad);
                }
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

    @Override
    public void onNoteClick(int position) {
        if (searchinfo.equals(Boolean.FALSE)) {
            String a = d.get(position).innumber;
            if (a.equals("")) {
                Toast.makeText(getActivity(), "No vehcile found", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), Offences.class);
                intent.putExtra("vcn", a);
                startActivity(intent);
            }
        } else {
            String a = dc.get(position).innumber;
            if (a.equals("")) {
                Toast.makeText(getActivity(), "No vehcile found", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), Offences.class);
                intent.putExtra("vcn", a);
                startActivity(intent);
            }
        }


    }
}