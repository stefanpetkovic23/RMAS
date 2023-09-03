package com.example.dealspotter.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dealspotter.Models.Rating;
import com.example.dealspotter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateDialog extends Dialog {

    private float userRate = 0;
    private String propertyID;
    private boolean hasUserRated = false;

    public interface OnRatingCompleteListener {
        void onRatingComplete();
    }

    private OnRatingCompleteListener ratingCompleteListener;

    public void setOnRatingCompleteListener(OnRatingCompleteListener listener) {
        this.ratingCompleteListener = listener;
    }


    public RateDialog(@NonNull Context context, String propertyID) {
        super(context);
        this.propertyID = propertyID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingdialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final AppCompatButton rateBtn = findViewById(R.id.rateNowBtn1);
        final AppCompatButton cancelBtn = findViewById(R.id.cancelBtn1);
        final RatingBar ratingBar = findViewById(R.id.dialogRatingbar1);

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference referencerating = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Ratings");
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userid = firebaseUser.getUid();
                Rating rating = new Rating(userid,userRate,propertyID);
                String key = referencerating.push().getKey();
                referencerating.child(key).setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            hasUserRated = true;
                            Toast.makeText(getContext(),"You are succesfuly added rating!",Toast.LENGTH_LONG).show();
                            dismiss();

                            if (ratingCompleteListener != null) {
                                ratingCompleteListener.onRatingComplete();
                            }

                        }else
                            Toast.makeText(getContext(),"Adding rating failed!",Toast.LENGTH_LONG).show();
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
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {

                userRate = rating;
            }
        });

    }
}
