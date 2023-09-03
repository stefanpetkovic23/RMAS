package com.example.dealspotter.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dealspotter.Adapters.PlaceHelpAdapter;
import com.example.dealspotter.Models.Favorites;
import com.example.dealspotter.Models.Place;
import com.example.dealspotter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlaceHelpAdapter adapter;
    private ArrayList<Place> favoritePlaces = new ArrayList<>();

    private DatabaseReference favoritesRef;
    private DatabaseReference placesRef;

    private Map<String, Boolean> favoriteStatusMap = new HashMap<>();
    private Set<String> favoritePropertyKeys = new HashSet<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
        View view =  inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclefavorite);
        adapter = new PlaceHelpAdapter(getContext(), favoritePlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        favoritesRef = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Favorites")
                .child(userID); // Update with your user ID
        placesRef = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Places");

        loadFavoriteStatus();
        
        return view;
    }

    private void loadFavoriteStatus() {

        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoritePropertyKeys.clear();
                for(DataSnapshot propertySnapshot : snapshot.getChildren())
                {
                    Favorites favorites = propertySnapshot.getValue(Favorites.class);
                    String propertyKey = favorites.getPropertyKey();
                    favoritePropertyKeys.add(propertyKey);
                }
                loadPropertiesFromDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPropertiesFromDatabase() {
        placesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoritePlaces.clear();

                for(DataSnapshot propertySnapshot : snapshot.getChildren()){
                    Place place = propertySnapshot.getValue(Place.class);
                    String propertyKey = place.getTitle();

                    if (favoritePropertyKeys.contains(propertyKey)) {
                        favoritePlaces.add(place);
                    }
                        adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }






}