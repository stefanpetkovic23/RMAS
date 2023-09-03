package com.example.dealspotter.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dealspotter.Models.Place;
import com.example.dealspotter.R;
import com.example.dealspotter.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPlaceFragment extends Fragment {

    EditText edtadress,edtrooms,edtbathrooms,edtgarage,edtsurface,edttitle,edtprice,edtdescription;
    ImageView imageofplace;
    Integer NofRooms,NofBathrooms,NofGarages,SurfaceofBuilding,PriceofBuilding;



    LinearLayout linearLayout;

    String titlecheck;

    public Uri imageUri;
    public String myUri="";

    DatabaseReference databaseReference;
    private StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPlaceFragment newInstance(String param1, String param2) {
        AddPlaceFragment fragment = new AddPlaceFragment();
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

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode== Activity.RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            myUri=imageUri.toString();
            imageofplace.setImageURI(imageUri);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);


       // Toast.makeText(getContext(),longitude.toString()+", "+ latitude.toString(),Toast.LENGTH_LONG).show();

        edtadress = view.findViewById(R.id.placeaddress);
        edtrooms = view.findViewById(R.id.placerooms);
        edtbathrooms = view.findViewById(R.id.placebathrooms);
        edtgarage = view.findViewById(R.id.placegarage);
        edtsurface = view.findViewById(R.id.placesurface);
        edtprice = view.findViewById(R.id.placeprice);
        edttitle = view.findViewById(R.id.placetitle);
        edtdescription = view.findViewById(R.id.placedescription);



        imageofplace = view.findViewById(R.id.placeimage);
        imageofplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        Button addplace = view.findViewById(R.id.buttonaddplace);
        addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Adress = edtadress.getText().toString();
                String Rooms = edtrooms.getText().toString();
                String Bathrooms = edtbathrooms.getText().toString();
                String Garage = edtgarage.getText().toString();
                String Surface = edtsurface.getText().toString();
                String Price = edtprice.getText().toString();
                String Title = edttitle.getText().toString();
                String Description = edtdescription.getText().toString();
                DatabaseReference referencecheck = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Places");
                referencecheck.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            titlecheck = dataSnapshot.child("title").getValue(String.class);
                            //Toast.makeText(getContext(),titlecheck,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                if(TextUtils.isEmpty(Adress)){
                    //Toast.makeText(getContext(),"Please enter adress!",Toast.LENGTH_LONG).show();
                    edtadress.setError("Adress is required!");
                    edtadress.requestFocus();
                }else if(TextUtils.isEmpty(Rooms)){
                    //Toast.makeText(getContext(),"Please enter number of rooms!",Toast.LENGTH_LONG).show();
                    edtrooms.setError("Number of rooms is required!");
                    edtrooms.requestFocus();
                }else if(TextUtils.isEmpty(Bathrooms)){
                   // Toast.makeText(getContext(),"Please enter number of bathrooms!",Toast.LENGTH_LONG).show();
                    edtbathrooms.setError("Number of bathrooms is required!");
                    edtbathrooms.requestFocus();
                }else if(TextUtils.isEmpty(Garage)){
                    //Toast.makeText(getContext(),"Please enter number of garages!",Toast.LENGTH_LONG).show();
                    edtgarage.setError("Number of garages is required!");
                    edtgarage.requestFocus();
                }else if(TextUtils.isEmpty(Surface)){
                   // Toast.makeText(getContext(),"Please enter surface of property!",Toast.LENGTH_LONG).show();
                    edtsurface.setError("Surface of property is required!");
                    edtsurface.requestFocus();
                }else if(TextUtils.isEmpty(Price)){
                   // Toast.makeText(getContext(),"Please enter price of property!",Toast.LENGTH_LONG).show();
                    edtprice.setError("Price of property is required!");
                    edtprice.requestFocus();
                }else if(TextUtils.isEmpty(Description)){
                   // Toast.makeText(getContext(),"Please enter property description!",Toast.LENGTH_LONG).show();
                    edtdescription.setError("Description is required!");
                    edtdescription.requestFocus();
                }else if(TextUtils.isEmpty(Title)){
                    // Toast.makeText(getContext(),"Please enter title of property!",Toast.LENGTH_LONG).show();
                    edttitle.setError("Title of is required!");
                    edttitle.requestFocus();
                }
                else{
                    checkTitleUniqueness(Title);

                }


            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users")
                .child(currentUserId);
        referenceProfile.child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Integer currentpoints = snapshot.getValue(Integer.class);
                    if (currentpoints != null){
                        int updatedPoints = currentpoints + 10;
                        referenceProfile.child("points").setValue(updatedPoints).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void checkTitleUniqueness(final String title) {
        DatabaseReference titleReference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Places");
        Query titleQuery = titleReference.orderByChild("title").equalTo(title);

        titleQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Title already exists, show an error message
                    edttitle.setError("Title is already in use!");
                    edttitle.requestFocus();
                } else {
                        addPlaceToDatabase();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }

    private void addPlaceToDatabase() {
        String Adress = edtadress.getText().toString();
        String Rooms = edtrooms.getText().toString();
        String Bathrooms = edtbathrooms.getText().toString();
        String Garage = edtgarage.getText().toString();
        String Surface = edtsurface.getText().toString();
        String Price = edtprice.getText().toString();
        String Title = edttitle.getText().toString();
        String Description = edtdescription.getText().toString();

        Bundle bundle = this.getArguments();
        Double longitude = bundle.getDouble("longitude");
        Double latitude = bundle.getDouble("latitude");

        Integer NofRooms = Integer.parseInt(Rooms);
        Integer NofBathrooms = Integer.parseInt(Bathrooms);
        Integer NofGarages = Integer.parseInt(Garage);
        Integer SurfaceofBuilding = Integer.parseInt(Surface);
        Integer PriceofBuilding = Integer.parseInt(Price);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Places");
        String key = reference.push().getKey();
        if (imageUri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Place Pic").child(  Title+ ".jpg");
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getContext(), "Picture selected!", Toast.LENGTH_LONG).show();
                    myUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                }
            });
        }
        Place place =new Place(Adress,userid,Description,Title,myUri,latitude,longitude,NofRooms,NofBathrooms,PriceofBuilding,NofGarages,SurfaceofBuilding);
        reference.child(key).setValue(place).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"You are succesfuly added place!",Toast.LENGTH_LONG).show();
                    getFragmentManager().popBackStack();
                }else
                    Toast.makeText(getContext(),"Adding place failed!",Toast.LENGTH_LONG).show();
            }
        });
    }

}