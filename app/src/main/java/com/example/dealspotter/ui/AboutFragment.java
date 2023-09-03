package com.example.dealspotter.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dealspotter.Adapters.PlaceHelpAdapter;
import com.example.dealspotter.Models.Place;
import com.example.dealspotter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    RecyclerView placerecycle;

    PlaceHelpAdapter adapter1;
    ArrayList<Place> places;

    SearchView searchView;

    FloatingActionButton filterBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);

        placerecycle = view.findViewById(R.id.placeRecycler);
        searchView = view.findViewById(R.id.searchView);
        filterBtn = view.findViewById(R.id.fabFilterPrice);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        placerecycle.setLayoutManager(layoutManager);


        DatabaseReference reference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Places");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                places = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Place place = dataSnapshot.getValue(Place.class);
                    places.add(place);
                }
                adapter1 = new PlaceHelpAdapter(requireContext(),places);
                placerecycle.setAdapter(adapter1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriceFilterDialog();
            }
        });

        return view;
    }

    private void showPriceFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.filterdialog, null);
        EditText minPriceEditText = dialogView.findViewById(R.id.editTextMinPrice);
        EditText maxPriceEditText = dialogView.findViewById(R.id.editTextMaxPrice);

        builder.setView(dialogView)
                .setTitle("Price Filter")
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String minPriceText = minPriceEditText.getText().toString();
                        String maxPriceText = maxPriceEditText.getText().toString();
                        if(minPriceText.isEmpty() && maxPriceText.isEmpty()){
                            ArrayList<Place> filteredList = places;
                            PlaceHelpAdapter adapter = new PlaceHelpAdapter(requireContext(), filteredList);
                            placerecycle.setAdapter(adapter);


                        }else
                        {
                            int minPrice = Integer.parseInt(minPriceEditText.getText().toString());
                            int maxPrice = Integer.parseInt(maxPriceEditText.getText().toString());
                            filterByPriceRange(minPrice, maxPrice);
                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void filterByPriceRange(int minPrice, int maxPrice) {
        ArrayList<Place> filteredList = new ArrayList<>();
        for (Place place : places) {
            int placePrice = place.getPrice();
            if (placePrice >= minPrice && placePrice <= maxPrice) {
                filteredList.add(place);
            }
        }

        PlaceHelpAdapter adapter = new PlaceHelpAdapter(requireContext(), filteredList);
        placerecycle.setAdapter(adapter);
    }


    private void search(String str) {

        ArrayList<Place> list = new ArrayList<>();
        for(Place obj : places){
            if(obj.getTitle().toLowerCase().contains(str.toLowerCase()) || obj.getAdress().toLowerCase().contains(str.toLowerCase()
            ))
            {
               list.add(obj);
            }
             PlaceHelpAdapter adapter = new PlaceHelpAdapter(requireContext(),list);
            placerecycle.setAdapter(adapter);
        }

    }
}