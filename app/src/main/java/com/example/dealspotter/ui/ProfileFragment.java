package com.example.dealspotter.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dealspotter.Adapters.LeaderBoardAdapter;
import com.example.dealspotter.Login;
import com.example.dealspotter.Models.User;
import com.example.dealspotter.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private String firstname,lastname,email,username,phonenumber,imageURL,points;
    Integer userpoints;
    Long estatecount;
    ImageButton logout;
    TextView txtfirstname,txtlastname,txtusername,txtemail,txtphonenumber,txtpoints,txtestatecount, pointssystem, morgagecalculator;
    ImageView profileimage;
    StorageReference storageReference;

    Dialog mydialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtfirstname = view.findViewById(R.id.profilefirstname);
        txtlastname = view.findViewById(R.id.profilelastname);
        txtusername = view.findViewById(R.id.profileusername);
        txtemail = view.findViewById(R.id.profilemail);
        txtphonenumber = view.findViewById(R.id.profilephone);
        txtpoints = view.findViewById(R.id.profilepoints);
        txtestatecount = view.findViewById(R.id.profileaddedestates);
        profileimage = view.findViewById(R.id.profileimage);
        logout = view.findViewById(R.id.profilelogout);
        pointssystem = view.findViewById(R.id.systempoints);
        morgagecalculator = view.findViewById(R.id.morgagecalculator);

        pointssystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydialog = new Dialog(getContext());
                mydialog.setContentView(R.layout.pointsrankingdialog);
                mydialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                mydialog.setCancelable(true);
                mydialog.show();

            }
        });
        morgagecalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorgageCalculatorFragment newFragment = new MorgageCalculatorFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if(firebaseUser==null){
            Toast.makeText(ProfileFragment.this.getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
        }else {
            showUserProfile(firebaseUser);
        }

        return view;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {

        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users");

        DatabaseReference referencePlace = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Places");
        referencePlace.orderByChild("owner").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                estatecount=snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user!=null){
                    firstname = user.firstname;
                    lastname = user.lastname;
                    username = user.username;
                    email = firebaseUser.getEmail();
                    phonenumber = user.phonenumber;
                    userpoints = user.points;
                    imageURL=user.photoUri;

                    storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pic").child(username+".jpg");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            if(getActivity() == null)
                            {
                                return;
                            }
                            Glide.with(ProfileFragment.this)
                                    .load(uri)
                                    .error(R.drawable.ic_launcher_background)
                                    .circleCrop()
                                    .into(profileimage);
                        }
                    });


                    txtfirstname.setText(firstname);
                    txtlastname.setText(lastname);
                    txtusername.setText(username);
                    txtemail.setText(email);
                    txtphonenumber.setText(phonenumber);
                    txtpoints.setText(userpoints.toString()+"pts");
                    txtestatecount.setText(estatecount.toString());



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileFragment.this.getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


    }
}