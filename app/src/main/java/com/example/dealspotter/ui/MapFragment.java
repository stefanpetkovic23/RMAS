package com.example.dealspotter.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dealspotter.Adapters.PlaceHelpAdapter;
import com.example.dealspotter.Models.Place;
import com.example.dealspotter.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap myMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Context context;
    ImageView addplace,filter,allplaces;
    List<Place> places = new ArrayList<>();

    DatabaseReference databaseReference;
    private StorageReference storageReference;
    SearchView searchmap;

    private ArrayList<Marker> allMarkers = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        addplace = view.findViewById(R.id.addplaceonmap);
        filter = view.findViewById(R.id.filtermap);
        allplaces = view.findViewById(R.id.allplacesmap);
        searchmap = view.findViewById(R.id.searchViewmap);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLocation();

        addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putDouble("longitude",currentlocation.getLongitude());
                bundle.putDouble("latitude",currentlocation.getLatitude());
                if(currentlocation.getLongitude()!=0 && currentlocation.getLatitude()!=0){
                    AddPlaceFragment placeFragment = new AddPlaceFragment();
                    placeFragment.setArguments(bundle);
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container,placeFragment).addToBackStack(null);
                    transaction.commit();
                }
                else
                    Toast.makeText(getContext(),"Turn on location",Toast.LENGTH_LONG).show();

                //databaseReference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Places");

            }
        });

        searchmap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRadiusDialog();
               // int radiusInMeters = 3000;
               // showPlacesWithinRadius(radiusInMeters);
            }
        });

        return view;
    }





    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(
                MapFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(
                        this.getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentlocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapFragment.this);
                }
            }
        });

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng currentlatLng = new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        myMap = googleMap;
        BitmapDescriptor customIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);



        if (ActivityCompat.checkSelfPermission(
                MapFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                MapFragment.this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        myMap.setMyLocationEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(false);
        myMap.getUiSettings().setZoomControlsEnabled(true);
          myMap.animateCamera(CameraUpdateFactory.newLatLng(currentlatLng));
          myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatLng,15));
            myMap.addMarker(new MarkerOptions()
                    .position(currentlatLng)
                    .title("My location")
                    .icon(customIcon));
        }

        allplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showallplaces();
            }
        });




    }

    private void showallplaces()
    {

        databaseReference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Places");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                myMap.clear();
                allMarkers.clear();
                for(DataSnapshot snapshot:datasnapshot.getChildren()){
                    double latitude = snapshot.child("latitude").getValue(double.class);
                    double longitude = snapshot.child("longitude").getValue(double.class);
                    String title = snapshot.child("title").getValue(String.class);
                    String adress = snapshot.child("adress").getValue(String.class);
                    LatLng latLng = new LatLng(latitude, longitude);
                    BitmapDescriptor customIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(title+", "+adress)
                            .icon(customIcon);

                    Marker marker = myMap.addMarker(markerOptions);
                    allMarkers.add(marker);
                   // myMap.addMarker(markerOptions);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void search(String str) {
        for (Marker marker : allMarkers) {
            String title = marker.getTitle();
            if (title != null && title.toLowerCase().contains(str.toLowerCase())) {
                marker.setVisible(true);
                LatLng markerLatLng = marker.getPosition();
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 10));
            } else {
                marker.setVisible(false);
            }
        }
    }

    private void showPlacesWithinRadius(int radiusInMeters) {
        myMap.clear();
        allMarkers.clear();

        LatLng userLocation = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());

        CircleOptions circleOptions = new CircleOptions()
                .center(userLocation)
                .radius(radiusInMeters)
                .strokeColor(Color.BLACK)
                .fillColor(Color.parseColor("#500084d3")); // Change color and transparency as needed
        myMap.addCircle(circleOptions);
        databaseReference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Places");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotradius : snapshot.getChildren()) {
                    double latitude = snapshotradius.child("latitude").getValue(double.class);
                    double longitude = snapshotradius.child("longitude").getValue(double.class);
                    String title = snapshotradius.child("title").getValue(String.class);
                    String adress = snapshotradius.child("adress").getValue(String.class);
                    LatLng placeLatLng = new LatLng(latitude, longitude);

                    float[] distanceResults = new float[1];
                    Location.distanceBetween(userLocation.latitude, userLocation.longitude, placeLatLng.latitude, placeLatLng.longitude, distanceResults);

                    if (distanceResults[0] <= radiusInMeters) {
                        BitmapDescriptor customIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(placeLatLng)
                                .title(title+", "+adress)
                                .icon(customIcon);

                        Marker marker = myMap.addMarker(markerOptions);
                        allMarkers.add(marker);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openRadiusDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.radiusfilter_dialog);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        dialog.getWindow().setAttributes(layoutParams);

        SeekBar seekBarRadius = dialog.findViewById(R.id.seekBarRadius);
        final TextView textViewRadiusValue = dialog.findViewById(R.id.textViewRadiusValue);
        seekBarRadius.setProgress(100);
        Button buttonApply = dialog.findViewById(R.id.buttonApply);

        seekBarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewRadiusValue.setText(progress + " meters");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadius = seekBarRadius.getProgress();

                dialog.dismiss();
                showPlacesWithinRadius(selectedRadius);
            }
        });

        dialog.show();
    }

}