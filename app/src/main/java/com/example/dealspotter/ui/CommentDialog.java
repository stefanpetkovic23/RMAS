package com.example.dealspotter.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dealspotter.Adapters.CommentAdapter;
import com.example.dealspotter.Adapters.LeaderBoardAdapter;
import com.example.dealspotter.Models.Comment;
import com.example.dealspotter.Models.Place;
import com.example.dealspotter.Models.User;
import com.example.dealspotter.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentDialog extends Dialog {
    private String propertyID;

    ArrayList<Comment> comments;
    CommentAdapter adapter;

    public CommentDialog(@NonNull Context context, String propertyID) {
        super(context);
        this.propertyID = propertyID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentdialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        final RecyclerView recycle = findViewById(R.id.recyclecommentsdialog);
        comments = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(getContext(),comments);
        recycle.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance("https://deal-spotter-68691-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Comments");
        reference.orderByChild("properdyId").equalTo(propertyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    comments.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
