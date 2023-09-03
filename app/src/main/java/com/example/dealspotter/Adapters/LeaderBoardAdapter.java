package com.example.dealspotter.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dealspotter.Models.User;
import com.example.dealspotter.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> users;
    Dialog mydialog;

    public LeaderBoardAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderBoardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboarditem,parent,false);
        LeaderBoardAdapter.MyViewHolder vHolder =new MyViewHolder(view);



        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardAdapter.MyViewHolder holder, int position) {
        User user = users.get(position);
        Integer pos = position+1;
        holder.userposition.setText(pos.toString());
        holder.username.setText(user.getUsername());
        holder.points.setText(user.getPoints().toString());
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        reference.child("Profile Pic").child(user.username+".jpg")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(holder.leaderprofile.getContext())
                                .load(uri)
                                .circleCrop()
                                .into(holder.leaderprofile);
                    }
                });

        int[] backgroundColors = {
                R.color.colorFirst,
                R.color.colorSecond,
                R.color.colorThird
        };

        if (position < backgroundColors.length) {
            holder.pozadina.setBackgroundResource(backgroundColors[position]);
        } else {
            holder.pozadina.setBackgroundResource(R.color.lightgreen);
        }

        mydialog = new Dialog(context);
        mydialog.setContentView(R.layout.profile_dialog);


        holder.itemcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dialogname = mydialog.findViewById(R.id.dialogname);
                TextView dialogphonenumber = mydialog.findViewById(R.id.dialogphone);
                TextView dialogemail = mydialog.findViewById(R.id.dialogemail);
                ImageView dialogprofilephoto = mydialog.findViewById(R.id.dialogprofile);
                dialogname.setText(user.getFirstname()+" "+user.getLastname());
                dialogphonenumber.setText(user.getPhonenumber());
                dialogemail.setText(user.getPoints()+"pts");
       StorageReference reference = FirebaseStorage.getInstance().getReference();
        reference.child("Profile Pic").child(user.getUsername()+".jpg")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .circleCrop()
                                .skipMemoryCache(true) // Skip memory caching
                                .into(dialogprofilephoto);
                    }
                });
                mydialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView leaderprofile;
        TextView userposition,username,points;

        ConstraintLayout itemcontact, pozadina;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userposition = itemView.findViewById(R.id.position);
            username = itemView.findViewById(R.id.leaderboardusername);
            points = itemView.findViewById(R.id.Points);
            leaderprofile = itemView.findViewById(R.id.leaderboardimage);
            itemcontact = itemView.findViewById(R.id.leaderboarditem);
            pozadina = itemView.findViewById(R.id.pozadina);
        }
    }
}
