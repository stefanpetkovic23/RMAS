package com.example.dealspotter.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dealspotter.Models.Comment;
import com.example.dealspotter.Models.Place;
import com.example.dealspotter.Models.Rating;
import com.example.dealspotter.Models.User;
import com.example.dealspotter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    ImageView homeprofileimg, commentimage, estateimage, ratestar, addcomment, ownernumber;
    String ownerID;
    String firstname,lastname,username,title,adress,description,photoURI;
    Integer price,rooms,bathrooms,garage,surface;
    TextView txtusername,txttitle,txtadress,txtrooms,txtbathrooms,txtgarage,txtsurface,txtdescription,txtprice,ratingscore,allcomments;
    StorageReference storageReference;
    EditText txtcomment;
    private boolean hasUserRated = false;

    private static final DecimalFormat dfZero = new DecimalFormat("0.00");

    RatingBar rating;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailFragment() {
        // Required empty public constructor
    }
    public DetailFragment(String title,String adress,Integer rooms, Integer bathrooms,Integer garage,Integer surface,String description,String photoURI,Integer price, String owner){
        this.title = title;
        this.adress = adress;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.garage = garage;
        this.surface = surface;
        this.description = description;
        this.photoURI = photoURI;
        this.price = price;
        this.ownerID = owner;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        txtusername = view.findViewById(R.id.detailownername);
        homeprofileimg = view.findViewById(R.id.detailownerpic);
        commentimage = view.findViewById(R.id.detailownerpicforcomment);
        txttitle = view.findViewById(R.id.detailTitle);
        txtadress = view.findViewById(R.id.detailadress);
        txtrooms = view.findViewById(R.id.detailrooms);
        txtbathrooms = view.findViewById(R.id.detailbathroom);
        txtgarage = view.findViewById(R.id.detailgarage);
        txtsurface = view.findViewById(R.id.detailsurface);
        txtdescription = view.findViewById(R.id.description);
        txtprice = view.findViewById(R.id.detailprice);
        estateimage = view.findViewById(R.id.placedetailimage);
        rating = view.findViewById(R.id.ratingbar);
        ratestar = view.findViewById(R.id.detailrate);
        txtcomment = view.findViewById(R.id.editTextcomment);
        addcomment = view.findViewById(R.id.addcommentimg);
        ratingscore = view.findViewById(R.id.ratingscore);
        allcomments = view.findViewById(R.id.allcomments);
        ownernumber = view.findViewById(R.id.ownercall);


        txttitle.setText(title);
        txtadress.setText(adress);
        txtrooms.setText(rooms.toString());
        txtbathrooms.setText(bathrooms.toString());
        txtgarage.setText(garage.toString());
        txtsurface.setText(surface.toString()+"m2");
        txtdescription.setText(description);
        txtprice.setText(price.toString()+"€");
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        reference.child("Place Pic").child(title+".jpg")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext())
                                .load(uri)
                                .into(estateimage);
                    }
                });


                    DatabaseReference ownerprofilereference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users");
                    ownerprofilereference.child(ownerID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                User user = snapshot.getValue(User.class);
                                firstname = user.firstname;
                                lastname = user.lastname;
                                username = user.username;
                                txtusername.setText(firstname + " " + lastname);
                                String phone = user.getPhonenumber();
                                ownernumber.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        PhoneDialog rateDialog = new PhoneDialog(getContext(),phone);
                                        rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        rateDialog.setCancelable(false);
                                        rateDialog.show();
                                    }
                                });
                                StorageReference reference = FirebaseStorage.getInstance().getReference();
                                reference.child("Profile Pic").child(user.username+".jpg").getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(DetailFragment.this)
                                                        .load(uri)
                                                        .error(R.drawable.ic_launcher_background)
                                                        .circleCrop()
                                                        .into(homeprofileimg);
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        ratestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference referencerating1 = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ratings");
                referencerating1.orderByChild("propertyId").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userid = firebaseUser.getUid();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Rating rating = dataSnapshot.getValue(Rating.class);
                            String ratinguserid = rating.getUserID();
                            if(ratinguserid.equals(userid)) {
                                hasUserRated = true;
                                ratestar.setClickable(false);
                                ratestar.setFocusable(false);
                                break;
                            }
                        }
                        if (!hasUserRated){
                            RateDialog rateDialog = new RateDialog(getContext(),title);
                            rateDialog.setCancelable(false);
                            rateDialog.show();
                    }else {
                            Toast.makeText(getContext(), "You have already rated.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }
        });


        allcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDialog commentDialog = new CommentDialog(getContext(),title);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(commentDialog.getWindow().getAttributes());
                layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 1.2);
                commentDialog.getWindow().setAttributes(layoutParams);
               // commentDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                commentDialog.setCancelable(true);
                commentDialog.show();

            }
        });

        DatabaseReference referencerating = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ratings");
        referencerating.orderByChild("propertyId").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalrankings = 0;
                float totalrankingvalue = 0;

                for(DataSnapshot ratingsnapshot : snapshot.getChildren()){
                    Rating rating = ratingsnapshot.getValue(Rating.class);
                    if(rating != null){
                        totalrankings++;
                        totalrankingvalue += rating.getRatingValue();
                    }
                }
                float averageratings = totalrankingvalue / totalrankings;
                rating.setRating(averageratings);
                String score = Float.toString(averageratings);

                if(Float.isNaN(averageratings) ){
                    ratingscore.setText("Unrated");
                }else
                    ratingscore.setText(dfZero.format(averageratings));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentedt = txtcomment.getText().toString();
                if(TextUtils.isEmpty(commentedt))
                {
                    txtcomment.setError("You need to fill comment field!");
                    txtadress.requestFocus();
                }else
                {
                    DatabaseReference referencecomment = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Comments");
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    DateFormat df = new SimpleDateFormat("EEE, d MMM , HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    Comment comment = new Comment(title,userid,commentedt,date);
                    String key = referencecomment.push().getKey();
                    referencecomment.child(key).setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"You are succesfuly added comment!",Toast.LENGTH_LONG).show();
                                txtcomment.getText().clear();
                            }else
                                Toast.makeText(getContext(),"Adding comment failed!",Toast.LENGTH_LONG).show();
                        }
                    });

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users")
                            .child(userid);
                    referenceProfile.child("points").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Integer currentpoints = snapshot.getValue(Integer.class);
                                if(currentpoints!=null){
                                    int updatedpoints = currentpoints+5;
                                    referenceProfile.child("points").setValue(updatedpoints).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                }


            }
        });

        DatabaseReference referenceplace = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if(firebaseUser==null){
            Toast.makeText(DetailFragment.this.getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
        }else {
            showUserProfile(firebaseUser);
        }

        return view;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users");


        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    firstname = user.firstname;
                    lastname = user.lastname;
                    username = user.username;

                    storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pic").child(username + ".jpg");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (getActivity() == null) {
                                return;
                            }
                            Glide.with(DetailFragment.this)
                                    .load(uri)
                                    .error(R.drawable.ic_launcher_background)
                                    .circleCrop()
                                    .into(commentimage);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}