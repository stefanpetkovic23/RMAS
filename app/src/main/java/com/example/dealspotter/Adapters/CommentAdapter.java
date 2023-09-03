package com.example.dealspotter.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dealspotter.Models.Comment;
import com.example.dealspotter.Models.User;
import com.example.dealspotter.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Comment> comments;
    private static DatabaseReference usersRef;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemcomment,parent,false);
        CommentAdapter.MyViewHolder vHolder =new CommentAdapter.MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.comment.setText(comment.getComment());
        holder.time.setText(comment.getDateofpost());
        String userid = comment.getUserId();
        usersRef = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered Users");
        usersRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    StorageReference reference = FirebaseStorage.getInstance().getReference();
                    reference.child("Profile Pic").child(user.username+".jpg").getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(holder.commentprofile.getContext())
                                            .load(uri)
                                            .circleCrop()
                                            .into(holder.commentprofile);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView commentprofile;
        TextView comment,time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            commentprofile = itemView.findViewById(R.id.commentimage);
            comment = itemView.findViewById(R.id.commenttext);
            time = itemView.findViewById(R.id.timeofposting);
        }
        public void bind(Comment comment){

        }
    }


}
